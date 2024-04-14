package com.my.mediaplayer.statics;

public class ClassManager {
    private static ClassManager instance;

    private Class<?> m_vActivity;

    private void setActivity(Class<?> activity) {
        this.m_vActivity = activity;
    }

    public static ClassManager CreateInstance(Class<?> activity) {
        if (instance != null) {
            instance.setActivity(activity);
        }
        else {
            instance = new ClassManager();
            instance.setActivity(activity);
        }

        return instance;
    }

    public static ClassManager getInstance() {
        return instance;
    }

    public static Class<?> getActivityClass() {
        if (instance != null)
            return instance.m_vActivity;

        return null;
    }
}
