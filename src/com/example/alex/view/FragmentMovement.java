package com.example.alex.view;

import com.example.alex.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class FragmentMovement extends Activity {
	private static final String TAG = "FragmentMovement";
	Fragment fragment1, fragment2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_movement);
		FragmentManager fragmentManager = getFragmentManager();
		fragment1 = fragmentManager
				.findFragmentById(R.id.fragment_movement_fragment1);
		fragment2 = fragmentManager
				.findFragmentById(R.id.fragment_movement_fragment2);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.AXIS_HSCROLL:
			Log.d(TAG, "AXIS_HSCROLL");
			return true;
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "action down");
			return true;
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "action move");
			return true;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "action up");
			return true;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
