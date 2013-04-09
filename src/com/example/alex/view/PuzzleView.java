package com.example.alex.view;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.alex.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PuzzleView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "PuzzleView";

	private SurfaceHolder mSurfaceHolder;
	private Context mContext;
	private Paint mPaint;
	private ArrayList<PuzzlePiece> mPuzzlePieces;
	private ArrayList<Bitmap> mBitmaps;
	private PuzzlePiece mSelectedPiece;

	public PuzzleView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;

		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mPuzzlePieces = new ArrayList<PuzzlePiece>();
		mBitmaps = new ArrayList<Bitmap>();
	}

	private void createPieces() {
		PuzzleClipper clipper = new PuzzleClipper();
		Bitmap[] bitmaps = clipper.clip(BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher_web), 2, 2);
		for(Bitmap bitmap : bitmaps) {
			Log.d(TAG, "bitmap: " + bitmap);
			mBitmaps.add(bitmap);
		}
//		
//		mBitmaps.add(BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher));
//		mBitmaps.add(BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher2));
//		mBitmaps.add(BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher3));
//		mBitmaps.add(BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher4));
		int x = 0;
		int y = 0;
		int column = 0;
		for (Bitmap bitmap : mBitmaps) {
			PuzzlePiece piece = new PuzzlePiece(bitmap);
			piece.moveTo(x, y);
			mPuzzlePieces.add(piece);

			if (column == 1) {
				x = 0;
				y += bitmap.getHeight() + 100;
				column = 0;
			} else {
				column++;
				x += bitmap.getWidth() + 100;
			}

		}
	}

	private void drawPieces(SurfaceHolder holder) {
		if (holder != null) {
			Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				for (PuzzlePiece piece : mPuzzlePieces) {
					canvas.drawBitmap(piece.getBitmap(), piece.getX()
							- piece.getBitmap().getWidth() / 2, piece.getY()
							- piece.getBitmap().getHeight() / 2, mPaint);
				}
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mSelectedPiece = getSelectedPiece(event.getX(), event.getY());
			return true;
		case MotionEvent.ACTION_MOVE:
			if (mSelectedPiece != null) {
				mSelectedPiece.moveTo(event.getX(), event.getY());
				drawPieces(mSurfaceHolder);
			}
			return true;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private PuzzlePiece getSelectedPiece(float x, float y) {
		for (PuzzlePiece piece : mPuzzlePieces) {
			if (x >= piece.getX()
					&& x <= piece.getX() + piece.getBitmap().getWidth()
					&& y >= piece.getY()
					&& y <= piece.getY() + piece.getBitmap().getHeight()) {
				return piece;
			}
		}
		return null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surface created");
		createPieces();
		drawPieces(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surface destoryed");
		mSurfaceHolder = null;
	}

}
