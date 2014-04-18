package com.example.alex.common;

import com.example.alex.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowResultActivity extends Activity {
	protected TextView mTextView;
	protected Button mButton;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mContext = this;
		initView();
	}

	private void initView() {
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		mButton = (Button) findViewById(R.id.btn_show_result);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				onButtonClick();
			}
		});
	}

	protected void onButtonClick() {

	}
}
