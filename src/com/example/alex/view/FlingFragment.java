package com.example.alex.view;

import com.example.alex.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class FlingFragment extends Fragment implements OnGestureListener {
	private static final String TAG = "FlingFragment";
	private GestureDetector mGestureDetector;
	private OnFlingListener mFlingListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnFlingListener) {
			mFlingListener = (OnFlingListener) activity;
		} else {
			Log.e(TAG, "activity does not impliment OnFlingListener");
			mFlingListener = new OnFlingListener() {
				
				@Override
				public void onFling(float f, float g) {
					// default listener;
				}
			};
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fling_fragment, container, false);
		mGestureDetector = new GestureDetector(getActivity()
				.getApplicationContext(), this);
		return view;
	}

	public GestureDetector getGestureDetector() {
		return mGestureDetector;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG, "onFling");
		mFlingListener.onFling(e1.getX(), e2.getX());
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
