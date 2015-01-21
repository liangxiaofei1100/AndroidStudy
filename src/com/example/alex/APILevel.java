package com.example.alex;

public class APILevel {

	public static boolean isAPILevelMatch(int requiredLevel) {
		int apiLevel = getCurrentAPILevel();
		if (apiLevel >= requiredLevel) {
			return true;
		}

		return false;
	}

	public static int getCurrentAPILevel() {
		return android.os.Build.VERSION.SDK_INT;
	}
}
