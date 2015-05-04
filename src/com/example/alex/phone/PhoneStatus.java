package com.example.alex.phone;

import com.example.alex.common.ShowResultActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;

public class PhoneStatus extends ShowResultActivity {
	private TelephonyManager mTelephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		showPhoneType();
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		PhoneThroughMonitor monitor = new PhoneThroughMonitor(vibrator, mTelephonyManager);
		new Thread(monitor).start();
	}

	private void showPhoneType() {
		String phoneTypeStr = "unkown";
		int phoneType = mTelephonyManager.getPhoneType();
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			phoneTypeStr = "CDMA";
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			phoneTypeStr = "GSM";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			phoneTypeStr = "NONE";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			phoneTypeStr = "SIP";
			break;

		default:
			break;
		}
		mTextView.append("Phone Type = " + phoneTypeStr);
	}
	
	
	

}
