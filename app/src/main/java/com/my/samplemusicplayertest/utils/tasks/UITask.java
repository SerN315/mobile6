package com.my.samplemusicplayertest.utils.tasks;

public class UITask {
    public enum Priority {
        HIGH,
        NORMAL,
        LOW
    }


    private UITaskRunnable m_vTask;
    private int m_vDelay;

    private int m_vId;

    private Priority m_vPriority;

    private boolean m_vRunInBackground;

    public UITask(UITaskRunnable task, int delay, int id) {
        this.m_vTask = task;
        this.m_vDelay = delay;
        this.m_vId = id;
        this.m_vPriority = Priority.NORMAL;
        this.m_vRunInBackground = false;
    }

    public UITask(UITaskRunnable task, int delay, int id, Priority priority) {
        this.m_vTask = task;
        this.m_vDelay = delay;
        this.m_vId = id;
        this.m_vPriority = priority;
        this.m_vRunInBackground = false;
    }

    public UITask(UITaskRunnable task, int delay, int id, Priority priority, boolean runInBackground) {
        this.m_vTask = task;
        this.m_vDelay = delay;
        this.m_vId = id;
        this.m_vPriority = priority;
        this.m_vRunInBackground = runInBackground;
    }

    public UITaskRunnable getTask() {
        return this.m_vTask;
    }

    public int getDelay() {
        return this.m_vDelay;
    }

    public int getId() {
        return this.m_vId;
    }

    public Priority getPriority() {
        return this.m_vPriority;
    }

    public boolean isBackgroundTask() {
        return this.m_vRunInBackground;
    }
}
