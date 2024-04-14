package com.my.mediaplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.util.Log;

import androidx.annotation.NonNull;

import com.my.mediaplayer.services.MediaPlayerService;

import java.util.List;

public class MediaPlayerController {
    public static final String TAG = MediaPlayerController.class.getSimpleName();

    private Activity m_vActivity;

    private MediaBrowser.ConnectionCallback m_vConnectionCallback;
    private MediaBrowser m_vMediaBrowser;
    private MediaController.Callback m_vMediaControllerCallback;

    private MediaBrowser.SubscriptionCallback m_vSubscriptionCallback = new MediaBrowser.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowser.MediaItem> children) { }
    };

    public MediaPlayerController(Activity activity, MediaController.Callback callback) {
        this.m_vActivity = activity;
        this.m_vMediaControllerCallback = callback;

        this.m_vConnectionCallback = new MediaBrowser.ConnectionCallback() {
            @Override
            public void onConnected() {
                Log.e(TAG, "Connected to Media Controller");
                MediaPlayerController.this.m_vMediaBrowser.subscribe(
                        MediaPlayerController.this.m_vMediaBrowser.getRoot(),
                        MediaPlayerController.this.m_vSubscriptionCallback);

                MediaController mediaController = new MediaController(
                        (Context) MediaPlayerController.this.m_vActivity,
                        MediaPlayerController.this.m_vMediaBrowser.getSessionToken());

                mediaController.registerCallback(MediaPlayerController.this.m_vMediaControllerCallback);

                MediaPlayerController.this.m_vActivity.setMediaController(mediaController);

                if (mediaController.getPlaybackState() == null)
                    return;

                PlaybackState playbackState = mediaController.getPlaybackState();

                if (playbackState.getState() == PlaybackState.STATE_PLAYING || playbackState.getState() == PlaybackState.STATE_PAUSED) {
                    MediaMetadata mediaMetadata = mediaController.getMetadata();
                    MediaPlayerController.this.m_vMediaControllerCallback.onMetadataChanged(mediaMetadata);
                    MediaPlayerController.this.m_vMediaControllerCallback.onPlaybackStateChanged(playbackState);
                }


            }

            @Override
            public void onConnectionFailed() {
                super.onConnectionFailed();

                Log.e(TAG, "Connection failed to Media Controller");
            }
        };
    }

    public void onStart() {
        if (this.m_vMediaBrowser != null && this.m_vMediaBrowser.isConnected())
            onDestroy();

        this.m_vMediaBrowser = new MediaBrowser(this.m_vActivity, new ComponentName(this.m_vActivity, MediaPlayerService.class), this.m_vConnectionCallback, null);
        this.m_vMediaBrowser.connect();
    }

    public void onDestroy() {
        if (this.m_vMediaBrowser == null)
            return;

        try {
            this.m_vActivity.getMediaController().unregisterCallback(this.m_vMediaControllerCallback);
        }
        catch (Exception ignore) { }

        if (this.m_vMediaBrowser.isConnected()) {
            this.m_vMediaBrowser.disconnect();
        }
    }
}
