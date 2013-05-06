package com.example.alex.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.R;

public class FaceDetectDemo extends Activity {
	private FaceImageView mFaceImageView;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	private static final int MAX_FACES = 10;
	private static final String TAG = "FaceDetectDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_dectect_demo_activity);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setFace();
	}

	private void initView() {
		mFaceImageView = (FaceImageView) findViewById(R.id.iv_fd_picture);

		// load the photo
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lena);
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

	public void setFace() {
		checkBitmapWidth();

		FaceDetector fd;
		FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
		PointF midpoint = new PointF();
		int[] fpx = null;
		int[] fpy = null;
		int count = 0;
		Log.d(TAG, "width = " + mFaceWidth);
		Log.d(TAG, "height = " + mFaceHeight);
		try {
			fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);
			count = fd.findFaces(mFaceBitmap, faces);
		} catch (Exception e) {
			Log.e(TAG, "setFace(): " + e.toString());
			return;
		}
		Toast.makeText(this, "Dectected " + count + " face.",
				Toast.LENGTH_SHORT).show();

		// check if we detect any faces
		if (count > 0) {

			fpx = new int[count];
			fpy = new int[count];

			for (int i = 0; i < count; i++) {
				try {
					faces[i].getMidPoint(midpoint);

					fpx[i] = (int) midpoint.x;
					fpy[i] = (int) midpoint.y;

				} catch (Exception e) {
					Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
				}
			}
		}

		mFaceImageView.setDisplayPoints(fpx, fpy, count,
				FaceImageView.DISPLAY_STYLE_FACE);
	}

}
