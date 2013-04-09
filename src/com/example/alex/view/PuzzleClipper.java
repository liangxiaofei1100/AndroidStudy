package com.example.alex.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

public class PuzzleClipper {
	private static final String TAG = "PuzzleClipper";
	/**
	 * clip original bitmap to m * n bitmaps.
	 * 
	 * @param bmpOriginal
	 * @param m
	 * @param n
	 * @return
	 */
	public Bitmap[] clip(Bitmap bmpOriginal, int m, int n) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap[] bitmaps = new Bitmap[m * n];
		Paint paint = new Paint();

		int index = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				Bitmap temp = Bitmap.createBitmap(width / n, height / m,
						Bitmap.Config.RGB_565);
				Canvas c = new Canvas(temp);
				c.drawBitmap(bmpOriginal, j * width / n, i * height / m, paint);
				// c.clipRect(j * width / n, i * height / m, (j + 1) * width /
				// n,
				// (j + 1) * height / m);
				bitmaps[index] = temp;
				index ++;
			}
		}

		return bitmaps;
	}
}
