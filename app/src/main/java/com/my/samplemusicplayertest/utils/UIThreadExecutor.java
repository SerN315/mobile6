package com.my.samplemusicplayertest.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

public class UIThreadExecutor implements Executor {

    private Handler m_vHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        this.m_vHandler.post(runnable);
    }
}
