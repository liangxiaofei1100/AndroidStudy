package com.example.alex.locale;

import com.example.alex.R;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LocaleListenerDemo extends Activity {
	private static final String TAG = "LocaleListenerDemo";
	private static TextView mTextView;
	private Button mButton;
	private static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "oncreate");
		setContentView(R.layout.show_result);
		mContext = this;
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		mButton = (Button) findViewById(R.id.btn_show_result);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		IntentFilter localeFilter = new IntentFilter();
		localeFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
		registerReceiver(mLocaleReceiver, localeFilter);

		IntentFilter updateContentFilter = new IntentFilter(LocaleResourceUpdater.ACTION_UPDATE_CONTENT_RESULT);
		registerReceiver(mUpdateContentReceiver, updateContentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "ondestroy");
		unregisterReceiverSafe(mLocaleReceiver);
		unregisterReceiverSafe(mUpdateContentReceiver);
	}

	private void unregisterReceiverSafe(BroadcastReceiver receiver) {
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	private BroadcastReceiver mUpdateContentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (LocaleResourceUpdater.ACTION_UPDATE_CONTENT_RESULT.equals(intent
					.getAction())) {
				String content = intent
						.getStringExtra(LocaleResourceUpdater.EXTRA_UPDATE_CONTENT_TEXT);
				int content_res = intent.getIntExtra(
						LocaleResourceUpdater.EXTRA_UPDATE_CONTENT_RES, 0);
				Log.d(TAG, "onReceive ACTION_UPDATE_CONTENT content = "
						+ content + ", content_res = " + content_res);
				mTextView.setText(content);
			}
		}
	};

	private BroadcastReceiver mLocaleReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
				Log.d(TAG, "locale changed.");
				Intent requestIntent = new Intent(
						LocaleResourceUpdater.ACTION_UPDATE_CONTENT_REQUEST);
				requestIntent.putExtra(
						LocaleResourceUpdater.EXTRA_PACKAGE_NAME,
						context.getPackageName());
				requestIntent.putExtra(
						LocaleResourceUpdater.EXTRA_UPDATE_CONTENT_RES,
						R.string.locale_test);
				sendBroadcast(requestIntent);
			}
		}

	};
}
