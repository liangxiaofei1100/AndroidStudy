package com.example.alex.mms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage[] smsMessages = new SmsMessage[messages.length];
		for (int i = 0; i < smsMessages.length; i++) {
			smsMessages[i] = SmsMessage.createFromPdu((byte[]) messages[i]);

			Toast.makeText(
					context,
					"Recived message: " + smsMessages[i].getMessageBody()
							+ ", From: "
							+ smsMessages[i].getDisplayOriginatingAddress(),
					Toast.LENGTH_SHORT).show();
		}

	}
}
