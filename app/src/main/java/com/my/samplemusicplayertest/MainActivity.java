package com.my.samplemusicplayertest;

import static androidx.core.graphics.drawable.DrawableCompat.applyTheme;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import com.my.samplemusicplayertest.threads.UIThread;
import com.my.samplemusicplayertest.utils.BackEventHandler;
import com.my.samplemusicplayertest.utils.PermissionManager;
import com.my.samplemusicplayertest.utils.tasks.UITaskExecute;
import com.my.samplemusicplayertest.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private UIThread m_vThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyTheme(getDarkModePref());
        createNotificationChannel();


        BackEventHandler.getInstance();

        PermissionManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PermissionManager.requestPermission(this, Manifest.permission.FOREGROUND_SERVICE, 100);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionManager.requestPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE, 100);
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        UITaskExecute.getInstance().setHandler(new Handler());

        this.m_vThread = new UIThread(this);
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

    private boolean getDarkModePref() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("dark_mode", false);
    }

    private void applyTheme(boolean darkMode) {
        AppCompatDelegate.setDefaultNightMode(darkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }



    @Override
    public void onBackPressed() {
        Runnable event = BackEventHandler.getInstance().getBackEvent();
        if (event != null) {
            event.run();
            return;
        }

        super.onBackPressed();
    }
}