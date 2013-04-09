package com.example.alex.apn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class APNSetting extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APN_SETTINGS);
		startActivity(intent);
		finish();
		
	}
	
}
