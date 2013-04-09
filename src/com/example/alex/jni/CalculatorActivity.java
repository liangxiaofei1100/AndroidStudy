package com.example.alex.jni;

import android.app.Activity;
import android.widget.TextView;

public class CalculatorActivity extends Activity {

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView mTextView = new TextView(getApplicationContext());
		setContentView(mTextView);
		
	};
}
