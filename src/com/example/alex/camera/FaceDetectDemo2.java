package com.example.alex.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.alex.R;

public class FaceDetectDemo2 extends Activity {
	private FaceImageView mFaceImageView;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	private static final int MAX_FACES = 10;
	private static String TAG = "TutorialOnFaceDetect";
	private static boolean DEBUG = false;

	private static final int MSG_INVILIDATE = 0;
	private static final int MSG_DETECT_FACE_FINISH = 1;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INVILIDATE:
				mFaceImageView.invalidate();
				break;
			case MSG_DETECT_FACE_FINISH:
				Toast.makeText(FaceDetectDemo2.this,
						"Dected " + msg.arg1 + " face.", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_dectect_demo_activity);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// perform face detection in setFace() in a background thread
		doLengthyCalc();
	}

	private void initView() {
		mFaceImageView = (FaceImageView) findViewById(R.id.iv_fd_picture);

		// load the photo
		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.wuyuetian);
		mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
		b.recycle();

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
		if (mFaceWidth % 2 != 0) {
			mFaceWidth--;
			mFaceBitmap = Bitmap.createBitmap(mFaceBitmap, 0, 0, mFaceWidth,
					mFaceHeight);
		}
	}

	public int setFace() {
		checkBitmapWidth();

		FaceDetector fd;
		FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
		PointF eyescenter = new PointF();
		float eyesdist = 0.0f;
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

		// check if we detect any faces
		if (count > 0) {
			fpx = new int[count * 2];
			fpy = new int[count * 2];

			for (int i = 0; i < count; i++) {
				try {
					faces[i].getMidPoint(eyescenter);
					eyesdist = faces[i].eyesDistance();

					// set up left eye location
					fpx[2 * i] = (int) (eyescenter.x - eyesdist / 2);
					fpy[2 * i] = (int) eyescenter.y;

					// set up right eye location
					fpx[2 * i + 1] = (int) (eyescenter.x + eyesdist / 2);
					fpy[2 * i + 1] = (int) eyescenter.y;

					if (DEBUG)
						Log.e(TAG,
								"setFace(): face "
										+ i
										+ ": confidence = "
										+ faces[i].confidence()
										+ ", eyes distance = "
										+ faces[i].eyesDistance()
										+ ", pose = ("
										+ faces[i]
												.pose(FaceDetector.Face.EULER_X)
										+ ","
										+ faces[i]
												.pose(FaceDetector.Face.EULER_Y)
										+ ","
										+ faces[i]
												.pose(FaceDetector.Face.EULER_Z)
										+ ")" + ", eyes midpoint = ("
										+ eyescenter.x + "," + eyescenter.y
										+ ")");
				} catch (Exception e) {
					Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
				}
			}
		}

		mFaceImageView.setDisplayPoints(fpx, fpy, count * 2,
				FaceImageView.DISPLAY_STYLE_EYE);
		return count;
	}

	private void doLengthyCalc() {
		Thread t = new Thread() {
			public void run() {
				try {
					int cout = setFace();
					mHandler.sendMessage(mHandler.obtainMessage(MSG_INVILIDATE));
					Message countMessage = mHandler
							.obtainMessage(MSG_DETECT_FACE_FINISH);
					countMessage.arg1 = cout;
					mHandler.sendMessage(countMessage);
				} catch (Exception e) {
					Log.e(TAG, "doLengthyCalc(): " + e.toString());
				}
			}
		};

		t.start();
	}
}
