package com.my.mediaplayer.utils;

import android.annotation.SuppressLint;
import android.media.session.PlaybackState;
import android.os.SystemClock;

import com.my.mediaplayer.PlaybackManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlaybackSubThread implements Runnable {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicBoolean isStopped = new AtomicBoolean(false);

    private final int m_vInterval;
    private final PlaybackManager m_vPlaybackManager;
    private final Thread m_vWorker;

    public PlaybackSubThread(int interval, PlaybackManager playbackManager) {
        this.m_vPlaybackManager = playbackManager;
        this.m_vInterval = interval;
        this.m_vWorker = new Thread(this);
    }

    @Override
    public void run() {
        this.isRunning.set(true);
        this.isStopped.set(false);

        while (isRunning()) {
            int state = this.m_vPlaybackManager.getPlaybackState();

            if (this.m_vPlaybackManager.isPlayingOrPaused()) {
                @SuppressLint("WrongConstant")
                PlaybackState.Builder builder = new PlaybackState.Builder()
                        .setActions(this.m_vPlaybackManager.getAvailableActions())
                        .setState(state, this.m_vPlaybackManager.getPlaybackPosition(), 1.0F, SystemClock.elapsedRealtime());

                this.m_vPlaybackManager.getPlaybackCallback().onPlaybackStateChanged(builder.build());
                if (this.m_vPlaybackManager.getPlaybackState() != PlaybackState.STATE_PLAYING)
                    onStop();
            }

            try {
                Thread.sleep(this.m_vInterval);
            }
            catch (InterruptedException ignore) {
                interrupt();
            }
        }

        this.isStopped.set(false);
    }

    private void interrupt() {
        this.isRunning.set(false);
        this.m_vWorker.interrupt();
    }

    public Thread getWorker() { return this.m_vWorker; }

    private boolean isRunning() { return this.isRunning.get(); }

    private boolean isStopped() { return this.isStopped.get(); }

    public void onStart() { this.m_vWorker.start(); }

    public void onStop() { this.interrupt(); }
}
