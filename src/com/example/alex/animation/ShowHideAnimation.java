package com.example.alex.animation;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.R;

/**
 * Failed.
 * 
 */
public class ShowHideAnimation extends Activity {
	private ViewGroup mContentView;
	private View mView;
	private LayoutTransition mLayoutTransition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_hide_animation);
		initView();
		initAnimation();
	}

	private void initAnimation() {
		mLayoutTransition = new LayoutTransition();
		mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 2000);
		mLayoutTransition
				.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 2000);
		mLayoutTransition.setDuration(5000);
		mContentView.setLayoutTransition(mLayoutTransition);

		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);

		// Changing while Adding
		ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(this,
				pvhBottom, pvhTop);
		changeIn.setDuration(mLayoutTransition
				.getDuration(LayoutTransition.CHANGE_APPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,
				changeIn);

		// Changing while Removing
		ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(this,
				pvhBottom, pvhTop);
		changeIn.setDuration(mLayoutTransition
				.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				changeOut);

		// Adding
		ObjectAnimator in = ObjectAnimator.ofFloat(null, "scrollY", 0, 90);
		mLayoutTransition.setAnimator(LayoutTransition.APPEARING, in);
		changeIn.setDuration(mLayoutTransition
				.getDuration(LayoutTransition.CHANGE_APPEARING));
		// Removing
		ObjectAnimator out = ObjectAnimator.ofFloat(null, "scrollY", 90, 0);
		mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, out);
		changeOut.setDuration(mLayoutTransition
				.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
	}

	private void initView() {
		mContentView = (ViewGroup) findViewById(R.id.content);
		mView = findViewById(R.id.ll_content);

	}

	public void showHide(View view) {
		if (mView.getVisibility() == View.VISIBLE) {
			mView.setVisibility(View.GONE);
		} else {
			mView.setVisibility(View.VISIBLE);
		}
	}
}
