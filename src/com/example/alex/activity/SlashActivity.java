package com.example.alex.activity;

import com.example.alex.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

public class SlashActivity extends Activity implements OnGestureListener,
		AnimationListener {
	private static final String TAG = "SlashActivity";
	private View mContainerView;
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slash_activity);
		mContainerView = findViewById(R.id.rl_slash_activity_container);
		mGestureDetector = new GestureDetector(this, this);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void scrollTo(float x) {
		float destinationX = mContainerView.getX() - x;
		if (destinationX < 0) {
			if (Math.abs(mContainerView.getX()) > 0.00001) {
				mContainerView.setX(0);
			}

		} else {
			mContainerView.setX(destinationX);
		}

	}

	private void showQuitAnimation() {
		Log.d("showQuitAnimation", "start = " + mContainerView.getX()
				+ ", end = " + mContainerView.getWidth());
		TranslateAnimation animation = new TranslateAnimation(0, 1080, 0, 0);
		animation.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setDuration(800);
		animation.setAnimationListener(this);
		animation.setFillAfter(true);
		mContainerView.startAnimation(animation);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d(TAG, "onDown");

		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG, "onFling");
		if (velocityX > 0 && Math.abs(velocityY) < 10f) {
			
			showQuitAnimation();
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d(TAG, "onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d(TAG, "onScroll");
		scrollTo(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d(TAG, "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		finish();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
