package com.example.alex.tmp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.example.alex.BootActivity;

/**
 * 通过packageManager.setComponentEnabledSetting(...)方法控制Activity是否在Launcher中显示图标。
 * 
 */
public class LauncherIconVisiblility {

    /**
     * 判断Activity是否在Launcher中显示图标。
     * 
     * @param context
     * @param activityClass
     * @return
     */
    public static boolean isLauncherIconShown(Context context,
            Class<Activity> activityClass) {
        PackageManager packageManager = context.getPackageManager();

        int setting = packageManager
                .getComponentEnabledSetting(new ComponentName(context,
                        activityClass));
        if (setting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 隐藏Activity在Launcher中的图标。
     * 
     * @param context
     * @param activityClass
     */
    public static void hide(Context context, Class<Activity> activityClass) {
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(context,
                activityClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    /**
     * 显示Activity在Launcher中的图标。
     * 
     * @param context
     */
    public static void show(Context context, Class<Activity> activityClass) {
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(context,
                BootActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }
}
