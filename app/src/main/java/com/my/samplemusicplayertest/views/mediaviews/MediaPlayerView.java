package com.my.samplemusicplayertest.views.mediaviews;

import android.annotation.SuppressLint;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.mediaplayer.PlaybackManager;
import com.my.samplemusicplayertest.glide.audiocover.AudioFileCover;
import com.my.samplemusicplayertest.threads.MediaPlayerThread;
import com.my.samplemusicplayertest.views.MediaPlayerBottomView;
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout;
import com.my.samplemusicplayertest.R;

import java.util.concurrent.TimeUnit;

public class MediaPlayerView {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PARTIAL = 1;

    private final View mRootView;

    private int mState;

    private MediaPlayerBottomView mMediaPlayer_BottomView;
    private ConstraintLayout mControlsContainer;

    private CardView m_vCardView_Art;

    private TextView m_vTextView_Title;
    private TextView m_vTextView_Artist;

    private SeekBar m_vSeekBar_Main;

    private TextView m_vTextView_CurrentDuration;
    private TextView m_vTextView_MaxDuration;

    private ExtendedFloatingActionButton m_vBtn_Repeat;
    private ExtendedFloatingActionButton m_vBtn_Prev;
    private ExtendedFloatingActionButton m_vBtn_Next;
    private FloatingActionButton m_vBtn_PlayPause;
    private ExtendedFloatingActionButton m_vBtn_Shuffle;

    @PlaybackManager.RepeatType
    public int m_vRepeatType = PlaybackManager.REPEAT_TYPE_NONE;

    private boolean m_vCanUpdateSeekbar = true;

    public MediaPlayerView(View rootView, FragmentManager fragmentManager, Lifecycle lifecycle) {
        this.mRootView = rootView;

        this.mMediaPlayer_BottomView = new MediaPlayerBottomView(findViewById(R.id.media_player_bottom_sheet_behavior), fragmentManager, lifecycle);
        this.mControlsContainer = findViewById(R.id.media_player_controls_container);

        this.mRootView.setAlpha(0.0F);

        this.m_vCardView_Art = this.mControlsContainer.findViewById(R.id.card_view_artist_art_container);
        this.m_vTextView_Title = this.mControlsContainer.findViewById(R.id.text_view_song_title);
        this.m_vTextView_Artist = this.mControlsContainer.findViewById(R.id.text_view_song_artist);

        this.m_vSeekBar_Main = this.mControlsContainer.findViewById(R.id.seek_bar_main);

        this.m_vSeekBar_Main.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int final_value;
            boolean isUser;

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                this.final_value = value;
                this.isUser = fromUser;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                m_vCanUpdateSeekbar = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isUser) {
                    MediaPlayerThread.getInstance().getCallback().onSetSeekbar(final_value);
                }
                m_vCanUpdateSeekbar = true;
            }
        });

        this.m_vTextView_CurrentDuration = this.mControlsContainer.findViewById(R.id.text_view_song_current_duration);
        this.m_vTextView_MaxDuration = this.mControlsContainer.findViewById(R.id.text_view_song_max_duration);

        this.m_vBtn_Repeat = findViewById(R.id.btn_repeat);
        this.m_vBtn_Prev = findViewById(R.id.btn_skip_previous);
        this.m_vBtn_PlayPause = findViewById(R.id.btn_play_pause);
        this.m_vBtn_Next = findViewById(R.id.btn_skip_next);
        this.m_vBtn_Shuffle = findViewById(R.id.btn_shuffle);

        this.m_vBtn_Repeat.setOnClickListener((v) -> {
            if (m_vRepeatType < 2) {
                m_vRepeatType++;
            }
            else {
                m_vRepeatType = PlaybackManager.REPEAT_TYPE_NONE;
            }

            switch (m_vRepeatType) {
                case PlaybackManager.REPEAT_TYPE_NONE:
                    this.m_vBtn_Repeat.setIconResource(com.realgear.icons_pack.R.drawable.ic_repeat_24px);
                    this.m_vBtn_Repeat.setAlpha(0.5F);
                    break;

                case PlaybackManager.REPEAT_TYPE_ONE:
                    this.m_vBtn_Repeat.setIconResource(com.realgear.icons_pack.R.drawable.ic_repeat_one_24px);
                    this.m_vBtn_Repeat.setAlpha(1F);
                    break;

                case PlaybackManager.REPEAT_TYPE_ALL:
                    this.m_vBtn_Repeat.setIconResource(com.realgear.icons_pack.R.drawable.ic_repeat_24px);
                    this.m_vBtn_Repeat.setAlpha(1F);
                    break;
            }

            MediaPlayerThread.getInstance().getCallback().onSetRepeatType(this.m_vRepeatType);
        });

        this.m_vBtn_Prev.setOnClickListener((v) -> {
            MediaPlayerThread.getInstance().getCallback().onClickPlayPrevious();
        });
        this.m_vBtn_PlayPause.setOnClickListener((v) -> {
            MediaPlayerThread.getInstance().getCallback().onClickPlayPause();
        });
        this.m_vBtn_Next.setOnClickListener((v) -> {
            MediaPlayerThread.getInstance().getCallback().onClickPlayNext();
        });
        this.m_vBtn_Shuffle.setOnClickListener((v) -> { });
    }

    public void onUpdateMetadata(MediaMetadata mediaMetadata) {
        this.m_vTextView_Title.setText(mediaMetadata.getText(MediaMetadata.METADATA_KEY_TITLE));
        this.m_vTextView_Artist.setText(mediaMetadata.getText(MediaMetadata.METADATA_KEY_ARTIST));

        this.m_vSeekBar_Main.setProgress(0);
        this.m_vSeekBar_Main.setMax((int)mediaMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION));

        String path = mediaMetadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI);

        ImageView imgView = (ImageView) this.m_vCardView_Art.getChildAt(0);
        if (imgView != null) {
            Glide.with(this.getRootView().getContext())
                    .load(new AudioFileCover(path))
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(com.realgear.icons_pack.R.drawable.ic_album_24px)
                    .into(imgView);
        }

        this.m_vTextView_MaxDuration.setText(getTimeFormat(mediaMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION)));
    }

    public void onPlaybackStateChanged(PlaybackState state) {
        if (m_vCanUpdateSeekbar) {
            this.m_vSeekBar_Main.setProgress((int) state.getPosition());
        }
        this.m_vBtn_PlayPause.setImageResource(state.getState() != PlaybackState.STATE_PLAYING ? com.realgear.icons_pack.R.drawable.ic_play_arrow_24px : com.realgear.icons_pack.R.drawable.ic_pause_24px);
        this.m_vTextView_CurrentDuration.setText(getTimeFormat(state.getPosition()));
    }

    public View getRootView() {
        return this.mRootView;
    }

    public void onSliding(float slideOffset, int state) {
        float fadeStart = 0.25F;
        float alpha = (slideOffset - fadeStart) * (1F / (1F - fadeStart));

        if (state == STATE_NORMAL) {
            this.mRootView.setAlpha(alpha);
            this.mControlsContainer.setAlpha(1F);
        }
        else {
            this.mControlsContainer.setAlpha(1F - alpha);
        }
        this.mState = state;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return this.mRootView.findViewById(id);
    }

    public void onPanelStateChanged(int panelSate) {
        if (panelSate == MultiSlidingUpPanelLayout.COLLAPSED) {
            this.mRootView.setVisibility(View.INVISIBLE);
        }
        else
            this.mRootView.setVisibility(View.VISIBLE);
    }

    @SuppressLint("DefaultLocale")
    public String getTimeFormat(long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
