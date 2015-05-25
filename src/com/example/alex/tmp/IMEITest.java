package com.example.alex.tmp;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.alex.common.ShowResultActivity;
import com.example.alex.common.TimeUtil;

public class IMEITest extends ShowResultActivity {

	@Override
	protected void onButtonClick() {
		
		mTextView.setText(System.currentTimeMillis() + " IMEI "
				+ getIMEINo(this));

	}

	/**
	 * 获取设备的IMEI号
	 * 
	 * @param context
	 * @return IMEI号字符串
	 */
	public static String getIMEINo(Context context) {
		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mngr.getDeviceId();
	}
}
