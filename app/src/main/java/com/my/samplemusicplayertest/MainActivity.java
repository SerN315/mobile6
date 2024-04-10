package com.my.samplemusicplayertest;

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

public class MainActivity extends AppCompatActivity {

    private UIThread m_vThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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