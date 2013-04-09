package com.example.alex.intent;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.alex.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraIntent extends Activity{
	private static final int REQUEST_CAPTURE_IMAGE = 1;
	private static final int REQUEST_CAPTURE_IMAGE2 = 2;
	
	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 imageView = new ImageView(this);
		setContentView(imageView);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CAPTURE_IMAGE:
			if (resultCode == RESULT_OK) {
				imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
				Uri uri = data.getData();
				Toast.makeText(this, "Uri = " + uri, Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
}
