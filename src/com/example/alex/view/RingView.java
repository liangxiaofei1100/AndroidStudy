package com.example.alex.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.example.alex.R;

public class RingView extends View {
	private Paint mPaint;
	private static int STROKE_WIDTH;
	private Shader mShader;
	private float cx;
	private float cy;
	private float r;
	private Bitmap mBitmap;

	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RingView(Context context) {
		super(context);
		init();
	}

	public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		STROKE_WIDTH = ViewUtil.px2dp(getContext(), 10);
		mPaint = new Paint();
		mPaint.setColor(Color.argb(0xff, 0xff, 0xff, 0x00));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(STROKE_WIDTH);
		mPaint.setDither(true);

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int width = getWidth();
		int height = getHeight();
		cx = (float) width / 2;
		cy = (float) height / 2;

		mShader = new SweepGradient(cx, cy, Color.argb(0xff, 0xff, 0xff, 0x00),
				Color.argb(0xff, 0x00, 0xff, 0x00));
		mPaint.setShader(mShader);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		cx = (float) width / 2;
		cy = (float) height / 2;
		if (width < height) {
			r = (float) width / 2 - STROKE_WIDTH / 2;
		} else {
			r = (float) height / 2 - STROKE_WIDTH / 2;
		}
		Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		RectF rectF = new RectF(cx - r, cy - r, cx + r, cy + r);

		canvas.drawBitmap(mBitmap, rect, rectF, mPaint);

		canvas.drawCircle(cx, cy, r, mPaint);
	}
}
