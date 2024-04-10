package com.my.mediaplayer.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.my.mediaplayer.PlaybackManager;

public class PlaybackListener implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final String TAG = PlaybackListener.class.getSimpleName();

    private final PlaybackManager m_vPlaybackManager;

    public PlaybackListener(PlaybackManager playbackManager) {
        this.m_vPlaybackManager = playbackManager;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        this.m_vPlaybackManager.onAudioFocusChanged(focusChange);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.m_vPlaybackManager.onAudioCompleted();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.m_vPlaybackManager.onStartMediaPlayer(mp);
    }
}
