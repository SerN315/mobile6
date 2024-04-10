package com.my.samplemusicplayertest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreferenceInstance {
    private static SharedPreferenceInstance m_vInstance;

    private Activity m_vActivity;
    public SharedPreferences m_vSharedPreferences;

    private SharedPreferenceInstance(@NonNull Activity activity) {
        this.m_vActivity = activity;
    }

    public static void init(@NonNull Activity activity) {
        m_vInstance = new SharedPreferenceInstance(activity);
    }

    public SharedPreferenceInstance getInstance() {
        return m_vInstance;
    }


    public SharedPreferences getPreferences(String preferenceKey) {
        return this.m_vActivity.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
    }
}
