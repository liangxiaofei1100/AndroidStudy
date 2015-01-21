package com.example.alex.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Show how Handler handle message after activity destroy.
 * 
 */
public class HandlerTest extends Activity {
	private static final String TAG = HandlerTest.class.getSimpleName();
	private Toast mToast;
	private Context mContext;
	Handler mHandler = new Handler() {
		int count = 0;

		public void handleMessage(android.os.Message msg) {
			count++;
			Log.d(TAG, "handleMessage, count=" + count);
			showToastAndCancelPrevious("handleMessage, count=" + count);

			mHandler.sendEmptyMessageDelayed(1, 200);
		};
	};

	private void showToastAndCancelPrevious(String message) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
		mToast.show();
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mHandler.sendEmptyMessage(1);
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mHandler.removeMessages(1);
	}
}
