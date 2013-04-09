package com.example.alex.activity;

import com.example.alex.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class TranslateBaseActivity extends Activity {
    protected static final String LOG_TAG = "TranslateActivity";
    protected int mID = 0;
    private ActivityManager manager = ActivityManager.getActivityManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager.putActivity(this);
        Log.d(LOG_TAG, "oncreate, id = " + mID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        Log.d(LOG_TAG, "onNewIntent, id = " + mID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeActivity(this);
        Log.d(LOG_TAG, "onDestroy, id = " + mID);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(LOG_TAG, "quit");
            exit();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected void exit() {
        manager.exit();
    }

    protected void startActivity(Context packageContext, Class cls) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

}
