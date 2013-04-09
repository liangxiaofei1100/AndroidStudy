package com.example.alex.intent;

import com.example.alex.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

public class VideoIntent extends Activity{
	private static final int REQUEST_CAPTURE_VIDEO = 1;
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView)findViewById(R.id.tv_show_result);
		
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CAPTURE_VIDEO:
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "data = " + data + ", URi = " + data.getData(), Toast.LENGTH_SHORT).show();
				mTextView.setText(data.toString());
			}
			break;

		default:
			break;
		}
	}
}
