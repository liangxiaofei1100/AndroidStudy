package com.example.alex.tmp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alex.common.ShowResultActivity;

public class EnableDisabledApp extends ShowResultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    protected void onButtonClick() {
        super.onButtonClick();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zhaoyan.floatwindowdemo", "com.zhaoyan.floatwindowdemo.FloatingService"));
        startService(intent);
        Toast.makeText(this, "launch ", Toast.LENGTH_SHORT).show();
    }
}
