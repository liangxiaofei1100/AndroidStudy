package com.example.alex.mms;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.R;

public class MMSTestActivity extends Activity {
	private static final String TAG = "MMSTestActivity";
	private EditText mMessageEditText;
	private EditText mPhoneNumberEditText;
	private Button mSendButton;
	private SmsManager mSmsManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mms_test);
		initView();

		mSmsManager = SmsManager.getDefault();
	}

	/**
	 * send sms.
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	private void sendSMS(String phoneNumber, String message) {
		if (message != null && message.length() > 0 && phoneNumber != null
				&& phoneNumber.length() > 0) {

			// For the sent result.
			Intent sentIntent = new Intent();
			sentIntent.setClass(this, MMSMessageSentReceiver.class);
			sentIntent.putExtra("number", phoneNumber);
			PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, sentIntent, 0);

			// For the delivery result.
			Intent deliveryIntent = new Intent();
			deliveryIntent.setClass(this, MMSMessageDeliveryReceiver.class);
			PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, deliveryIntent, 0);

			if (message.length() > 70) {
				// divideMessage
				ArrayList<String> texts = mSmsManager.divideMessage(message);
				for (String s : texts) {
					mSmsManager.sendTextMessage(phoneNumber, null, s,
							sentPendingIntent, deliveryPendingIntent);
				}
			} else {
				mSmsManager.sendTextMessage(phoneNumber, null, message,
						sentPendingIntent, deliveryPendingIntent);
			}
		} else {
			Log.w(TAG, "send SMS fail: phone number = " + phoneNumber
					+ ", message = " + message);
		}
	}

	/**
	 * save message to SMS content provider.
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	private void saveToSMSProvider(String phoneNumber, String message) {
		ContentValues values = new ContentValues();
		// send time
		values.put(SMSProvider.SMS.DATE, System.currentTimeMillis());
		// read status
		values.put(SMSProvider.SMS.READ, 0);
		// type 1:receive, 2: send
		values.put(SMSProvider.SMS.TYPE, 2);
		// recipient phone number
		values.put(SMSProvider.SMS.ADDRESS, phoneNumber);
		// sms message.
		values.put(SMSProvider.SMS.BODY, message);
		getContentResolver().insert(SMSProvider.URI_SENT, values);
	}

	private void initView() {
		mMessageEditText = (EditText) findViewById(R.id.edittext_message_mms);
		mPhoneNumberEditText = (EditText) findViewById(R.id.edittext_phone_number_mms);
		mSendButton = (Button) findViewById(R.id.btn_send_mms);
		mSendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNumber = mPhoneNumberEditText.getText().toString();
				String message = mMessageEditText.getText().toString();
				sendSMS(phoneNumber, message);
				saveToSMSProvider(phoneNumber, message);
			}
		});
	}
}
