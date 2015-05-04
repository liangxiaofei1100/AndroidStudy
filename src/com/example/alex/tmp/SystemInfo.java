package com.example.alex.tmp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.alex.common.SimpleTestActivity;

public class SystemInfo extends SimpleTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShowResultButton.setVisibility(View.GONE);
        mInputEditText.setVisibility(View.GONE);

        mResulTextView.setText("CPU info:\n");
        mResulTextView.append(getCpuInfo());
        mResulTextView.append("\n");

        mResulTextView.append("Board: " + getBordInfo() + "\n");
        mResulTextView.append("Model: " + getModel() + ", size= " + getModel().length()+"\n");
        mResulTextView.append("Device: " + getDevice() + "\n");
        mResulTextView.append("Brand: " + Build.BRAND + "\n");
        mResulTextView.append("Display: " + Build.DISPLAY + "\n");
        mResulTextView.append("Hardware: " + Build.HARDWARE + "\n");
        mResulTextView.append("Manufacturer: " + Build.MANUFACTURER + "\n");
        mResulTextView.append("Product: " + Build.PRODUCT + "\n");
        mResulTextView.append("Serial: " + Build.SERIAL + "\n");
        mResulTextView.append("Type: " + Build.TYPE + "\n");
        mResulTextView.append("VERSION.CODENAME: " + Build.VERSION.CODENAME
                + "\n");
        mResulTextView.append("VERSION.RELEASE: " + Build.VERSION.RELEASE
                + "\n");
        float density = getResources().getDisplayMetrics().density;
        mResulTextView.append("screen density"
                + getResources().getDisplayMetrics().density + "\n");
        mResulTextView.append("screen width"
                + getResources().getDisplayMetrics().widthPixels + "\n");
        mResulTextView.append("screen height"
                + getResources().getDisplayMetrics().heightPixels + "\n");

        float screenWithInch = getResources().getDisplayMetrics().widthPixels
                / (density * 160);
        float screenHeightInch = getResources().getDisplayMetrics().heightPixels
                / (density * 160);
        mResulTextView.append("screen width inch" + screenWithInch + "\n");
        mResulTextView.append("screen height inch" + screenHeightInch + "\n");

        float screenInch = (float) Math.sqrt(screenWithInch * screenWithInch
                + screenHeightInch * screenHeightInch);
        mResulTextView.append("screen inch" + screenInch + "\n");
    }

    public static String getDevice() {
        return android.os.Build.DEVICE;
    }

    public static String getBordInfo() {
        String bordInfo = android.os.Build.BOARD;
        return bordInfo;
    }

    public static String getModel() {
        return android.os.Build.MODEL;
    }

    public static String getCpuInfo() {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder cpuInfo = new StringBuilder();
            FileReader fr = new FileReader("/proc/cpuinfo");
            bufferedReader = new BufferedReader(fr);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                cpuInfo.append(line + "\n");
            }

            return cpuInfo.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
