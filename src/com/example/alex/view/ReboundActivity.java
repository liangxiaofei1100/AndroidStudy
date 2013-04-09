package com.example.alex.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ReboundActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableFullScreen();
		enableNoTitle();
		ReboundView reboundView = new ReboundView(this);
		setContentView(reboundView);
	}
	
	private void enableFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void enableNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
