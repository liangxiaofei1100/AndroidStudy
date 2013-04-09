package com.example.alex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LaunchBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setClass(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		
	}

}
