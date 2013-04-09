package com.example.alex.media;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FilePickerActivity extends Activity {
	private static String TAG = "FilePickerActivity";
	private TextView mTextView;
	private static final int REQUET_GET_FILE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTextView = new TextView(this);
		setContentView(mTextView);

		Intent intent = new Intent("feiyi.ext.fileselector");
		intent.putExtra("SELECTION_MODE", 1);
		intent.putExtra("START_PATH", "/mnt/");

		try {
			startActivityForResult(intent, REQUET_GET_FILE);
		} catch (ActivityNotFoundException e) {
			mTextView.setText("Get file fail. File Picker is not exist.");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUET_GET_FILE) {
			if (data.getData() == null) {
				mTextView.setText("Get file fail. get result fail. data is null");
				Log.e(TAG, "on activity result, get result fail. data is null");
				return;
			}
			String filePath = data.getData().toString();
			if (filePath != null) {
				mTextView.setText("Got file: " + filePath + "\n");
				mTextView.append(InsertAudioIntoPlaylist.getAudioId(filePath, this));
			} else {
				mTextView.setText("Get file fail. Picked file is null.");
			}
		}
	}

	private void getAudioFile() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		startActivity(Intent.createChooser(intent, "Select Music"));
	}

}
