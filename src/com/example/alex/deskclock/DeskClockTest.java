package com.example.alex.deskclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class DeskClockTest extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTime();
	}
	
	private void setTime(){
		Intent intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
		startActivity(intent1);
	}
}
