package com.example.alex.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class PuzzlePiece {
	private static final String TAG = "PuzzlePiece";
	private Bitmap mBitmap;
	private float mPositionX;
	private float mPositionY;
	private Paint mPaint;

	public PuzzlePiece() {
		init();
	}

	public PuzzlePiece(Bitmap bitmap) {
		mBitmap = bitmap;
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

	}

	public void moveTo(float x, float y) {
		mPositionX = x;
		mPositionY = y;
	}
	
	public float getX() {
		return mPositionX;
	}
	
	public float getY() {
		return mPositionY;
	}
	
	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void drawPiece(SurfaceHolder holder) {
		if (holder != null) {
			Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				Log.d(TAG, "drawbitmap: x = " + mPositionX + ", y = " + mPositionY);
				canvas.drawBitmap(mBitmap, mPositionX, mPositionY, mPaint);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

}
