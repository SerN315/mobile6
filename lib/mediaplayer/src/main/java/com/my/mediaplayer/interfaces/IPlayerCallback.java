package com.my.mediaplayer.interfaces;

import com.my.mediaplayer.PlaybackManager;
import com.my.mediaplayer.model.Song;

import java.util.List;

public interface IPlayerCallback {
    void onClickPlay(int queueIndex, List<Integer> queue);
    void onClickPlayIndex(int queueIndex);
    void onClickPlayNext();
    void onClickPlayPause();
    void onClickStop();
    void onClickPlayPrevious();
    void onSetSeekbar(int position);
    void onSetRepeatType(@PlaybackManager.RepeatType int repeatType);
    void onUpdateQueue(List<Song> queue, int queueIndex);
    void onDestroy();
}
