package com.example.alex.view;

import com.example.alex.R;
import com.example.alex.common.HapticFeedback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "GameView";
	SurfaceHolder mSurfaceHolder;
	Paint mPaint;
	Bitmap[] bitmaps = {
			BitmapFactory
					.decodeResource(getResources(), R.drawable.ic_launcher),
			BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher2) };
	private boolean mIsClicked = false;

	private Context mContext;

	private float x;
	private float y;

	private Matrix mMatrix;
	HapticFeedback hapticFeedback;

	public GameView(Context context) {
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
		// init hapti feedback
		hapticFeedback = new HapticFeedback();
		hapticFeedback.init(context, true);
	}

	public GameView(Context context, AttributeSet attrs) {
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

		drawPictures();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surface destoryed");
		mSurfaceHolder = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsClicked = isClicked(event.getX(), event.getY());
			if (mIsClicked) {
				hapticFeedback.vibrate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsClicked) {
				x = event.getX() - bitmaps[0].getWidth() / 2;
				y = event.getY() - bitmaps[0].getHeight() / 2;
				drawPicturesScaled(1.5f);
			}

			break;
		case MotionEvent.ACTION_UP:
			mIsClicked = false;
			drawPictures();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * check whether the bitmap is checked or not.
	 * 
	 * @param touchX
	 * @param touchY
	 * @return
	 */
	private boolean isClicked(float touchX, float touchY) {
		if (touchX >= x && touchX <= x + bitmaps[0].getWidth() && touchY >= y
				&& y <= y + bitmaps[0].getHeight()) {
			return true;
		}
		return false;
	}

	private void drawPictures() {
		drawPictures(x, y);
	}

	/**
	 * Draw picture
	 * 
	 * @param x
	 * @param y
	 */
	private void drawPictures(float x, float y) {
		Canvas canvas = mSurfaceHolder.lockCanvas();
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bitmaps[0], x, y, mPaint);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	/**
	 * Draw scaled the picture.
	 * 
	 * @param scale
	 *            the scale number.
	 */
	private void drawPicturesScaled(float scale) {
		mMatrix.setScale(scale, scale);
		Canvas canvas = mSurfaceHolder.lockCanvas();
		canvas.drawColor(Color.BLACK);
		canvas.translate(x, y);
		canvas.drawBitmap(bitmaps[0], mMatrix, mPaint);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

}
