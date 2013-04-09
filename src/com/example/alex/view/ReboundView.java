package com.example.alex.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.alex.R;
import com.example.alex.common.DisplayUtil;

public class ReboundView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameView";
	SurfaceHolder mSurfaceHolder;
	Paint mPaint;
	Bitmap[] bitmaps = {
			BitmapFactory
					.decodeResource(getResources(), R.drawable.ic_launcher),
			BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher2) };

	private Context mContext;

	private float x;
	private float y;
	private int mScreenWidth;
	private int mScreenHeight;

	private Matrix mMatrix;

	public ReboundView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		// init surface
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		// init matrix
		mMatrix = new Matrix();
		// init paint
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		setFocusable(true);
	}

	public ReboundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surfaceCreated");
		mSurfaceHolder = holder;
		mIsRunning = true;

		mScreenWidth = DisplayUtil.getScreenWidth(mContext);
		mScreenHeight = DisplayUtil.getScreenHeight(mContext);

		DrawThread drawThread = new DrawThread();
		drawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surface destoryed");
		mSurfaceHolder = null;
		mIsRunning = false;
	}

	class DrawThread extends Thread {
		boolean addX = true;
		boolean addY = true;

		@Override
		public void run() {
			while (mIsRunning) {
				drawPictures(x, y);
				if (addX) {
					x += 5;
				} else {
					x -= 5;
				}
				if (addY) {
					y += 5;
				} else {
					y -= 5;
				}
				if (x > mScreenWidth - bitmaps[0].getWidth()) {
					addX = false;
				} else if (x < 0) {
					addX = true;
				}

				if (y > mScreenHeight - bitmaps[0].getHeight()) {
					addY = false;
				} else if (y < 0) {
					addY = true;
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean mIsRunning = false;

	/**
	 * Draw picture
	 * 
	 * @param x
	 * @param y
	 */
	private void drawPictures(float x, float y) {
		if (mSurfaceHolder != null) {
			Canvas canvas = mSurfaceHolder.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(bitmaps[0], x, y, mPaint);
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
