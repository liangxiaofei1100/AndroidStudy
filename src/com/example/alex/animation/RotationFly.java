package com.example.alex.animation;

import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.alex.R;

public class RotationFly extends Activity {
	private ImageView mImageView;
	private View mDestinationView;
	private Context mContext;
	private LinearLayout mLayout;
	TransportAnimationView mTransportAnimationView;

	private static final String TAG = "RotationFly";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotation_fly);
		mContext = this;
		mDestinationView = findViewById(R.id.iv_rotation_fly_destination);

		mLayout = (LinearLayout) findViewById(R.id.ll_rotation_fly);
		final ImageView[] images = new ImageView[10];
		for (int i = 0; i < 10; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(R.drawable.ic_launcher);
			mLayout.addView(imageView, LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			images[i] = imageView;
		}

		mTransportAnimationView = new TransportAnimationView(mContext);

		mImageView = (ImageView) findViewById(R.id.iv_rotation_fly);
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View view) {
				ViewGroup contentView = (RelativeLayout) mImageView.getParent();
				mTransportAnimationView.startTransportAnimation(contentView,
						mDestinationView, images);
				Log.d(TAG,
						"contentView child count = "
								+ contentView.getChildCount());

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_rotation_fly, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
