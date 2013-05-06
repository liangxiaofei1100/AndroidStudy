package com.example.alex.camera;

import com.example.alex.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class FaceDetectDemo3 extends Activity {
	private ImageView mFaceImageView;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	private static final int MAX_FACES = 10;
	private static final String TAG = "FaceDetectDemo3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_dectect_demo3_activity);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		detectFace();
	}

	private void initView() {
		mFaceImageView = (ImageView) findViewById(R.id.iv_fd3_picture);

		// load the photo
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.girls_generation);
		mFaceBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
		bitmap.recycle();

		mFaceWidth = mFaceBitmap.getWidth();
		mFaceHeight = mFaceBitmap.getHeight();

		mFaceImageView.setImageBitmap(mFaceBitmap);
	}

	/**
	 * If image width is odd, sub one pixel. Because FaceDetector can only
	 * process image with width even.
	 * "public FaceDetector (int width, int height, int maxFaces) Note that the width of the image must be even."
	 */
	private void checkBitmapWidth() {
		mFaceWidth = mFaceWidth & ~1;
		mFaceBitmap = Bitmap.createBitmap(mFaceBitmap, 0, 0, mFaceWidth,
				mFaceHeight);
	}

	private void detectFace() {
		Thread thread = new Thread() {
			public void run() {
				int cout = setFace();
				mHandler.sendMessage(mHandler.obtainMessage(MSG_INVILIDATE));
				Message countMessage = mHandler
						.obtainMessage(MSG_DETECT_FACE_FINISH);
				countMessage.arg1 = cout;
				mHandler.sendMessage(countMessage);
			}
		};

		thread.start();
	}

	private static final int MSG_INVILIDATE = 0;
	private static final int MSG_DETECT_FACE_FINISH = 1;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INVILIDATE:
				mFaceImageView.invalidate();
				break;
			case MSG_DETECT_FACE_FINISH:
				Toast.makeText(FaceDetectDemo3.this,
						"Dected " + msg.arg1 + " face.", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	public int setFace() {
		checkBitmapWidth();

		FaceDetector fd;
		FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
		PointF midpoint = new PointF();
		int[] fpx = null;
		int[] fpy = null;
		int count = 0;

		try {
			fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);
			count = fd.findFaces(mFaceBitmap, faces);
		} catch (Exception e) {
			Log.e(TAG, "setFace(): " + e.toString());
			return 0;
		}

		Log.d(TAG, "setFace():Dectected face: " + count);
		// check if we detect any faces
		if (count > 0) {

			fpx = new int[count];
			fpy = new int[count];

			for (int i = 0; i < count; i++) {
				try {
					faces[i].getMidPoint(midpoint);

					fpx[i] = (int) midpoint.x;
					fpy[i] = (int) midpoint.y;

					Log.d(TAG, "setFace(): x =" + fpx[i]);
					Log.d(TAG, "setFace(): y =" + fpy[i]);
				} catch (Exception e) {
					Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
				}
			}
		}

		Canvas canvas = new Canvas(mFaceBitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setColor(0x80ff0000);
		paint.setStrokeWidth(3);

		if (fpx != null && fpy != null) {
			for (int i = 0; i < fpx.length; i++) {
				canvas.drawRect(fpx[i] - 20, fpy[i] - 20, fpx[i] + 20,
						fpy[i] + 20, paint);
			}
		}
		return count;
	}
}
