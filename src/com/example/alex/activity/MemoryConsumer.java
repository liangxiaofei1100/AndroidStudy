package com.example.alex.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.R;

public class MemoryConsumer extends Activity {
	private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		final TextView textView = (TextView) findViewById(R.id.tv_show_result);
		textView.setText("0");
		Button button = (Button) findViewById(R.id.btn_show_result);
		button.setOnClickListener(new OnClickListener() {
			int n = 0;

			@Override
			public void onClick(View v) {
				n++;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.girls_generation);
				mBitmaps.add(bitmap);
				textView.setText(String.valueOf(n));
			}
		});
	}

	@Override
	protected void onDestroy() {
		for (Bitmap bitmap : mBitmaps) {
			bitmap.recycle();
		}
		super.onDestroy();
	}

}
