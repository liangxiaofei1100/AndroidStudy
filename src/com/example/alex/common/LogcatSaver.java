package com.example.alex.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is used for saving android logcat log to file.
 * 
 */
public class LogcatSaver {
	private static final String TAG = "LogcatSaver";
	private boolean mRunning = false;
	private Process mLogcatProc = null;
	private BufferedReader mReader = null;
	private LogFile mLogFile;

	public LogcatSaver(String fileName) {
		mLogFile = new LogFile(fileName);
	}

	public boolean isRunning() {
		return mRunning;
	}

	/**
	 * Start a thread to save log.
	 */
	public void start() {
		if (mRunning) {
			return;
		}
		mRunning = true;
		mLogFile.open();

		SaveThread saveThread = new SaveThread();
		saveThread.start();
	}

	/**
	 * Set running false and save log thread will exit.
	 */
	public void stop() {
		mRunning = false;
	}

	class SaveThread extends Thread {
		@Override
		public void run() {

			try {
				Log.d(TAG, "start run()");
				mLogcatProc = Runtime.getRuntime().exec("logcat -v time -d");
				Log.d(TAG, "start successs.");
				mReader = new BufferedReader(new InputStreamReader(
						mLogcatProc.getInputStream()), 1024);
				String line;
				Log.d(TAG,
						"mRunning = " + mRunning + ", readLine = "
								+ mReader.readLine());
				while (mRunning && (line = mReader.readLine()) != null) {
					if (!mRunning) {
						break;
					}
					if (line.length() == 0) {
						continue;
					}

					mLogFile.writeLog(line + "\n");
				}
			} catch (IOException e) {
				Log.e(TAG, "error reading log" + e);
				return;
			} finally {
				Log.d(TAG, "stopped");

				if (mLogcatProc != null) {
					mLogcatProc.destroy();
					mLogcatProc = null;
				}
				if (mReader != null) {
					try {
						mReader.close();
						mReader = null;
					} catch (IOException e) {
						Log.e(TAG, "error closing stream" + e);
					}
				}
			}
			mLogFile.close();
			mRunning = false;
		}
	}
}
