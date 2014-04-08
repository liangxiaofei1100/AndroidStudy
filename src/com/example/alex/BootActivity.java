package com.example.alex;

import com.example.alex.crash.CrashDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.BatteryManager;
import android.os.Bundle;

public class BootActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = getSharedPreferences("crash",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		boolean hasCrashed = sharedPreferences.getBoolean("crash", false);
		if (hasCrashed) {
			CrashDialog.show(this);
			editor.putBoolean("crash", false);
			editor.commit();
		}else {
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
}
