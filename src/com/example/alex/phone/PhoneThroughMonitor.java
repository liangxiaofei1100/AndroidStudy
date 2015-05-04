package com.example.alex.phone;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneThroughMonitor implements Runnable {
    private static final String TAG = PhoneThroughMonitor.class.getSimpleName();

    // 振动器
    Vibrator mVibrator;
    // 电话服务
    TelephonyManager telManager;

    public PhoneThroughMonitor(Vibrator mVibrator, TelephonyManager telManager) {
        this.mVibrator = mVibrator;
        this.telManager = telManager;
    }

    @Override
    public void run() {
        // 获取当前话机状态
        int callState = telManager.getCallState();
        Log.i("TestService", "开始.........." + Thread.currentThread().getName());
        // 记录拨号开始时间
        long threadStart = System.currentTimeMillis();
        Process process;
        InputStream inputstream;
        BufferedReader bufferedreader;
        try {
            process = Runtime.getRuntime().exec("logcat -v time -b radio");
            inputstream = process.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(
                    inputstream);
            bufferedreader = new BufferedReader(inputstreamreader);
            String str = "";
            long dialingStart = 0;
            boolean enableVibrator = false;
            boolean isAlert = false;
            while ((str = bufferedreader.readLine()) != null) {
                // 如果话机状态从摘机变为空闲,销毁线程
                if (callState == TelephonyManager.CALL_STATE_OFFHOOK
                        && telManager.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
                    break;
                }
                // 线程运行5分钟自动销毁
                if (System.currentTimeMillis() - threadStart > 300000) {
                    break;
                }
                Log.i("TestService", Thread.currentThread().getName() + ":"
                        + str);
                // 记录GSM状态DIALING
                if (str.contains("GET_CURRENT_CALLS")
                        && str.contains("DIALING")) {
                    // 当DIALING开始并且已经经过ALERTING或者首次DIALING
                    if (!isAlert || dialingStart == 0) {
                        // 记录DIALING状态产生时间
                        dialingStart = System.currentTimeMillis();
                        isAlert = false;
                    }
                    continue;
                }
                if (str.contains("GET_CURRENT_CALLS")
                        && str.contains("ALERTING") && !enableVibrator) {

                    long temp = System.currentTimeMillis() - dialingStart;
                    isAlert = true;
                    // 这个是关键,当第一次DIALING状态的时间,与当前的ALERTING间隔时间在1.5秒以上并且在20秒以内的话
                    // 那么认为下次的ACTIVE状态为通话接通.
                    if (temp > 1500 && temp < 20000) {
                        enableVibrator = true;
                        Log.i("TestService", "间隔时间....." + temp + "....."
                                + Thread.currentThread().getName());
                    }
                    continue;
                }
                if (str.contains("GET_CURRENT_CALLS") && str.contains("ACTIVE")
                        && enableVibrator) {
                    mVibrator.vibrate(100);
                    enableVibrator = false;
                    break;
                }
            }
            Log.i("TestService", "结束.........."
                    + Thread.currentThread().getName());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
