package com.example.alex.camera;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CameraPreviewPixels extends Activity {
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textView = new TextView(this);
		setContentView(textView);
		int n = Camera.getNumberOfCameras();

		for (int i = 0; i < n; i++) {
			Camera camera = Camera.open(i);
			Parameters parameters = camera.getParameters();
			List<Size> supportedPreviewSizes = parameters
					.getSupportedPreviewSizes();
			List<Size> supportedPictureSizes = parameters
					.getSupportedPictureSizes();
			showSizes(supportedPictureSizes, "supportedPictureSizes");
//			showSizes(supportedPreviewSizes, "supportedPreviewSizes");
			camera.release();
		}

	}

	private void showSizes(List<Size> sizes, String type) {
		for (Size size : sizes) {
			textView.append(type + ": " + size.width + " x " + size.height
					+ '\n');
		}
	}
}
