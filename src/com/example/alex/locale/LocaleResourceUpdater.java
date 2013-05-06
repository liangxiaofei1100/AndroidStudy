package com.example.alex.locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Update resource when locale changes.
 * 
 */
public class LocaleResourceUpdater extends BroadcastReceiver {
	private static final String TAG = "LocaleResourceUpdater";

	/** Broadcast action for send resource update request. */
	public static final String ACTION_UPDATE_CONTENT_REQUEST = "com.android.locale.LocaleResourceUpdater.ACTION_UPDATE_CONTENT_REQUEST";

	/** Broadcast action for get resource update result. */
	public static final String ACTION_UPDATE_CONTENT_RESULT = "com.android.locale.LocaleResourceUpdater.ACTION_UPDATE_CONTENT_RESULT";
	/** type String */
	public static final String EXTRA_PACKAGE_NAME = "com.android.locale.LocaleResourceUpdater.EXTRA_PACKAGE_NAME";
	/** type String */
	public static final String EXTRA_UPDATE_CONTENT_TEXT = "com.android.locale.LocaleResourceUpdater.EXTRA_UPDATE_CONTENT_TEXT";
	/** type int */
	public static final String EXTRA_UPDATE_CONTENT_RES = "com.android.locale.LocaleResourceUpdater.EXTRA_UPDATE_CONTENT_RES";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ACTION_UPDATE_CONTENT_REQUEST.equals(intent.getAction())) {

			if (!context.getPackageName().equals(
					intent.getStringExtra(EXTRA_PACKAGE_NAME))) {
				// It is not mine, ignore.
				return;
			}

			int content_res = intent.getIntExtra(EXTRA_UPDATE_CONTENT_RES, 0);
			if (content_res != 0) {
				updateResource(context, content_res);
			} else {
				Log.e(TAG, "onReceive(): content_res = 0.");
			}
		}
	}

	private void updateResource(Context context, int content_res) {
		Intent intent = new Intent(ACTION_UPDATE_CONTENT_RESULT);
		intent.putExtra(EXTRA_UPDATE_CONTENT_TEXT, context.getResources()
				.getString(content_res));
		intent.putExtra(EXTRA_UPDATE_CONTENT_RES, content_res);

		Log.d(TAG, "updateResource(): content = "
				+ context.getResources().getString(content_res)
				+ " content_res = " + content_res);
		context.sendBroadcast(intent);
	}
}
