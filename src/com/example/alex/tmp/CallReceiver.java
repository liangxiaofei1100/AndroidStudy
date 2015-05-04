package com.example.alex.tmp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.example.alex.BootActivity;
import com.example.alex.common.Log;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = CallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent);

        // if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
        // boolean mIsLauncherIconShown = isLauncherIconShown(context);
        // if (mIsLauncherIconShown) {
        // Log.d(TAG, "hide");
        // hide(context);
        // mIsLauncherIconShown = false;
        // } else {
        // Log.d(TAG, "show");
        // show(context);
        // mIsLauncherIconShown = true;
        // }
        // }

    }

    private boolean isLauncherIconShown(Context context) {
        PackageManager packageManager = context.getPackageManager();

        int setting = packageManager
                .getComponentEnabledSetting(new ComponentName(context,
                        BootActivity.class));
        if (setting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return true;
        } else {
            return false;
        }
    }

    private void hide(Context context) {
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(context,
                BootActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    private void show(Context context) {
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(context,
                BootActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }
}
