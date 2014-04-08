package com.example.alex.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class VelocityTrackerTest extends Activity{
	private static final String TAG ="VelocityTrackerTest";
	private VelocityTracker mVelocityTracker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = mVelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000);
			float xVelocity = mVelocityTracker.getXVelocity();
			Log.d(TAG, " xVelocity = " + xVelocity);
			mVelocityTracker.recycle();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
