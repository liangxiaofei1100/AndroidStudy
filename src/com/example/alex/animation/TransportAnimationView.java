package com.example.alex.animation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

/**
 * Show transport animation.
 * 
 */
@SuppressWarnings("deprecation")
public class TransportAnimationView extends View {
	private static final String TAG = "TransportAnimationView";
	private static final int DURATION_TRANSLATE = 500;
	private static final int DURATION_ROTATE = 500;
	private static final int DURATION_SCALE = 500;

	public TransportAnimationView(Context context) {
		super(context);

	}

	/**
	 * Show transport animation.
	 * 
	 * @param containView
	 *            The view group to contain the animation view.
	 * @param endView
	 *            Provide the end position of animation.
	 * @param startViews
	 *            Provide animation images.
	 */
	public void startTransportAnimation(ViewGroup containView, View endView,
			ImageView... startViews) {
		// Create new layout.
		AbsoluteLayout absoluteLayout = new AbsoluteLayout(getContext());
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		containView.addView(absoluteLayout, layoutParams);
		int[] layoutPosition = new int[2];
		absoluteLayout.getLocationInWindow(layoutPosition);

		// Create shadow image views and start animate.
		int[] startViewPosition = new int[2];
		int[] endViewPosition = new int[2];
		boolean isTheLastAnimation = false;
		for (int i = 0; i < startViews.length; i++) {
			ImageView startView = startViews[i];
			ImageView shadowView = new ImageView(getContext());
			shadowView.setImageDrawable(startView.getDrawable());
			startView.getLocationInWindow(startViewPosition);
			AbsoluteLayout.LayoutParams abLayoutParams = new AbsoluteLayout.LayoutParams(
					startView.getWidth(), startView.getHeight(),
					startViewPosition[0] - layoutPosition[0],
					startViewPosition[1] - layoutPosition[1]);
			absoluteLayout.addView(shadowView, abLayoutParams);

			endView.getLocationInWindow(endViewPosition);
			final int xDelta = endViewPosition[0] + endView.getWidth() / 2
					- startViewPosition[0] - startView.getWidth() / 2;
			final int yDelta = -500;
			isTheLastAnimation = (i == (startViews.length - 1));
			startAnimation(shadowView, xDelta, yDelta, absoluteLayout,
					containView, isTheLastAnimation);
		}
	}

	/**
	 * 
	 * @param startView
	 * @param containView
	 * @param xDelta
	 * @param yDelta
	 * @param isTheLastAnimation
	 */
	private void startAnimation(final View startView, final int xDelta,
			final int yDelta, final ViewGroup animationLayout,
			final ViewGroup containViewOfAnimateLayout,
			final boolean isTheLastAnimation) {
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(DURATION_ROTATE);

		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				xDelta, 0, yDelta);
		translateAnimation.setInterpolator(AnimationUtils
				.loadInterpolator(getContext(),
						android.R.anim.accelerate_decelerate_interpolator));
		translateAnimation.setDuration(DURATION_TRANSLATE);

		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(translateAnimation);

		final AnimationListener animationListener = new AnimationListener() {
			/** Animation index */
			private int mIndex = 0;

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				switch (mIndex) {
				case 0:
					// Move view to the position of the TranslateAnimation end.
					startView.offsetLeftAndRight(xDelta);
					startView.offsetTopAndBottom(yDelta);

					ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1,
							0, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation
							.setInterpolator(AnimationUtils
									.loadInterpolator(
											getContext(),
											android.R.anim.accelerate_decelerate_interpolator));
					scaleAnimation.setDuration(DURATION_SCALE);
					scaleAnimation.setStartOffset(100);
					scaleAnimation.setAnimationListener(this);
					mIndex = 1;
					startView.startAnimation(scaleAnimation);
					break;
				case 1:
					// Release resource after animation finish.
					animationLayout.removeView(startView);
					if (isTheLastAnimation) {
						containViewOfAnimateLayout.removeView(animationLayout);
						System.gc();
					}
					break;
				default:
					break;
				}

			}
		};
		animationSet.setAnimationListener(animationListener);
		startView.startAnimation(animationSet);
	}

}
