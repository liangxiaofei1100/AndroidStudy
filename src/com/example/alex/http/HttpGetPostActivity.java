package com.example.alex.http;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.R;

public class HttpGetPostActivity extends Activity {
	TextView mTextView;
	HttpGetPost mHttpGetPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.http_get_post);

		initView();
		mHttpGetPost = new HttpGetPost();
	}

	private void initView() {
		mTextView = (TextView) findViewById(R.id.tv_http_result);

		Button getButton = (Button) findViewById(R.id.btn_http_get);
		getButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				httpGet();
			}
		});

		Button postButton = (Button) findViewById(R.id.btn_http_post);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				httpPost();
			}

		});

	}

	protected void httpPost() {
		mTextView.setText("Http post\n");
		String result = mHttpGetPost.httpPost();
		mTextView.append("post:" + result);
		
		String result2 = mHttpGetPost.httpPost2("1234", "2345");
		mTextView.append("\nHttpPost2" + result2);
	}

	protected void httpGet() {
		mTextView.setText("Http get\n");
		String resultString = mHttpGetPost.httpGet();
		mTextView.append("\nHttpGet1" + resultString);

		String result2 = mHttpGetPost.httpGet2("123", "234");
		mTextView.append("\nHttpGet2" + result2);
	}

}
