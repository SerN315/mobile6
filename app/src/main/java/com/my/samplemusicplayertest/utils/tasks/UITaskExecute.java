package com.my.samplemusicplayertest.utils.tasks;

import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;

public class UITaskExecute {
    private static UITaskExecute m_vInstance;

    private Handler m_vUIHandler;

    public Queue<UITask> m_vTasks_High;
    public Queue<UITask> m_vTasks_Normal;

    public Queue<UITask> m_vTasks_Low;

    private UITask m_vLastTaskHandled;

    private boolean m_vIsRunning;

    private UITaskExecute() {
        this.m_vTasks_High = new ArrayDeque<>();
        this.m_vTasks_Normal = new ArrayDeque<>();
        this.m_vTasks_Low = new ArrayDeque<>();

        this.m_vIsRunning = false;
    }

    public void setHandler(@NotNull Handler handler) {
        this.m_vUIHandler = handler;
    }

    public void newTask(UITask task) {
        Queue<UITask> temp_queue;

        switch (task.getPriority()) {
            case HIGH: {
                temp_queue = new ArrayDeque<>(this.m_vTasks_High);
                if (containsTask(temp_queue, task))
                    return;

                this.m_vTasks_High.add(task);
            }
            break;

            case NORMAL: {
                temp_queue = new ArrayDeque<>(this.m_vTasks_Normal);
                if (containsTask(temp_queue, task))
                    return;

                this.m_vTasks_Normal.add(task);
            }
            break;

            case LOW: {
                temp_queue = new ArrayDeque<>(this.m_vTasks_Low);
                if (containsTask(temp_queue, task))
                    return;

                this.m_vTasks_Low.add(task);
            }
            break;
        }

        if (!this.m_vIsRunning) {
            this.m_vIsRunning = true;
            this.onDequeue();
        }
    }

    public boolean containsTask(Queue<UITask> queue, UITask task) {
        for (UITask item : queue) {
            if (item.getId() == task.getId())
                return true;
        }

        return false;
    }

    public static void execute(UITask task) {
        m_vInstance.newTask(task);
    }

    public void onTaskComplete() {
        if (this.m_vLastTaskHandled == null) {
            onDequeue();
            return;
        }

        switch (this.m_vLastTaskHandled.getPriority()) {
            case HIGH:
                this.m_vTasks_High.remove(this.m_vLastTaskHandled);
                break;

            case NORMAL:
                this.m_vTasks_Normal.remove(this.m_vLastTaskHandled);
                break;

            case LOW:
                this.m_vTasks_Low.remove(this.m_vLastTaskHandled);
                break;
        }

        this.onDequeue();
    }

    public void onDequeue() {
        UITask task = getTaskByPriority();
        if (task == null) {
            this.m_vIsRunning = false;
            return;
        }

        this.m_vLastTaskHandled = task;

        if (this.m_vLastTaskHandled.isBackgroundTask()) {
            new Thread(this::executeInBackground).start();
        }
        else {
            this.executeInUIThread();
        }
    }

    private void executeInBackground() {
        if (this.m_vLastTaskHandled == null) {
            this.onTaskComplete();
            return;
        }

        try {
            Thread.sleep(this.m_vLastTaskHandled.getDelay());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.m_vLastTaskHandled.getTask().run();
    }

    private void executeInUIThread() {
        if (this.m_vLastTaskHandled == null) {
            this.onTaskComplete();
            return;
        }

        this.m_vUIHandler.postDelayed(this.m_vLastTaskHandled.getTask(), this.m_vLastTaskHandled.getDelay());
    }

    public UITask getTaskByPriority() {
        UITask task = this.m_vTasks_High.peek();
        if (task == null) {
            task = this.m_vTasks_Normal.peek();
            if (task == null) {
                task = this.m_vTasks_Low.peek();
            }
        }

        return task;
    }

    public static UITaskExecute getInstance() {
        if (m_vInstance == null) {
            m_vInstance = new UITaskExecute();
        }

        return m_vInstance;
    }
}
