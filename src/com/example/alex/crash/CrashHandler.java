package com.example.alex.crash;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static CrashHandler mInstance = new CrashHandler();
	private Application mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	private transient Activity lastActivityCreated;

	public static CrashHandler getInstance() {
		return mInstance;
	}

	public void init(Application application) {
		mContext = application;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

		ApplicationHelper.registerActivityLifecycleCallbacks(mContext,
				new ActivityLifecycleCallbacksCompat() {
					@Override
					public void onActivityCreated(Activity activity,
							Bundle savedInstanceState) {
						// Ignore CrashReportDialog because we want the last
						// application Activity that was started so that we can
						// explicitly kill it off.
						lastActivityCreated = activity;
					}

					@Override
					public void onActivityStarted(Activity activity) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onActivityResumed(Activity activity) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onActivityPaused(Activity activity) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onActivityStopped(Activity activity) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onActivitySaveInstanceState(Activity activity,
							Bundle outState) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onActivityDestroyed(Activity activity) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// SharedPreferences sharedPreferences = mContext.getSharedPreferences(
		// "crash", Context.MODE_PRIVATE);
		// Editor editor = sharedPreferences.edit();
		// editor.putBoolean("crash", true);
		// editor.commit();

		if (!handleException(thread, ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			if (lastActivityCreated != null) {
				lastActivityCreated.finish();
			}
			notifyDialog(null);

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);

		}
	}

	/**
	 * Notify user with a dialog the app has crashed, ask permission to send it.
	 * {@link CrashReportDialog} Activity.
	 * 
	 * @param reportFileName
	 *            Name fo the error report to display in the crash report
	 *            dialog.
	 */
	void notifyDialog(String reportFileName) {
		Intent dialogIntent = new Intent(mContext, CrashDialog.class);
		dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(dialogIntent);
	}

	private boolean handleException(Thread thread, Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		Log.e(TAG, writer.toString());

		return true;
	}

}
