package com.example.alex.activity;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.alex.R;

/**
 * PreferenceActivity从Android3.0开始，推荐使用Header+Fragment形式。
 * 可以实现在tablet上横屏时同时显示Header和Fragment。
 * 但是有个问题，Header的形式很单一，不能分类显示，只能以一个列表形式显示。如果要实现像系统设置一样的分类显示
 * ，就需要自定义Adapter。目前没有发现比较好的方法。所以推荐不要分类显示。
 * 
 */
public class SettingsActivity extends PreferenceActivity {
	private String TAG = SettingsActivity.class.getSimpleName();

	/**
	 * Build headers from XML.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.activity_settings_headers, target);
	}

	public static class Preference1 extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.fragmented_preferences);
		}
	}

}
