package com.example.alex.activity;


import android.app.Activity;
import android.content.Context;
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
import android.widget.Scroller;

import com.example.alex.R;

/**
 * Right slide and finish activity.
 * 
 */
public class RightSlideLayout extends ViewGroup {
	private static final String TAG = "RightSlideLayout";
	private static final boolean DEBUG = false;

	private VelocityTracker mVelocityTracker;

	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private float mLastMotionX;
	private float mLastMotionY;
	private float mInitialMotionX;

	private boolean mIsBeingDragged = false;
	private boolean mIsUnableToDrag;
	private boolean mIsIgnoreDrag;

	private int mDefaultGutterSize;
	private int mGutterSize;
	private static final int DEFAULT_GUTTER_SIZE = 16; // dips
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

	private Scroller mScroller;
	/** Distance of the minimum horizontal scroll(in px). */
	private int mMinimumScrollX;
	/** Distance of the maximum horizontal scroll(in px). */
	private int mMaximumScrollX;
	private boolean mIsScrollingToRightEdge = false;
	private boolean mIsScrollingToLeftEdge = false;
	private static final int MAX_SETTLE_DURATION = 600; // ms

	private OnSlideListener mOnSlideListener;

	public RightSlideLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public RightSlideLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
		mMinimumScrollX = 0 - getResources().getDisplayMetrics().widthPixels;
		mMaximumScrollX = 0;

		ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(viewConfiguration);
		mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

