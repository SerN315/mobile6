package com.my.samplemusicplayertest.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

public class PermissionManager {
    public static boolean isPermissionGranted (Activity activity, String permission) {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        activity.requestPermissions(new String[] { permission }, requestCode);
    }
}
