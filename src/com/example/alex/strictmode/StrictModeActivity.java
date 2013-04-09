package com.example.alex.strictmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

public class StrictModeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build();
		StrictMode.setThreadPolicy(policy);
	}
}
