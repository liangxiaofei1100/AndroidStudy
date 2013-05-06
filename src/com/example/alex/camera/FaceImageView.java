package com.example.alex.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FaceImageView extends ImageView {
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private int mBitmapWidth = 200;
	private int mBitmapHeight = 200;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private int mDisplayStyle = 0;
	public static final int DISPLAY_STYLE_FACE = 0;
	public static final int DISPLAY_STYLE_EYE = 1;

	private int[] mPX = null;
	private int[] mPY = null;

	public FaceImageView(Context c) {
		super(c);
		init();
	}

	public FaceImageView(Context c, AttributeSet attrs) {
		super(c, attrs);
		init();
	}

	private void init() {
		mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Bitmap.Config.RGB_565);
		mCanvas = new Canvas(mBitmap);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(0x80ff0000);
		mPaint.setStrokeWidth(3);
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			mBitmapWidth = bm.getWidth();
			mBitmapHeight = bm.getHeight();

			mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
					Bitmap.Config.RGB_565);
			mCanvas = new Canvas();
			mCanvas.setBitmap(mBitmap);
			mCanvas.drawBitmap(bm, 0, 0, null);
		}

		super.setImageBitmap(bm);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mBitmapWidth = (mBitmap != null) ? mBitmap.getWidth() : 0;
		mBitmapHeight = (mBitmap != null) ? mBitmap.getHeight() : 0;
		if (mBitmapWidth == w && mBitmapHeight == h) {
			return;
		}

		if (mBitmapWidth < w)
			mBitmapWidth = w;
		if (mBitmapHeight < h)
			mBitmapHeight = h;
	}

	// set up detected face features for display
	public void setDisplayPoints(int[] xx, int[] yy, int total, int style) {
		mDisplayStyle = style;
		mPX = null;
		mPY = null;

		if (xx != null && yy != null && total > 0) {
			mPX = new int[total];
			mPY = new int[total];

			for (int i = 0; i < total; i++) {
				mPX[i] = xx[i];
				mPY[i] = yy[i];
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);

			if (mPX != null && mPY != null) {
				for (int i = 0; i < mPX.length; i++) {
					if (mDisplayStyle == DISPLAY_STYLE_EYE) {
						canvas.drawCircle(mPX[i], mPY[i], 10.0f, mPaint);
					} else {
						canvas.drawRect(mPX[i] - 20, mPY[i] - 20, mPX[i] + 20,
								mPY[i] + 20, mPaint);
					}
				}
			}
		}
	}
}
