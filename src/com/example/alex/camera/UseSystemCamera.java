package com.example.alex.camera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alex.R;

public class UseSystemCamera extends Activity {

	private final static int REQUEST_TAKE_PICTURE = 1;
	private final static int REQUEST_CROP_PICTURE = 2;
	private final static int REQUEST_RECORD_VIDEO = 3;
	private Uri mPictureUri;
	private ImageView mImageView;
	private Camera mCamera;
	private boolean mIsLightOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_system_camera);
		mImageView = (ImageView) findViewById(R.id.iv_camera_picture);
		Button button = (Button) findViewById(R.id.btn_take_picture);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePicture(mPictureUri);
			}
		});

		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/test/");
		dir.mkdirs();
		File file = new File(dir, "test.jpg");
		mPictureUri = Uri.fromFile(file);

		Button button2 = (Button) findViewById(R.id.btn_record_video);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recordVideo();
			}

		});

		Button button3 = (Button) findViewById(R.id.btn_camera_flash_light);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCamera == null) {
					return;
				}

				Parameters parameters = mCamera.getParameters();
				if (mIsLightOn) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(parameters);
					mCamera.stopPreview();
					mIsLightOn = false;
				} else {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(parameters);
					mCamera.startPreview();
					mIsLightOn = true;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCamera == null) {
			mCamera = Camera.open();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	private void recordVideo() {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/test/");
		dir.mkdirs();
		File file = new File(dir, "test.mp4");
		Uri uri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// 设置视频质量，0代表低分辨率，1代表高分辨率。
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		// 设置视频文件大小限制,单位字节。特别注意：大小一定要是long类型，否则大小限制可能会无效。
		intent.putExtra("android.intent.extra.sizeLimit", 200 * 1024L);
		// 设置时间长度，单位秒。
		intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, REQUEST_RECORD_VIDEO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_TAKE_PICTURE:
			resizePicture(mPictureUri);
			break;
		case REQUEST_CROP_PICTURE:
			if (data != null) {
				Bitmap bitmap = data.getExtras().getParcelable("data");
				mImageView.setImageBitmap(bitmap);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void takePicture(Uri uri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, REQUEST_TAKE_PICTURE);
	}

	public void resizePicture(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 设置裁剪框比例为 1:1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 设置输出图片的像素大小
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		// 将裁剪后的图片以Bitmap返回
		intent.putExtra("return-data", true);
		// 也可以将裁剪后的图片存储到文件
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/test/");
		dir.mkdirs();
		File file = new File(dir, "test_crop.jpg");
		Uri cropUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
		intent.putExtra("outputFormat", "jpg");// 返回格式

		startActivityForResult(intent, REQUEST_CROP_PICTURE);
	}

}
