package com.example.alex.game;

import com.example.alex.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ShouJiYanKuai extends Activity {
	private static final String TAG = ShouJiYanKuai.class.getSimpleName();
	private ImageView mImageViews[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoujiyankuai);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		switchView(findViewById(R.id.iv_1), findViewById(R.id.iv_3));
	}

	private void initView() {
		mImageViews = new ImageView[4];
		mImageViews[0] = (ImageView) findViewById(R.id.iv_1);
		mImageViews[1] = (ImageView) findViewById(R.id.iv_2);
		mImageViews[2] = (ImageView) findViewById(R.id.iv_3);
		mImageViews[3] = (ImageView) findViewById(R.id.iv_4);
	}

	private void switchView(View view1, View view2) {
		Log.d(TAG, "View1 " + view1.getWidth() + " View2 " + view2.getWidth());
		int[] location1 = new int[2];
		int[] location2 = new int[2];
		view1.getLocationInWindow(location1);
		view2.getLocationOnScreen(location2);

		Log.d(TAG, "switchView x1=" + location1[0] + ", x2=" + location2[0]
				+ ", y1=" + location1[1] + ",y2=" + location2[1]);
		Animation animation = new TranslateAnimation(0, 0, 0, 1000);
		animation.setDuration(2000);
		view1.startAnimation(animation);
	}
}
