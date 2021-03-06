package com.example.alex.tmp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetSystemAnimation extends ShowResultActivity {
    private static final String TAG = "SetSystemAnimation";
    private static final String ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableAnimation();
    }

    private void disableAnimation() {
        int permStatus = checkCallingOrSelfPermission(ANIMATION_PERMISSION);
        permStatus = PackageManager.PERMISSION_GRANTED;
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            if (reflectivelyDisableAnimation()) {
                Log.i(TAG, "All animations disabled.");
            } else {
                Log.i(TAG, "Could not disable animations.");
            }
        } else {
            Log.i(TAG, "Cannot disable animations due to lack of permission.");
        }
    }

    private boolean reflectivelyDisableAnimation() {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales",
                    float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                Log.d(TAG,"currentScales = "  + currentScales[i]);
                currentScales[i] = 1.0f;
            }
//            setAnimationScales.invoke(windowManagerObj, currentScales);
            return true;
        } catch (ClassNotFoundException cnfe) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        } catch (NoSuchMethodException mnfe) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        } catch (SecurityException se) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        } catch (InvocationTargetException ite) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        } catch (IllegalAccessException iae) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        } catch (RuntimeException re) {
            Log.w(TAG, "Cannot disable animations reflectively.");
        }
        return false;
    }
}
