package com.example.alex.sdcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.R;

/**
 * From android 4.4, READ_EXTERNAL_STORAGE permission is added. If this
 * permission is not declared, application can not read sdcard.
 * 
 * 
 */
public class SDCardReadPermissionTest extends Activity {
	private TextView mResultTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdcard_permission);
		initView();
	}

	private void initView() {
		Button readButton = (Button) findViewById(R.id.btn_sd_read);
		readButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				readTest();
			}

		});

		Button writeButton = (Button) findViewById(R.id.btn_sd_write);
		writeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				writeTest();
			}

		});

		mResultTextView = (TextView) findViewById(R.id.tv_sd_result);
	}

	private void readTest() {
		File file = new File("/mnt/sdcard2/", "test.txt");
		if (file.exists()) {
			String result = null;
			try {
				result = readFile(file);
				mResultTextView.setText("readTest: read success. result = "
						+ result);
			} catch (IOException e) {
				e.printStackTrace();
				mResultTextView.setText("readTest: read fail. " + e);
			}
		} else {
			mResultTextView.setText("readTest: file is not exist.");
		}
	}

	private String readFile(File file) throws IOException {
		StringBuffer result = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			result.append(s);
			result.append("\n");
		}
		bufferedReader.close();
		return result.toString();
	}

	private void writeTest() {
		File file = new File("/mnt/sdcard2/", "test.txt");

		try {
			writeFile(file);
			String result = readFile(file);
			mResultTextView.setText("writeTest: write sucess, result = "
					+ result);
		} catch (IOException e) {
			e.printStackTrace();
			mResultTextView.setText("writeTest: write fail. " + e);
		}
	}

	private void writeFile(File file) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("123");
		fileWriter.flush();
		fileWriter.close();
	}
}
