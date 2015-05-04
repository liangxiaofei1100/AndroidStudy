package com.example.alex.tmp;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.alex.BootActivity;
import com.example.alex.common.ShowResultActivity;

public class LauncherIconHide extends ShowResultActivity {
    private static final String TAG = LauncherIconHide.class.getSimpleName();

    private boolean mIsLauncherIconShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsLauncherIconShown = isLauncherIconShown();
    }

    @Override
    protected void onButtonClick() {
        if (mIsLauncherIconShown) {
            Log.d(TAG, "hide");
            hide();
            mIsLauncherIconShown = false;
        } else {
            Log.d(TAG, "show");
            show();
            mIsLauncherIconShown = true;
        }
    }

    private boolean isLauncherIconShown() {
        PackageManager packageManager = getPackageManager();

        int setting = packageManager
                .getComponentEnabledSetting(new ComponentName(this,
                        BootActivity.class));
        if (setting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return true;
        } else {
            return false;
        }
    }

    private void hide() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this,
                BootActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    private void show() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this,
                BootActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }
}
