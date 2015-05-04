package com.example.alex.tmp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.alex.common.ShowResultActivity;

import android.os.Bundle;
import android.util.Log;

public class Deamon extends ShowResultActivity {
    private static final String TAG = Deamon.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "copy file start");
        copyFileToFiles();
        Log.d(TAG, "copy file end");

        File deamonFile = new File(getFilesDir(), "deamon.sh");
        try {
            Log.d(TAG, "deamon prepare to run");
            Process process = Runtime.getRuntime().exec(
                    "/system/bin/sh " + deamonFile.getAbsolutePath());
            Log.d(TAG, "deamon started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFileToFiles() {
        InputStream inputStream;
        try {
            inputStream = getAssets().open("deamon.sh");
            OutputStream outputStream = new FileOutputStream(new File(
                    getFilesDir(), "deamon.sh"));
            copyFile(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) {
        byte[] buf = new byte[1024];
        int len;

        try {
            while ((len = inputStream.read(buf)) >= 0) {
                outputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
