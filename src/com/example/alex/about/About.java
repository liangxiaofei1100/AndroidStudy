package com.example.alex.about;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;
import com.example.alex.R;
import com.example.alex.BuildConfig;

public class About extends ShowResultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTextView.setText("package: " + getPackageName() + "\n");

		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			mTextView.append("version code: " + packageInfo.versionCode + "\n");
			mTextView.append("version name: " + packageInfo.versionName + "\n");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		mTextView.append("channel: " + getMetaData(mContext, "CHANEL") + "\n");
		mTextView.append("debug: " + BuildConfig.DEBUG + "\n");
	}

	/**
	 * 获取Manifest中配置的键值对
	 * 
	 * @param context
	 * @param key
	 * @return 键对应的值
	 */
	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}
}
