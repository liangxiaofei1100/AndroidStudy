package com.example.alex.mms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MMSMessageSentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(MMSLog.TAG, "sent receiver onReceive");
		String phoneNumber = intent.getStringExtra("number");

		/**
		 * RESULT_ERROR_GENERIC_FAILURE<br/>
		 * RESULT_ERROR_RADIO_OFF <br/>
		 * RESULT_ERROR_NULL_PDU
		 */
		int result = getResultCode();
		switch (result) {
		case Activity.RESULT_OK:
			Toast.makeText(context,
					"message sent success, number: " + phoneNumber,
					Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Toast.makeText(context, "RESULT_ERROR_GENERIC_FAILURE",
					Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Toast.makeText(context, "RESULT_ERROR_NO_SERVICE",
					Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Toast.makeText(context, "RESULT_ERROR_NULL_PDU", Toast.LENGTH_SHORT)
					.show();
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Toast.makeText(context, "RESULT_ERROR_RADIO_OFF",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			Toast.makeText(context, "unkown error", Toast.LENGTH_SHORT).show();
			break;
		}

	}

}
