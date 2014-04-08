package com.example.alex.activity;

import com.example.alex.R;
import com.example.alex.activity.SlashView.OnSlashQuitListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SlashActivity2 extends Activity implements OnSlashQuitListener{
	SlashView mSlashView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slash_activity2);
		mSlashView = (SlashView) findViewById(R.id.rl_slash_activity_container);
		mSlashView.setOnSlashQuitListener(this);
	}
	@Override
	public void onSlashQuit() {
		Log.d("SlashView", "onSlashQuit");
		finish();
	}
}
