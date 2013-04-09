package com.example.alex.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SurfaceViewTestActivity extends Activity {
	private final static String TAG = "SurfaceViewTestActivity";
	GameView gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableNoTitle();
		enableFullScreen();
		gameView = new GameView(this);
		setContentView(gameView);
	}

	private void enableFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void enableNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(gameView.onTouchEvent(event)) {
			return true;
		}else {
			return false;
		}
	}
}
