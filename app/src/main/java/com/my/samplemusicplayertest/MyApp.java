package com.my.samplemusicplayertest;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.preference.PreferenceManager;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        adjustFontScale(getResources().getConfiguration(), getFontSizePref());
    }

    private void adjustFontScale(Configuration configuration, String fontSize) {
        float scale = fontSize.equals("small") ? 0.85f :
                fontSize.equals("normal") ? 1.0f :
                        fontSize.equals("large") ? 1.15f : 1.0f;

        configuration.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getResources().updateConfiguration(configuration, metrics);
    }

    private String getFontSizePref() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("font_size", "normal");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
