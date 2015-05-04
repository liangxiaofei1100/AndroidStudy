package com.example.alex.common;

import com.example.alex.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class SimpleTestActivity extends Activity {
	protected EditText mInputEditText;
	protected TextView mResulTextView;
	protected Button mShowResultButton;
	protected Context mContext;
	protected ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_test);

		mInputEditText = (EditText) findViewById(R.id.et_input);
		mResulTextView = (TextView) findViewById(R.id.tv_result);
		mResulTextView.setText("");
		mShowResultButton = (Button) findViewById(R.id.btn_show_result);
		mScrollView = (ScrollView) findViewById(R.id.sv_result);
		mContext = this;
	}

	/**
	 * mShowResultButton on click listener.
	 * 
	 * @param view
	 */
	public void showResult(View view) {

	}

	public void scrollToBottom() {
		mHandler.sendEmptyMessage(MSG_SCROLL_TO_BOTTOM);
	}

	protected static final int MSG_SCROLL_TO_BOTTOM = 100;

	protected Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SCROLL_TO_BOTTOM:
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
				break;

			default:
				break;
			}

		}
	};
}
