package com.example.alex.tmp;

import com.example.alex.common.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {
    private static final String TAG = BootReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent);
        Log.d(TAG, "hide");
//        LauncherIconVisiblility.hide(context);
    }
}
