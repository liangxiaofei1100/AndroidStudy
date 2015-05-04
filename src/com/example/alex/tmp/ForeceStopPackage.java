package com.example.alex.tmp;

import java.io.IOException;

import android.os.Bundle;
import android.util.Log;

import com.example.alex.common.ShowResultActivity;

public class ForeceStopPackage extends ShowResultActivity {
    private static final String TAG = ForeceStopPackage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kill("com.gavin.memedia");
    }

    /**
     * 结束进程,执行操作调用即可
     */
    public static void kill(String packageName) {
        try {
            Log.d(TAG, "start kill " + packageName);
            Process process = Runtime.getRuntime().exec(
                    "su am force-stop " + packageName);
            process.waitFor();
            process.destroy();
            Log.d(TAG, "success kill " + packageName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
