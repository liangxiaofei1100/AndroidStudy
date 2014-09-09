package com.example.alex.animation.petal;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PetalView extends SurfaceView implements SurfaceHolder.Callback {
	public static final int DIE_OUT_LINE = 800;
	PetalSet mPetalSet;
	PetalThread mPetalThread;
	DrawThread mDrawThread;
	int x1;
	private Paint mPaint;
	private Paint mClearPaint;

	public PetalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PetalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PetalView(Context context) {
		super(context);
		init();
	}

	private void init() {
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setZOrderOnTop(true);
		getHolder().addCallback(this);
		
		mPetalSet = new PetalSet(getResources());
		mPaint = new Paint();
		mClearPaint = new Paint();
		mClearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	}

	/**
	 * Draw all petals.
	 * 
	 * @param canvas
	 */
	public void doDraw(Canvas canvas) {
		clear(canvas);

		ArrayList<Petal> petalSet = mPetalSet.petalSet;

		for (int i = 0; i < petalSet.size(); i++) {
			Petal petal = petalSet.get(i);
			mPaint.setAlpha(petal.imageAlpha);
			canvas.drawBitmap(petal.image, petal.x, petal.y, mPaint);
		}
	}

	private void clear(Canvas canvas) {
		canvas.drawPaint(mClearPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mPetalThread = new PetalThread(this);
		mDrawThread = new DrawThread(this, getHolder());
		if (mPetalThread != null && !mPetalThread.isAlive()) {
			mPetalThread.start();
		}
		if (mDrawThread != null && !mDrawThread.isAlive()) {
			mDrawThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mDrawThread.stopDraw();
		mDrawThread = null;
		mPetalThread.quit();
		mPetalThread = null;
	}
}