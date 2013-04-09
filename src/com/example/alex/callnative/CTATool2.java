package com.example.alex.callnative;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import android.util.Log;

public class CTATool2 {
	private static final String TAG = "CTATool2";
	Process mProcess;

	public synchronized void excuteTool() {
		Log.d(TAG, "start excute");

		try {
			mProcess = Runtime.getRuntime().exec("labtool");
			getResult();
			getError();
			Log.d(TAG, "excuting");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void getResult() {
		if (mProcess == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				InputStream in = mProcess.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				try {
					int ch = 0;
					while ((ch = reader.read()) != -1) {
						System.out.print((char) (ch));
					}

					System.out.println("Process end.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void getError() {
		if (mProcess == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				InputStream in = mProcess.getErrorStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;

				try {
					while ((line = reader.readLine()) != null) {
						System.err.println(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void sendCommand(String cmd) {
		if (mProcess == null) {
			return;
		}
		Log.d(TAG, "send command: " + cmd);
		OutputStream out = mProcess.getOutputStream();
		PrintWriter writer = new PrintWriter(out);
		writer.write(cmd);
		writer.flush();
	}

	public int exitProcess() {
		if (mProcess == null) {
			return -1;
		}
		int exitValue = -1;
		try {
			exitValue = mProcess.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exitValue;
	}
}
