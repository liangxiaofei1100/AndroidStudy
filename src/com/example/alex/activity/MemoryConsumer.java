package com.example.alex.activity;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.example.alex.R;
import com.example.alex.common.SimpleTestActivity;

public class MemoryConsumer extends SimpleTestActivity {
	private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
	private int mCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResulTextView.setText("Count=" + mCount);
	}

	@Override
	public void showResult(View view) {
		super.showResult(view);
		mCount++;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.girls_generation);
		mBitmaps.add(bitmap);

		mResulTextView.setText("Count=" + mCount);
	}

	@Override
	protected void onDestroy() {
		for (Bitmap bitmap : mBitmaps) {
			bitmap.recycle();
		}
		super.onDestroy();
	}

}
