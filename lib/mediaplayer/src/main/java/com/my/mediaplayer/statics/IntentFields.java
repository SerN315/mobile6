package com.my.mediaplayer.statics;

import android.content.Intent;

public class IntentFields {
    // Media Notification Constants
    public static final String ACTION_NEXT = "com.my.samplemusicplayertest.mediaplayer.NEXT";
    public static final String ACTION_PAUSE = "com.my.samplemusicplayertest.mediaplayer.PAUSE";
    public static final String ACTION_PLAY = "com.my.samplemusicplayertest.mediaplayer.PLAY";
    public static final String ACTION_PREV = "com.my.samplemusicplayertest.mediaplayer.PREV";
    public static final String CHANNEL_ID = "com.my.samplemusicplayertest";

    // Activities Constants
    public static final String EXTRA_REPEAT_STATE = "RepeatState";
    public static final String EXTRA_TRACKS_LIST = "TracksList";
    public static final String EXTRA_TRACK_ID = "TrackId";
    public static final String EXTRA_TRACK_INDEX = "TrackIndex";
    public static final String EXTRA_TRACK_POSITION = "TrackPosition";

    // MediaPlayerService Constants
    public static final String INTENT_ADD_TRACK_NEXT = "com.my.samplemusicplayertest.service.MediaPlayerService.AddTrackNext";
    public static final String INTENT_CHANGE_REPEAT = "com.my.samplemusicplayertest.service.MediaPlayerService.ChangeRepeat";
    public static final String INTENT_PLAY = "com.my.samplemusicplayertest.service.MediaPlayerService.Play";
    public static final String INTENT_STOP = "com.my.samplemusicplayertest.service.MediaPlayerService.Stop";
    public static final String INTENT_PLAY_INDEX = "com.my.samplemusicplayertest.service.MediaPlayerService.PlayIndex";
    public static final String INTENT_PLAY_NEXT = "com.my.samplemusicplayertest.service.MediaPlayerService.PlayNext";
    public static final String INTENT_PLAY_PAUSE = "com.my.samplemusicplayertest.service.MediaPlayerService.PlayPause";
    public static final String INTENT_PLAY_PREV = "com.my.samplemusicplayertest.service.MediaPlayerService.PlayPrev";
    public static final String INTENT_SET_SEEKBAR = "com.my.samplemusicplayertest.service.MediaPlayerService.SetSeekbar";
    public static final String INTENT_UPDATE_QUEUE = "com.my.samplemusicplayertest.service.MediaPlayerService.UpdateQueue";

}
