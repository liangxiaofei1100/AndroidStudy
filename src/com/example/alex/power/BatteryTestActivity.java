package com.example.alex.power;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BatteryTestActivity extends Activity {
	IntentFilter intentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
	}

	protected void onResume() {
		registerReceiver(mBatteryStatusReciever, intentFilter);

	};

	protected void onPause() {
		unregisterReceiver(mBatteryStatusReciever);
	};

	BroadcastReceiver mBatteryStatusReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {

			}
		}

	};

}
