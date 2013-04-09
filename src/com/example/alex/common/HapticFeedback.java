package com.example.alex.common;

import com.example.alex.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.util.Log;

/**
 * Make a vibrating.<br/>
 * Notice:<br/>
 * 1. Declare vibrator permission in manifest.<br/>
 * 2. Remember to call {@link #init(Context, boolean)} to initialize.
 */
public class HapticFeedback {
	private static final int VIBRATION_PATTERN_ID = R.array.virtualKeyVibePattern;
	/** If no pattern was found, vibrate for a small amount of time. */
	private static final long DURATION = 10; // millisec.
	/** Play the haptic pattern only once. */
	private static final int NO_REPEAT = -1;

	private static final String TAG = "HapticFeedback";
	private long[] mHapticPattern;
	private Vibrator mVibrator;

	private boolean mEnabled;

	/**
	 * Initialize this instance using the app and system configs. Since these
	 * don't change, init is typically called once in 'onCreate'.
	 * 
	 * @param context
	 *            To look up the resources.
	 * @param enabled
	 *            If false, vibrator will not vibrate.
	 */
	public void init(Context context, boolean enabled) {
		mEnabled = enabled;
		if (enabled) {
			mVibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);

			if (!loadHapticPattern(context.getResources())) {
				mHapticPattern = new long[] { 0, DURATION, 2 * DURATION,
						3 * DURATION };
			}
		}
	}

	/**
	 * Generate the haptic feedback vibration. Only one thread can request it.
	 * If the phone is already in a middle of an haptic feedback sequence, the
	 * request is ignored.
	 */
	public void vibrate() {
		if (!mEnabled) {
			return;
		}
		if (mHapticPattern != null && mHapticPattern.length == 1) {
			mVibrator.vibrate(mHapticPattern[0]);
		} else {
			mVibrator.vibrate(mHapticPattern, NO_REPEAT);
		}
	}

	/**
	 * @return true If the haptic pattern was found.
	 */
	private boolean loadHapticPattern(Resources r) {
		int[] pattern;

		mHapticPattern = null;
		try {
			pattern = r.getIntArray(VIBRATION_PATTERN_ID);
		} catch (Resources.NotFoundException nfe) {
			Log.e(TAG, "Vibrate pattern missing.", nfe);
			return false;
		}

		if (null == pattern || pattern.length == 0) {
			Log.e(TAG, "Haptic pattern is null or empty.");
			return false;
		}

		mHapticPattern = new long[pattern.length];
		for (int i = 0; i < pattern.length; i++) {
			mHapticPattern[i] = pattern[i];
		}
		return true;
	}
}
