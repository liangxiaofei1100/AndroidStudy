package com.example.alex.widget;

import com.example.alex.R;
import com.example.alex.view.ColorAdapter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.StackView;

public class StackViewWidgetProvider extends AppWidgetProvider {

	private int[] mColors = { Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN,
			Color.RED };
	private static final String TAG = "StackViewWidgetProvider";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
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

	private void updateWidget(Context context, int i) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.stack_view_widget);
		PendingIntent pendingIntent;
		Intent intent = new Intent();
		

		ColorAdapter colorAdapter = new ColorAdapter(context, mColors);

	}
}
