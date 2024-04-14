package com.my.samplemusicplayertest.utils.tasks;

public interface ITask {
    void run();

    default UITask build(int delay, boolean autoNotify, int id) {
        return new UITask(new UITaskRunnable(ITask.this::run, autoNotify), delay, id);
    }

    default UITask build(int delay, boolean autoNotify, int id, UITask.Priority priority) {
        return new UITask(new UITaskRunnable(ITask.this::run, autoNotify), delay, id, priority);
    }

    default UITask build(int delay, boolean autoNotify, int id, UITask.Priority priority, boolean runInBackgroundThread) {
        return new UITask(new UITaskRunnable(ITask.this::run, autoNotify), delay, id, priority, runInBackgroundThread);
    }
}
