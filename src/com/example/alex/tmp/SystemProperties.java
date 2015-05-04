package com.example.alex.tmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.alex.common.SimpleTestActivity;

public class SystemProperties extends SimpleTestActivity {
	private Process mProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInputEditText.setVisibility(View.GONE);
		mShowResultButton.setVisibility(View.GONE);
		getSystemProperties();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mProcess != null) {
			mProcess.destroy();
		}
	}

	public void getSystemProperties() {
		try {
			mProcess = Runtime.getRuntime().exec("getprop");
			InputStream inputStream = mProcess.getInputStream();
			InputStream errorStream = mProcess.getErrorStream();

			readInputStream(inputStream);
			readInputStream(errorStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readInputStream(final InputStream inputStream) {
		Thread readThread = new Thread() {
			@Override
			public void run() {
				StringBuilder stringBuilder = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line = null;
				try {
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						bufferedReader.close();
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// Read finished.
				Message message = mUIHandler.obtainMessage(MSG_SHOW_RESULT);
				message.obj = stringBuilder.toString();
				message.sendToTarget();
			}
		};

		readThread.start();
	}

	private static final int MSG_SHOW_RESULT = 1;
	Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_RESULT:
				String result = msg.obj.toString();
				mResulTextView.append("Result:\n" + result);
				break;

			default:
				break;
			}

		}
	};

}
