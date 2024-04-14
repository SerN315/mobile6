package com.my.samplemusicplayertest.utils;

import android.content.Context;

import androidx.annotation.DimenRes;

public class ViewsUtil {
    public static int dp2px(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
}
