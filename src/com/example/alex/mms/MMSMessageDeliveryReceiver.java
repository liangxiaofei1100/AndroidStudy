package com.example.alex.mms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MMSMessageDeliveryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(MMSLog.TAG, "delivery receiver onReceive");
		Log.d(MMSLog.TAG, "action: " + intent.getAction());
		int result = getResultCode();
		switch (result) {
		case Activity.RESULT_OK:
			Log.d(MMSLog.TAG, "message delivered sucess");
			Toast.makeText(context, "message delivered sucess",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			Log.d(MMSLog.TAG, "deliver result code: " + result);
			break;
		}
	}
}
