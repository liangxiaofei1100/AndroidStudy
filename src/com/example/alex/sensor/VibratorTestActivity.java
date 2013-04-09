package com.example.alex.sensor;

import com.example.alex.common.HapticFeedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class VibratorTestActivity extends Activity {
	HapticFeedback feedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		feedback = new HapticFeedback();
		feedback.init(this, true);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		feedback.vibrate();
		return super.onTouchEvent(event);
	}

}
