package com.example.alex.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.alex.R;

public class HtmlView extends Activity {
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		String data = "";
		try {
			data = getData(getAssets().open("e550w.htm"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mWebView.loadDataWithBaseURL("file:///android_asset/e550w.files",
				data, "text/html", "utf-8", null);
	}

	private String getData(InputStream inputStream) {
		char[] data = new char[0];

		try {
			data = new char[inputStream.available()];
			InputStreamReader reader;
			reader = new InputStreamReader(inputStream, "utf-8");
			reader.read(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(data);
	}
}
