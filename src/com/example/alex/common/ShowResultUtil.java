package com.example.alex.common;

import android.widget.TextView;

public class ShowResultUtil {
    private TextView mTextView;

    public ShowResultUtil() {

    }

    public ShowResultUtil(TextView textView) {
        mTextView = textView;
    }

    public void showInTextView(String msg) {
        showInTextView(mTextView, msg);
    }

    public static void showInTextView(TextView textView, String msg) {
        if (textView != null) {
            if (!msg.endsWith("\n")) {
                textView.append(TimeUtil.getCurrentTime() + " " + msg + '\n');
            } else {
                textView.append(TimeUtil.getCurrentTime() + " " + msg);
            }

        }
    }
}
