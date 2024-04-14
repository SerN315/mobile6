package com.my.samplemusicplayertest.utils.tasks;

public class UITaskRunnable implements Runnable {

    private boolean m_vAutoNotify;
    private Runnable m_vTask;

    public UITaskRunnable(Runnable task, boolean autoNotify) {
        this.m_vTask = task;
        this.m_vAutoNotify = autoNotify;
    }

    @Override
    public void run() {
        this.m_vTask.run();

        if(this.m_vAutoNotify) {
            this.onComplete();
        }
    }

    public void onComplete() {
        UITaskExecute.getInstance().onTaskComplete();
    }
}
