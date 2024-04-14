package com.my.mediaplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.IntDef;

import com.my.mediaplayer.interfaces.IPlaybackCallback;
import com.my.mediaplayer.utils.PlaybackListener;
import com.my.mediaplayer.utils.PlaybackSubThread;
import com.my.mediaplayer.database.LibraryRepository;
import com.my.mediaplayer.model.Song;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PlaybackManager {
    public static final String TAG = PlaybackManager.class.getSimpleName();

    public final int THREAD_UPDATE_INTERVAL = 350;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REPEAT_TYPE_NONE, REPEAT_TYPE_ONE, REPEAT_TYPE_ALL})
    public @interface RepeatType { }
    public static final int REPEAT_TYPE_NONE = 0;
    public static final int REPEAT_TYPE_ONE = 1;
    public static final int REPEAT_TYPE_ALL = 2;

    @RepeatType
    private int m_vRepeatType = REPEAT_TYPE_NONE;

    private int m_vPlaybackState;

    private boolean m_vIsPrepared;
    private boolean m_vPlayOnFocusGain;

    private final IPlaybackCallback m_vCallback;
    private final AudioManager m_vAudioManager;
    private final PlaybackListener m_vListener;
    private final Handler m_vUIHandler;
    private final Context m_vContext;

    private AudioFocusRequest m_vAudioFocusRequest;
    private MediaPlayer m_vMediaPlayer;

    private int m_vCurrentQueueIndex;
    private Song m_vCurrentSong;

    private List<Integer> m_vQueue;
    private TreeMap<Integer, Song> m_vSongs;
    private List<PlaybackSubThread> m_vThreads;

    public PlaybackManager(Context context, IPlaybackCallback playbackCallback) {
        this.m_vContext = context;
        this.m_vCallback = playbackCallback;

        this.m_vListener = new PlaybackListener(this);
        this.m_vUIHandler = new Handler();
        this.m_vAudioManager = (AudioManager) context.getSystemService(AudioManager.class);

        this.m_vQueue = new ArrayList<>();
        this.m_vThreads = new ArrayList<>();

        //LibraryManager.initLibrary(context);

        LibraryRepository libraryRepository = new LibraryRepository(context);
        libraryRepository.getSongs().observeForever(items -> {
            this.m_vSongs = LibraryManager.getTreemapOfSongs(items);
        });

        //this.m_vSongs = LibraryManager.getTreemapOfSongs(Objects.requireNonNull(libraryRepository.getSongs().getValue()));
    }


    private void onAbandonAudioFocus() {
        if (this.m_vAudioFocusRequest != null) {
            this.m_vAudioManager.abandonAudioFocusRequest(this.m_vAudioFocusRequest);
        }
    }

    private boolean onGetAudioFocus() {
        if(this.m_vAudioFocusRequest == null) {
            AudioFocusRequest.Builder builder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN);
            builder.setFocusGain(AudioManager.AUDIOFOCUS_GAIN);
            builder.setAcceptsDelayedFocusGain(true);
            builder.setOnAudioFocusChangeListener(this.m_vListener);

            builder.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());

            this.m_vAudioFocusRequest = builder.build();
        }
        return this.m_vAudioManager.requestAudioFocus(this.m_vAudioFocusRequest) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void onPlay() {
        this.m_vMediaPlayer.start();
        this.m_vPlaybackState = PlaybackState.STATE_PLAYING;
        this.onUpdatePlaybackState();
    }

    private void onRelease() {
        if(this.m_vMediaPlayer != null) {
            this.m_vMediaPlayer.reset();
            this.m_vMediaPlayer.release();
            this.m_vMediaPlayer = null;
        }
    }

    private void onStartMediaPlayer() {
        this.m_vIsPrepared = true;
        if(!this.m_vPlayOnFocusGain)
            onPlay();
    }

    private void onStoppingThreads() {
        if (this.m_vThreads.size() > 0) {
            for (PlaybackSubThread thread : this.m_vThreads) {
                thread.onStop();
                this.m_vThreads.remove(thread);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void onUpdatePlaybackState() {
        if (this.m_vCallback == null)
            return;

        onStoppingThreads();

        if (this.m_vPlaybackState == PlaybackState.STATE_PLAYING) {
            PlaybackSubThread thread = new PlaybackSubThread(THREAD_UPDATE_INTERVAL, this);
            thread.getWorker().setName("Playback Sub Thread");
            this.m_vThreads.add(thread);
            thread.onStart();
        }
        else {
            PlaybackState.Builder builder = new PlaybackState.Builder()
                    .setActions(getAvailableActions())
                    .setState(getPlaybackState(), getPlaybackPosition(), 1.0F, SystemClock.elapsedRealtime());

            this.m_vCallback.onPlaybackStateChanged(builder.build());
        }
    }

    // Playback Actions
    public boolean canPlayNext() {
        return (this.m_vCurrentQueueIndex + 1) < this.m_vQueue.size();
    }

    public boolean canPlayPrev() {
        return (this.m_vCurrentQueueIndex - 1) >= 0;
    }

    public long getAvailableActions() {
        long actions = PlaybackState.ACTION_PLAY;
        if(isPlaying()) {
            actions |= PlaybackState.ACTION_PAUSE;
        }

        return actions | PlaybackState.ACTION_STOP | PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS | PlaybackState.ACTION_SEEK_TO;
    }

    public int getCurrentQueueIndex() {
        return this.m_vCurrentQueueIndex;
    }

    public Song getCurrentSong() {
        return this.m_vCurrentSong;
    }

    public int getPlaybackPosition() {
        if(isPlayingOrPaused()) {
            return this.m_vMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getPlaybackState() {
        return this.m_vPlaybackState;
    }

    public IPlaybackCallback getPlaybackCallback() {
        return this.m_vCallback;
    }

    public boolean isPlaying() {
        if(!this.m_vPlayOnFocusGain && this.m_vMediaPlayer != null && this.m_vPlaybackState == PlaybackState.STATE_PLAYING) {
            return this.m_vMediaPlayer.isPlaying();
        }

        return false;
    }

    public boolean isPlayingOrPaused() {
        return (isPlaying() || (this.m_vMediaPlayer != null && this.m_vPlaybackState == PlaybackState.STATE_PAUSED));
    }

    public void onAudioCompleted() {
        if (isPlaying()) {
            onStop();
        }

        if (!isPlaying()) {
            switch (this.m_vRepeatType) {
                case REPEAT_TYPE_NONE:
                    this.m_vPlaybackState = PlaybackState.STATE_PAUSED;
                    this.m_vMediaPlayer.pause();
                    this.m_vMediaPlayer.seekTo(0);
                    onUpdatePlaybackState();
                    break;
                case REPEAT_TYPE_ONE:
                    onPlayIndex(this.m_vCurrentQueueIndex);
                    break;
                case REPEAT_TYPE_ALL:
                    if (canPlayNext())
                        onPlayNext();
                    else
                        onPlayIndex(0);
                    break;
            }


        }
    }

    public void onAudioFocusChanged(int focusChanged) {
        switch (focusChanged) {
            default:
                if (isPlaying()) {
                    onPause();
                    this.m_vPlayOnFocusGain = true;
                }
                return;
            case AudioManager.AUDIOFOCUS_GAIN:
                break;
        }

        if (this.m_vPlayOnFocusGain) {
            this.m_vPlayOnFocusGain = false;
            onPlay();
        }
    }


    public void onPause() {
        if (isPlaying())
            this.m_vMediaPlayer.pause();

        this.m_vPlaybackState = PlaybackState.STATE_PAUSED;
        onUpdatePlaybackState();
    }

    public void onPlayIndex(int queueIndex) {
        int id = this.m_vQueue.get(queueIndex);
        Song songToPlay = this.m_vSongs.get(id);
        boolean isEqual = false;

        if (m_vCurrentSong != null && queueIndex == m_vCurrentQueueIndex && m_vCurrentSong.getId() == songToPlay.getId())
            isEqual = true;

        if (this.m_vMediaPlayer == null) {
            m_vMediaPlayer = new MediaPlayer();
            this.m_vMediaPlayer.setWakeMode(this.m_vContext, 1);
            this.m_vMediaPlayer.setOnCompletionListener(this.m_vListener);
            this.m_vMediaPlayer.setOnPreparedListener(this.m_vListener);
        }
        else if (!isEqual || this.m_vPlaybackState == PlaybackState.STATE_STOPPED) {
            this.m_vPlaybackState = PlaybackState.STATE_NONE;
            this.m_vMediaPlayer.reset();
            this.m_vIsPrepared = false;
        }

        this.m_vPlayOnFocusGain = onGetAudioFocus();
        if (!isEqual || this.m_vPlaybackState == PlaybackState.STATE_STOPPED) {
            this.m_vCallback.onUpdateMetadata(songToPlay);
            this.m_vCurrentSong = songToPlay;
            this.m_vCurrentQueueIndex = queueIndex;

            if (songToPlay != null) {
                try {
                    this.m_vMediaPlayer.setDataSource(songToPlay.getData());
                }
                catch (Exception ignore) {}

                this.m_vMediaPlayer.prepareAsync();
            }
            else
                onStartMediaPlayer();
        }
        else {
            onStartMediaPlayer();
        }
    }

    public void onPlayNext() {
        if (canPlayNext())
            onPlayIndex(this.m_vCurrentQueueIndex + 1);
    }

    public void onPlayPause() {
        if (m_vMediaPlayer == null)
            return;

        if (m_vMediaPlayer.isPlaying())
            onPause();
        else
            onPlayIndex(m_vCurrentQueueIndex);
    }

    public void onPlayPrevious() {
        if (canPlayPrev())
            onPlayIndex(m_vCurrentQueueIndex - 1);
    }

    public void onSeekTo(long position) {
        if (this.m_vMediaPlayer != null && isPlayingOrPaused()) {
            this.m_vMediaPlayer.seekTo((int)position);
            onUpdatePlaybackState();
        }
    }

    public void onSetQueue(List<Integer> queue) {
        this.m_vQueue = queue;
    }

    public void onSetRepeatState(@RepeatType int repeatState) {
        this.m_vRepeatType = repeatState;
    }

    public void onStartMediaPlayer(MediaPlayer mediaPlayer) {
        if (this.m_vMediaPlayer != mediaPlayer)
            return;

        onStartMediaPlayer();
    }

    public void onStop() {
        if (this.m_vMediaPlayer != null) {
            this.m_vPlaybackState = PlaybackState.STATE_STOPPED;
            onUpdatePlaybackState();
            onAbandonAudioFocus();
            onRelease();
        }
    }

    public void onUpdateIndex(int index) {
        this.m_vCurrentQueueIndex = index;
    }
}
