package com.example.alex.cts;

import java.io.IOException;
import java.io.InputStream;

import com.example.alex.R;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

public class ContextWrapperTest extends Activity {
	private static final String TAG = ContextWrapperTest.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			testAccessWallpaper();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "IOException");
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.e(TAG, "InterruptedException");
		}
	}

	public void testAccessWallpaper() throws IOException, InterruptedException {
		ContextWrapper contextWrapper = new ContextWrapper(this);
		BitmapDrawable originalWallpaper = (BitmapDrawable) contextWrapper
				.getWallpaper();

		Bitmap bitmap = Bitmap.createBitmap(20, 30, Bitmap.Config.RGB_565);
		Drawable testDrawable = contextWrapper.getWallpaper();
		Drawable testDrawable2 = contextWrapper.peekWallpaper();

		contextWrapper.setWallpaper(bitmap);
		synchronized (this) {
			wait(500);
		}

		Log.d(TAG, "1" + !testDrawable.equals(contextWrapper.peekWallpaper()));
		Log.d(TAG, "2" + (contextWrapper.getWallpaper() != null));
		Log.d(TAG, "3" + !testDrawable2.equals(contextWrapper.peekWallpaper()));
		Log.d(TAG, "4" + (contextWrapper.peekWallpaper() != null));

		contextWrapper.clearWallpaper();
		testDrawable = contextWrapper.getWallpaper();
		InputStream stream = contextWrapper.getResources().openRawResource(
				R.drawable.lena);
		contextWrapper.setWallpaper(stream);

		synchronized (this) {
			wait(1000);
		}

		Log.d(TAG, "5" + !testDrawable.equals(contextWrapper.peekWallpaper()));

		contextWrapper.setWallpaper(originalWallpaper.getBitmap());
		Log.d(TAG, "Pass");
	}
}
