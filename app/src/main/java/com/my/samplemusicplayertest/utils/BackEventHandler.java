package com.my.samplemusicplayertest.utils;

import java.util.ArrayList;
import java.util.List;

public class BackEventHandler {

    private static BackEventHandler m_vInstance;

    public List<Runnable> m_vBackEvents;

    private BackEventHandler() {
        this.m_vBackEvents = new ArrayList<>();
    }

    public void addBackEvent(Runnable runnable){
        if (this.m_vBackEvents.contains(runnable))
            return;

        this.m_vBackEvents.add(runnable);
    }

    public Runnable getBackEvent() {
        int index = this.m_vBackEvents.size() - 1;

        if (index < 0)
            return null;

        Runnable item = this.m_vBackEvents.get(index);
        this.m_vBackEvents.remove(index);
        return item;
    }

    public void removeBackEvent(Runnable runnable){
        if (!this.m_vBackEvents.contains(runnable))
            return;

        this.m_vBackEvents.remove(runnable);
    }

    public static BackEventHandler getInstance() {
        if (m_vInstance == null)
            m_vInstance = new BackEventHandler();

        return m_vInstance;
    }
}
