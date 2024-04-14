package com.my.mediaplayer.interfaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public interface ICustomBroadcastReceiver {
    void onReceive(Context context, Intent intent);

    default BroadcastReceiver build() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ICustomBroadcastReceiver.this.onReceive(context, intent);
            }
        };
    }

    static BroadcastReceiver onCreate (ICustomBroadcastReceiver args) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                args.onReceive(context, intent);
            }
        };
    }
}
