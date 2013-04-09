package com.example.alex.db.profilesprovider;

import com.example.alex.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LoaderTest extends Activity{
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		
	}
}
