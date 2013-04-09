package com.example.alex.root;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.R;

public class RootTest extends Activity {
	TextView mResultTextView;
	EditText mCommandEditText;
	Button mCommitButton;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.native_communication);
		mContext = this;
		mResultTextView = (TextView) findViewById(R.id.tv_result);
		mCommandEditText = (EditText) findViewById(R.id.etv_command);
		mCommitButton = (Button) findViewById(R.id.btn_commit);
		mCommitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String command = mCommandEditText.getText().toString();
				command = "root \"load_mfg_8787.sh > /tmp/12.txt 2>&1\"";
				String[] cmd = { "sh", "-c", command };
				Process process = null;
				try {
					process = Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}

				String resultString = "";
				resultString += "start excute: " + command + '\n';
				try {
					resultString += loadStream(process.getInputStream());
					resultString += loadStream(process.getErrorStream());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				resultString += "\nexcute done.\n";
				mResultTextView.setText(resultString);
			}
		});

	}

	// read an input-stream into a String
	private static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();

	}
}
