package com.example.alex.activity;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

public class HandlerTest extends Activity {
	private static final String TAG = HandlerTest.class.getSimpleName();
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d(TAG, "handleMessage");
			mHandler.sendEmptyMessageDelayed(1, 200);
		};
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler.sendEmptyMessage(1);
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mHandler.removeMessages(1);
	}
}
