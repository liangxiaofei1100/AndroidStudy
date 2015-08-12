package com.example.alex.tmp;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

public class MeMediaTest extends ShowResultActivity {

    @Override
    protected void onButtonClick() {
        super.onButtonClick();
        startMeMeidaService();
    }

    private void startMeMeidaService() {
        Intent intent = new Intent("com.gavin.memedia.Service");
        try {
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchSetting() {
        Intent intent = new Intent("com.gavin.memedia.Settings");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
