package com.example.alex.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.alex.R;

public class SimpleWidgetConfigActivity extends Activity {

	private int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getApplicationContext());
		RemoteViews views = new RemoteViews(this.getPackageName(),
				R.layout.simple_appwidget);
		views.setTextViewText(R.id.tv_simple_widget, "Hello widget~");
		appWidgetManager.updateAppWidget(mAppWidgetId, views);

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
}
