package com.example.alex.view;

import com.example.alex.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

interface OnFlingListener {
	void onFling(float f, float g);
}

public class FragmentSwitch extends Activity implements OnFlingListener {

	private static final String TAG = "FragmentSwitch";
	GestureDetector gestureScanner;
	private FlingFragment mCurrentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int i = 0; i < 10; i++) {
			actionBar.addTab(
					actionBar
							.newTab()
							.setText("Tab " + i)
							.setTabListener(
									new TabListener<FlingFragment>(this, "tab "
											+ i, FlingFragment.class,
											savedInstanceState)), i);
		}

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					"tab", 0));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	public class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(Activity activity, String tag, Class<T> clz,
				Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();

				ft.detach(mFragment);
				ft.commit();
			}
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// ft.setCustomAnimations(android.R.animator.fade_in,
			// android.R.animator.fade_out, android.R.animator.fade_in,
			// android.R.animator.fade_out);
			ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
					R.animator.fragment_slide_right_exit);

			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(),
						mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
			mCurrentFragment = (FlingFragment) mFragment;
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
	}

	private static final int FLING_DESTANCE = 120;

	@Override
	public void onFling(float x1, float x2) {

		Log.d(TAG, "onfling x1 = " + x1 + ", x2 = " + x2);
		ActionBar actionBar = getActionBar();
		int index = actionBar.getSelectedNavigationIndex();
		int count = actionBar.getNavigationItemCount();
		Log.d(TAG, "index = " + index + " count = " + count);
		int position = -1;

		if (x2 - x1 > FLING_DESTANCE) {
			// fling to right
			position = index + 1;
			if (position == count) {
				position = 0;
			}
		} else if (x2 - x1 < -FLING_DESTANCE) {
			// fling to left
			position = index - 1;
			if (position < 0) {
				position = count - 1;
			}
		} else {
			return;
		}

		Log.d(TAG, "set position " + position);
		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mCurrentFragment != null) {
			mCurrentFragment.getGestureDetector().onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
}