		final float density = context.getResources().getDisplayMetrics().density;
		mDefaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
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
		mVelocityTracker.addMovement(ev);
		if (DEBUG)
			Log.v(TAG, "Intercept returning " + mIsBeingDragged + " !");
		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
			// Don't handle edge touches immediately -- they may actually belong
			// to one of our descendants.
			return false;
		}

		if (mIsScrollingToRightEdge) {
			if (DEBUG) {
				Log.v(TAG, "Is srollling to right edge, ignore.");
			}
			return false;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			// Remember where the motion event started
			mLastMotionX = mInitialMotionX = event.getX();
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			break;
		}
		case MotionEvent.ACTION_MOVE:
			if (mIsIgnoreDrag) {
				if (DEBUG)
					Log.v(TAG, "onTouchEvent mIsIgnoreDrag!");
				return false;
			}

			if (!mIsBeingDragged) {
				final int pointerIndex = MotionEventCompat.findPointerIndex(
						event, mActivePointerId);
				final float x = MotionEventCompat.getX(event, pointerIndex);
				final float xDiff = Math.abs(x - mLastMotionX);
				final float y = MotionEventCompat.getY(event, pointerIndex);
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
						.findPointerIndex(event, mActivePointerId);
				float x;
				try {
					x = MotionEventCompat.getX(event, activePointerIndex);
				} catch (Exception e) {
					// Exception happened one time which cased by input dispatch
					// error then lead to activePointerIndex error.
					x = event.getX();
				}
				performDrag(x);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
						velocityTracker, mActivePointerId);
				final int curScrollX = getScrollX();
				final int positionX = Math.abs(curScrollX);
				if (DEBUG)
					Log.v(TAG, "MotionEvent."
							+ "ACTION_UP begin scroll . positionX = "
							+ positionX);
				if (Math.abs(initialVelocity) > mMinimumVelocity) {
					if (DEBUG) {
						Log.v(TAG, "Fling. velocity = " + initialVelocity);
					}
					if (positionX > getWidth() / 2) {
						scrollToRightEdge();
					} else {
						if (initialVelocity > 0) {
							scrollToRightEdge();
						} else {
							scrollToLeftEdge();
						}
					}
				} else {
					if (DEBUG) {
						Log.d(TAG, "Not Fling. velocity = " + initialVelocity);
					}
					if (positionX > getWidth() / 2) {
						scrollToRightEdge();
					} else {
						scrollToLeftEdge();
					}
				}

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
			final int index = MotionEventCompat.getActionIndex(event);
			final float x = MotionEventCompat.getX(event, index);
			mLastMotionX = x;
			mActivePointerId = MotionEventCompat.getPointerId(event, index);
			break;
		}
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(event);
			mLastMotionX = MotionEventCompat
					.getX(event, MotionEventCompat.findPointerIndex(event,
							mActivePointerId));
			break;
		}
		return true;
	}

	private void scrollToLeftEdge() {
		if (DEBUG)
			Log.v(TAG, "scrollToLeftEdge");
		scrollByWithAnim(mMaximumScrollX - getScrollX());
		mIsScrollingToLeftEdge = true;
	}

	private void scrollToRightEdge() {
		if (DEBUG)
			Log.v(TAG, "scrollToRightEdge");
		scrollByWithAnim(mMinimumScrollX - getScrollX());
		mIsScrollingToRightEdge = true;
	}

	/**
	 * With the horizontal scroll of the animation
	 * 
	 * @param dx
	 *            x-axis offset
	 */
	private void scrollByWithAnim(int dx) {
		if (dx == 0) {
			return;
		}
		int duration = Math.min(Math.abs(dx), MAX_SETTLE_DURATION);
		mScroller.startScroll(getScrollX(), 0, dx, 0, duration);
		invalidate();
	}

	private void performDrag(float x) {
		int curScrollX = getScrollX();
		if (DEBUG) {
			Log.v(TAG, "performDrag x = " + x + ", curScrollX = " + curScrollX);
		}

		// compute the x-axis offset from last point to current point
		int deltaX = (int) (mLastMotionX - x);
		if (curScrollX + deltaX < mMinimumScrollX) {
			deltaX = mMinimumScrollX - curScrollX;
			mLastMotionX = mLastMotionX - deltaX;
		} else if (curScrollX + deltaX > mMaximumScrollX) {
			deltaX = mMaximumScrollX - curScrollX;
			mLastMotionX = mLastMotionX - deltaX;
		} else {
			mLastMotionX = x;
		}

		// Move view to the current point
		if (deltaX != 0) {
			scrollBy(deltaX, 0);
		}

		if (Math.abs(getScrollX()) >= Math.abs(mMinimumScrollX)) {
			onSlideToRightEdge();
		}

		mIsScrollingToLeftEdge = false;
		mIsScrollingToRightEdge = false;
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

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}

		final int curScrollX = mScroller.getCurrX();
		if (mIsScrollingToRightEdge
				&& Math.abs(curScrollX) >= Math.abs(mMinimumScrollX)) {
			if (DEBUG) {
				Log.v(TAG, "Scrolled to right edge.");
			}
			onSlideToRightEdge();
			mIsScrollingToRightEdge = false;
		}

		if (DEBUG) {
			Log.v(TAG, "Scrolled to left edge.");
		}
		if (mIsScrollingToLeftEdge
				&& Math.abs(curScrollX) <= Math.abs(mMaximumScrollX) + 5) {
			if (DEBUG) {
				Log.v(TAG, "Scrolled to left edge.");
			}
			mIsScrollingToLeftEdge = false;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int measuredWidth = getMeasuredWidth();
		final int maxGutterSize = measuredWidth / 10;
		mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);

		// check child count
		final int count = getChildCount();
		if (count > 1) {
			throw new IllegalStateException(
					"RightSlideLayout can only have one child at most!");
		}

		// measure child views
		if (count > 0) {
			View child = getChildAt(0);
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		if (count <= 0) {
			return;
		}

		final View child = getChildAt(0);

		// Set the size and position of Main Child
		// Fix bug that when set activity on title, there will be a blank area
		// on the top(it is title bar). So set the top = 0;
		t = 0;
		if (child != null) {
			child.layout(l, t, l + child.getMeasuredWidth(),
					t + child.getMeasuredHeight());
		}
	}

	private boolean isGutterDrag(float x, float dx) {
		return (x < mGutterSize && dx > 0)
				|| (x > getWidth() - mGutterSize && dx < 0);
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

	public void setOnSlashQuitListener(OnSlideListener listener) {
		mOnSlideListener = listener;
	}

	private void onSlideToRightEdge() {
		if (mOnSlideListener != null) {
			mOnSlideListener.onSlide();
		} else if (getContext() instanceof Activity) {
			Activity activity = (Activity) getContext();
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
		}
	}

	public interface OnSlideListener {
		/**
		 * The layout is scrolled to the right edge.
		 */
		void onSlide();
	}

}
