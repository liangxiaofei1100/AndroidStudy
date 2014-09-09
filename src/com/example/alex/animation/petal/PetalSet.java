package com.example.alex.animation.petal;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.alex.R;

public class PetalSet {
	ArrayList<Petal> petalSet;
	private Random mRandom;
	private static final int START_X = -15;
	private static final float START_Y = 0.5f;
	// petal acceleration.
	private static final float ACCELERATION_X_BASE = 0.003f;
	private static final float ACCELERATION_X = 0.008f;
	private static final float ACCELERATION_Y_BASE = 0.003f;
	private static final float ACCELERATION_Y = 0.005f;

	private Bitmap mPetalBitmap;
	private Paint mPaint;

	public PetalSet(Resources res) {
		petalSet = new ArrayList<Petal>();
		mRandom = new Random();
		mPetalBitmap = BitmapFactory.decodeResource(res, R.drawable.petal);
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
	}

	public void add(int count, int maxX, int maxY) {
		for (int i = 0; i < count; i++) {
			Bitmap bitmap = getRandomPetalBitmap();

			int x = START_X;
			int a = mRandom.nextBoolean() ? 1 : -1;
			// y in random rang (-1 * START_Y * maxY, START_Y * maxY)
			int y = (int) (maxY * mRandom.nextFloat() * START_Y) * a;
			int acceleration_x = (int) (maxX * ACCELERATION_X_BASE)
					+ mRandom.nextInt((int) (maxX * ACCELERATION_X));
			int acceleration_y = (int) (maxY * ACCELERATION_Y_BASE)
					+ mRandom.nextInt((int) (maxY * ACCELERATION_Y));
			// alpha in rang (0x88, 0xff)
			int imageAlpha = Math.min(0xff, (0x88 + mRandom.nextInt(0xff)));
			Petal petal = new Petal(bitmap, imageAlpha, x, y, acceleration_x,
					acceleration_y);
			petalSet.add(petal);
		}
	}

	public Bitmap getRandomPetalBitmap() {
		Matrix matrix = new Matrix();
		// scale
		float scale = 0.1f * (5 + mRandom.nextInt(5));
		scale = Math.min(1.5f, scale);
		matrix.postScale(scale, scale);
		// rotate
		float degrees = mRandom.nextInt(360);
		matrix.postRotate(degrees, mPetalBitmap.getWidth() / 2,
				mPetalBitmap.getHeight() / 2);

		Bitmap bitmap = mPetalBitmap;
		try {
			bitmap = Bitmap.createBitmap(mPetalBitmap, 0, 0,
					mPetalBitmap.getWidth(), mPetalBitmap.getHeight(), matrix,
					true);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
