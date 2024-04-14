package com.my.mediaplayer.interfaces;

import android.media.session.PlaybackState;

import com.my.mediaplayer.model.Song;

public interface IPlaybackCallback {
    void onPlaybackStateChanged(PlaybackState playbackState);
    void onUpdateMetadata(Song song);
}
