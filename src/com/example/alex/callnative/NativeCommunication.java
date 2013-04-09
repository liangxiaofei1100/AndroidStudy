package com.example.alex.callnative;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.R;

public class NativeCommunication extends Activity {
	private final static String TAG = "NativeCommunication";
	TextView mResultTextView;
	EditText mCommandEditText;
	Button mCommitButton;
	Context mContext;
	private BufferedOutputStream mBufferedOutputStream;
	private BufferedReader mBufferedReader;
	Process mProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.native_communication);
		
		
		mContext = this;
		mResultTextView = (TextView) findViewById(R.id.tv_result);
		mCommandEditText = (EditText) findViewById(R.id.etv_command);
		mCommitButton = (Button) findViewById(R.id.btn_commit);
		File file = null;;
		try {
			file = File.createTempFile("CallRecord", ".amr", Environment.getExternalStorageDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mResultTextView.setText(file.getAbsolutePath());
		// try {
		// String[] cmd = { "sh", "-c", "su /system/bin/load_mfg_8787.sh" };
		// Process process = Runtime.getRuntime().exec(cmd);
		// mResultTextView.setText("load success!");
		// mResultTextView.setText(loadStream(process.getInputStream()));
		// process.waitFor();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// thread.start();
		CTATool2 ctaTool2 = new CTATool2();
		ctaTool2.excuteTool();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ctaTool2.sendCommand("99\n");
		ctaTool2.exitProcess();
	}

	Thread readThread = new Thread() {
		public void run() {
			InputStream inputStream = mProcess.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			int ch = 0;
			try {
				Log.d(TAG, "begin read.");
				while ((ch = reader.read()) != -1) {
					Log.d(TAG, String.valueOf(ch));
				}
				Log.d(TAG, "Process ended.");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}

		};
	};

	Thread errorThread = new Thread() {
		public void run() {
			InputStream inputStream = mProcess.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					Log.d(TAG, line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		};
	};

	private void startTest(String configFilePath) {
		Log.d(TAG, "start excute: " + configFilePath);
		String[] cmd = { "sh", "-c",
				"root cat " + configFilePath + " | labtool" };
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			Log.d(TAG, "excute completed");
			mResultTextView.setText(loadStream(ps.getInputStream()));
			mResultTextView.append(loadStream(ps.getErrorStream()));
			ps.waitFor();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// read an input-stream into a String
	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();

	}

	private String createConfigFile() {
		File file = new File(getFilesDir().getAbsolutePath() + "/config.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			CTAWiFiSetting setting = new CTAWiFiSetting(file);
			setting.setInitialPower(1, 12, 1);
			setting.setNMode(0);
			setting.setDutyCycleAndPacketMode(13);
			setting.setCompleted();
			// try {
			// BufferedWriter bufferedWriter = new BufferedWriter(
			// new FileWriter(file));
			// bufferedWriter.write("1" + '\n');
			// bufferedWriter.write("22 1 12 1" + '\n');
			// bufferedWriter.write("99\n99\n");
			// bufferedWriter.flush();
			// bufferedWriter.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		Log.d(TAG, "create file completed: " + file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	private boolean sendCommand(String cmd) {
		Log.d(TAG, "send command: " + cmd);
		if (mProcess == null) {
			Log.e(TAG, "send Command fail.");
			return false;
		}
		OutputStream outputStream = mProcess.getOutputStream();
		PrintWriter writer = new PrintWriter(outputStream);
		writer.println(cmd);
		writer.println("99");
		writer.println("99");
		writer.flush();
		return true;
	}

	private String getRespond() {
		Log.d(TAG, "getRespond.");
		if (mBufferedReader == null) {
			Log.e(TAG, "get respond fail");
			return null;
		}

		String line;
		int num = 0;
		StringBuffer buffer = new StringBuffer();
		try {
			while (num < 10 && (line = mBufferedReader.readLine()) != null) {
				Log.d(TAG, line);
				buffer.append(line + '\n');
				num++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private void stop() {

		try {
			mBufferedReader.close();
			mBufferedOutputStream.close();
			mProcess.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private String executeCommand1(String cmd) {
		if (cmd == null) {
			return "";
		}
		String[] cmds = { "sh", "-c", cmd };
		try {
			mProcess = Runtime.getRuntime().exec(cmds);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
