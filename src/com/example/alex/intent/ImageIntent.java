package com.example.alex.intent;

import com.example.alex.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ImageIntent extends Activity{
	private static final int REQUEST_GET_IMAGE = 1;
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView)findViewById(R.id.tv_show_result);
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_GET_IMAGE);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_GET_IMAGE:
			if (resultCode == RESULT_OK) {
				mTextView.setText(data.toString());
			}
			break;

		default:
			break;
		}
	}
}
