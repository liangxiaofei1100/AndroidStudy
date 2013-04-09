package com.example.alex.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.alex.R;
import com.example.alex.common.TimeUtil;

public class SimpleWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "SimpleWidgetProvider";
	public static final String ACTION_NOTIFY_DATASET_CHANGED = "SimpleWidgetProvider";
	private static PendingIntent mUpdateIntent;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		stopUpdateAlarm(context);
	}

	private void stopUpdateAlarm(Context context) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Log.d(TAG, "cancel alarm: " + mUpdateIntent.toString());
		alarmMgr.cancel(mUpdateIntent);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		mUpdateIntent = setupUpdateAlarm(context);
	}

	private PendingIntent setupUpdateAlarm(Context context) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int requestCode = 0;
		PendingIntent updateIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerAtTime = SystemClock.elapsedRealtime();
		int interval = 1000;
		alarmMgr.setRepeating(AlarmManager.RTC, triggerAtTime, interval,
				mUpdateIntent);
		return updateIntent;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			int[] appWidgetIds = appWidgetManager
					.getAppWidgetIds(new ComponentName(context,
							SimpleWidgetProvider.class));
			if (appWidgetIds.length > 0) {
				appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
						R.id.tv_simple_widget);
			}
			onUpdate(context, appWidgetManager, appWidgetIds);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "update");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int i = 0; i < appWidgetIds.length; i++) {
			updateWidget(context, appWidgetIds[i]);
		}

	}

	private void updateWidget(Context context, int appWidgetId) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.simple_appwidget);
		PendingIntent pendingIntent;

		// Open Simple widget test activity when click the widget.
		final Intent simpleWidgetIntent = new Intent(context,
				SimpleWidgetTestActivity.class);
		pendingIntent = PendingIntent.getActivity(context, 0,
				simpleWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.tv_simple_widget,
				pendingIntent);
		remoteViews.setTextViewText(R.id.tv_simple_widget,
				TimeUtil.getCurrentTime());

		AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId,
				remoteViews);
	}

}
