package com.example.alex.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class SlashView extends FrameLayout implements AnimationListener {
	private static final String TAG = "SlashView";
	private static final boolean DEBUG = true;

	private VelocityTracker mVelocityTracker;

	private int mTouchSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;

	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private int mFlingDistance;
	private float mLastMotionX;
	private float mInitialMotionX;

	private boolean mIsBeingDragged = false;

	private float mLastMotionY;

	private boolean mIsUnableToDrag;
	private boolean mIsIgnoreDrag;

	private int mDefaultGutterSize;
	private int mGutterSize;
	/**
	 * ID of the active pointer. This is used to retain consistency during
	 * drags/flings if multiple pointers are used.
	 */
	private int mActivePointerId = INVALID_POINTER;
	/**
	 * Sentinel value for no current active pointer. Used by
	 * {@link #mActivePointerId}.
	 * 
	 */
	private static final int INVALID_POINTER = -1;
	private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
	private static final int DEFAULT_GUTTER_SIZE = 16; // dips

	public SlashView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(viewConfiguration);
		mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
		mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

		final float density = context.getResources().getDisplayMetrics().density;
		mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
		mDefaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
	}

	public SlashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlashView(Context context) {
		super(context);
		init(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*
		 * This method JUST determines whether we want to intercept the motion.
		 * If we return true, onMotionEvent will be called and we do the actual
		 * scrolling there.
		 */

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

		// Always take care of the touch gesture being complete.
		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			// Release the drag.
			mIsBeingDragged = false;
			mIsUnableToDrag = false;
			mActivePointerId = INVALID_POINTER;
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			if (DEBUG)
				Log.v(TAG, "Intercept returning true!");
			return false;
		}

		// Nothing more to do here if we have decided whether or not we
		// are dragging.
		if (action != MotionEvent.ACTION_DOWN) {
			if (mIsBeingDragged) {
				if (DEBUG)
					Log.v(TAG, "Intercept returning true!");
				return true;
			}
			if (mIsUnableToDrag) {
				if (DEBUG)
					Log.v(TAG, "Intercept returning false!");
				return false;
			}
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			/*
			 * mIsBeingDragged == false, otherwise the shortcut would have
			 * caught it. Check whether the user has moved far enough from his
			 * original down touch.
			 */

			/*
			 * Locally do absolute value. mLastMotionY is set to the y value of
			 * the down event.
			 */
			final int activePointerId = mActivePointerId;
			if (activePointerId == INVALID_POINTER) {
				// If we don't have a valid id, the touch down wasn't on
				// content.
				break;
			}

			final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
					activePointerId);
			final float x = MotionEventCompat.getX(ev, pointerIndex);
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float y = MotionEventCompat.getY(ev, pointerIndex);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (DEBUG)
				Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + ","
						+ yDiff);

			if (dx != 0 && !isGutterDrag(mLastMotionX, dx)
					&& canScroll(this, false, (int) dx, (int) x, (int) y)) {
				// Nested view has scrollable area under this point. Let it be
				// handled there.
				mInitialMotionX = mLastMotionX = x;
				mLastMotionY = y;
				mIsUnableToDrag = true;
				if (DEBUG)
					Log.v(TAG, "Intercept returning false!");
				return false;
			}

			if (xDiff < mTouchSlop && yDiff < mTouchSlop) {
				Log.d(TAG, "xDiff = " + xDiff + ", yDiff = " + yDiff
						+ ", mTouchSlop = " + mTouchSlop);
				if (DEBUG)
					Log.v(TAG, "Intercept returning false!");
				return false;
			} else if (xDiff > mTouchSlop && xDiff > yDiff) {
				if (DEBUG)
					Log.v(TAG, "Starting drag!");
				mIsBeingDragged = true;
				mLastMotionX = dx > 0 ? mInitialMotionX + mTouchSlop
						: mInitialMotionX - mTouchSlop;
			} else {
				if (yDiff > mTouchSlop) {
					// The finger has moved enough in the vertical
					// direction to be counted as a drag... abort
					// any attempt to drag horizontally, to work correctly
					// with children that have scrolling containers.
					if (DEBUG)
						Log.v(TAG, "Starting unable to drag!");
					mIsUnableToDrag = true;
				}
			}
			// if (mIsBeingDragged) {
			// // Scroll to follow the motion event
			// // TODO ????
			// if (performDrag(x, mLastMotionX)) {
			// ViewCompat.postInvalidateOnAnimation(this);
			// }
			// }
			break;
		}

		case MotionEvent.ACTION_DOWN: {
			/*
			 * Remember location of down touch. ACTION_DOWN always refers to
			 * pointer index 0.
			 */
			mLastMotionX = mInitialMotionX = ev.getX();
			mLastMotionY = ev.getY();
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mIsUnableToDrag = false;

			// TODO ????
			mIsBeingDragged = false;

			if (DEBUG)
				Log.v(TAG, "Down at " + mLastMotionX + "," + mLastMotionY
						+ " mIsBeingDragged=" + mIsBeingDragged
						+ "mIsUnableToDrag=" + mIsUnableToDrag);
			break;
		}

		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		// mVelocityTracker.addMovement(ev);
		if (DEBUG)
			Log.v(TAG, "Intercept returning " + mIsBeingDragged + " !");
		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		return mIsBeingDragged;
	}

	VelocityTracker mVelocityTracker2;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (mVelocityTracker2 == null) {
			mVelocityTracker2 = VelocityTracker.obtain();
		}
		mVelocityTracker2.addMovement(ev);
		if (action == MotionEvent.ACTION_UP) {
			mVelocityTracker2.computeCurrentVelocity(1000);
			Log.d(TAG,
					"mVelocityTracker2 = " + mVelocityTracker2.getXVelocity());
			mVelocityTracker2.clear();
			mVelocityTracker2.recycle();
			mVelocityTracker2 = null;
		}

		if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
			// Don't handle edge touches immediately -- they may actually belong
			// to one of our descendants.
			return false;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			// TODO ????
			// mScroller.abortAnimation();
			// mPopulatePending = false;
			// populate();
			// mIsBeingDragged = true;

			// Remember where the motion event started
			mLastMotionX = mInitialMotionX = ev.getX();
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}
		case MotionEvent.ACTION_MOVE:
			if (mIsIgnoreDrag) {
				if (DEBUG)
					Log.v(TAG, "onTouchEvent mIsIgnoreDrag!");
				return false;
			}

			if (!mIsBeingDragged) {
				final int pointerIndex = MotionEventCompat.findPointerIndex(ev,
						mActivePointerId);
				final float x = MotionEventCompat.getX(ev, pointerIndex);
				final float xDiff = Math.abs(x - mLastMotionX);
				final float y = MotionEventCompat.getY(ev, pointerIndex);
				final float yDiff = Math.abs(y - mLastMotionY);
				if (DEBUG)
					Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff
							+ "," + yDiff);
				if (xDiff > mTouchSlop && xDiff > yDiff * 2) {
					if (DEBUG)
						Log.v(TAG, "Starting drag!");
					mIsBeingDragged = true;
					mLastMotionX = x - mInitialMotionX > 0 ? mInitialMotionX
							+ mTouchSlop : mInitialMotionX - mTouchSlop;
				} else {
					if (yDiff > mTouchSlop) {
						if (DEBUG)
							Log.v(TAG, "Starting ignore drag!");
						mIsIgnoreDrag = true;
					}
				}
			}
			// Not else! Note that mIsBeingDragged can be set above.
			if (mIsBeingDragged) {
				// Scroll to follow the motion event
				final int activePointerIndex = MotionEventCompat
						.findPointerIndex(ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, activePointerIndex);
				performDrag(x, mLastMotionX);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
						velocityTracker, mActivePointerId);
				if (DEBUG)
					Log.v(TAG, "MotionEvent." + "ACTION_UP begin scroll.");
				Log.d(TAG, "MotionEvent.ACTION_UP direction = "
						+ (getX() - mLastDragX));
				if (Math.abs(initialVelocity) > mMinimumVelocity) {
					Log.d(TAG, "Fling");
					if (getX() > getWidth() / 2) {
						startScroll(getX(), getWidth());
					} else {
						if (getX() - mLastDragX > 0) {
							startScroll(getX(), getWidth());
						} else {
							startScroll(getX(), 0);
						}
					}
				} else {
					Log.d(TAG, "Not Fling");
					if (getX() > getWidth() / 2) {
						startScroll(getX(), getWidth());
					} else {
						startScroll(getX(), 0);
					}
				}

				// if (initialVelocity > 0) {
				// startScroll(getX(), getWidth());
				// } else {
				// startScroll(getX(), 0);
				// }

				mActivePointerId = INVALID_POINTER;
				endDrag();
			}

			mIsIgnoreDrag = false;
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mIsBeingDragged) {
				mActivePointerId = INVALID_POINTER;
				endDrag();
			}
			mIsIgnoreDrag = false;
			break;
		case MotionEventCompat.ACTION_POINTER_DOWN: {
			final int index = MotionEventCompat.getActionIndex(ev);
			final float x = MotionEventCompat.getX(ev, index);
			mLastMotionX = x;
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			mLastMotionX = MotionEventCompat.getX(ev,
					MotionEventCompat.findPointerIndex(ev, mActivePointerId));
			break;
		}
		return true;
	}
	
	public float getX() {
		return 0;
	}

	private void startScroll(final float from, final float to) {
		TranslateAnimation animation = new TranslateAnimation(0, to - from, 0,
				0);
		animation.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
				android.R.anim.accelerate_decelerate_interpolator));
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int left = getLeft() + (int) (to - from);
				int top = getTop();
				int width = getWidth();
				int height = getHeight();
				clearAnimation();
				layout(left, top, left + width, top + height);
				if (to == getWidth()) {
					Activity activity = (Activity) getContext();
					activity.finish();
				}
			}
		});
		animation.setFillAfter(true);
		startAnimation(animation);

		mIsQuitAnimation = !(to == 0);
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = MotionEventCompat.getActionIndex(ev);
		final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
			mActivePointerId = MotionEventCompat.getPointerId(ev,
					newPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	/**
	 * Tests scrollability within child views of v given a delta of dx.
	 * 
	 * @param v
	 *            View to test for horizontal scrollability
	 * @param checkV
	 *            Whether the view v passed should itself be checked for
	 *            scrollability (true), or just its children (false).
	 * @param dx
	 *            Delta scrolled in pixels
	 * @param x
	 *            X coordinate of the active touch point
	 * @param y
	 *            Y coordinate of the active touch point
	 * @return true if child views of v can be scrolled by delta of dx.
	 */
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewGroup) {
			final ViewGroup group = (ViewGroup) v;
			final int scrollX = v.getScrollX();
			final int scrollY = v.getScrollY();
			final int count = group.getChildCount();
			// Count backwards - let topmost views consume scroll distance
			// first.
			for (int i = count - 1; i >= 0; i--) {
				// TODO: Add versioned support here for transformed views.
				// This will not work for transformed views in Honeycomb+
				final View child = group.getChildAt(i);
				if (x + scrollX >= child.getLeft()
						&& x + scrollX < child.getRight()
						&& y + scrollY >= child.getTop()
						&& y + scrollY < child.getBottom()
						&& canScroll(child, true, dx,
								x + scrollX - child.getLeft(), y + scrollY
										- child.getTop())) {
					return true;
				}
			}
		}

		return checkV && ViewCompat.canScrollHorizontally(v, -dx);
	}

	private float mLastDragX;
	
	public void setX(int x) {
		layout(x, getTop(), getRight(), getBottom());
	}
	private boolean performDrag(float x, float lastMotionX) {
		Log.d(TAG, "performDrag x = " + x);
		float dx = x - lastMotionX;
		mLastDragX = getX();
		float destinationX = getX() + dx;
		if (destinationX < 0) {
			setX(0);
		} else {
			setX((int)destinationX);
		}
		return false;
	}

	private void endDrag() {
		if (DEBUG) {
			Log.d(TAG, "endDrag");
		}
		mIsBeingDragged = false;
		mIsUnableToDrag = false;

		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private boolean isGutterDrag(float x, float dx) {
		return (x < mGutterSize && dx > 0)
				|| (x > getWidth() - mGutterSize && dx < 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int measuredWidth = getMeasuredWidth();
		final int maxGutterSize = measuredWidth / 10;
		mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		mIsAnimationRunning = true;
	}

	private boolean mIsAnimationRunning = false;
	private boolean mIsQuitAnimation = false;

	@Override
	public void onAnimationEnd(Animation animation) {
		Log.d(TAG, "onAnimationEnd");
		if (mIsQuitAnimation) {
			Activity activity = (Activity) getContext();
			activity.finish();
		} else {
		}
		mIsAnimationRunning = false;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	private OnSlashQuitListener mOnSlashQuitListener;

	public void setOnSlashQuitListener(OnSlashQuitListener listener) {
		mOnSlashQuitListener = listener;
	}

	public interface OnSlashQuitListener {
		void onSlashQuit();

	}
}
