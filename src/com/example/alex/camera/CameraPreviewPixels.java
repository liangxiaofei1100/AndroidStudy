package com.example.alex.camera;

import java.util.List;

import com.example.alex.common.SimpleTestActivity;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.View;

public class CameraPreviewPixels extends SimpleTestActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInputEditText.setVisibility(View.GONE);
		mShowResultButton.setVisibility(View.GONE);
		mResulTextView.setText("");
	}

	@Override
	protected void onResume() {
		super.onResume();
		int n = Camera.getNumberOfCameras();

		for (int i = 0; i < n; i++) {
			Camera camera = Camera.open(i);
			Parameters parameters = camera.getParameters();
			List<Size> supportedPreviewSizes = parameters
					.getSupportedPreviewSizes();
			List<Size> supportedPictureSizes = parameters
					.getSupportedPictureSizes();

			showSizes(i, supportedPictureSizes, "supportedPictureSizes");
			showSizes(i, supportedPreviewSizes, "supportedPreviewSizes");

			camera.release();
		}
	}

	private void showSizes(int cameraId, List<Size> sizes, String type) {
		mResulTextView.append("CameraId:" + cameraId + ", " + type + ":\n");
		for (Size size : sizes) {
			mResulTextView.append(size.width + " x " + size.height + '\n');
		}
	}
}
