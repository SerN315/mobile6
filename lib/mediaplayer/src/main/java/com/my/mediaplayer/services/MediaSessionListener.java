package com.my.mediaplayer.services;

import android.media.session.MediaSession;

public class MediaSessionListener extends MediaSession.Callback {
    private final MediaPlayerService m_vService;

    public MediaSessionListener(MediaPlayerService service) {
        this.m_vService = service;
    }

    @Override
    public void onPause() {
        this.m_vService.onPause();
    }

    @Override
    public void onPlay() {
        this.m_vService.onPlay();
    }

    @Override
    public void onSeekTo(long pos) {
        this.m_vService.setSeekbarPosition((int)pos);
    }

    @Override
    public void onSkipToNext() {
        this.m_vService.onPlayNext();
    }

    @Override
    public void onSkipToPrevious() {
        this.m_vService.onPlayPrev();
    }

    @Override
    public void onStop() {
        this.m_vService.getNotificationManager().onStop();
        this.m_vService.stopSelf();
    }
}
