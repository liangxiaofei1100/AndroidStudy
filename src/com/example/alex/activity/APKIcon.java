package com.example.alex.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class APKIcon extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView imageView = new ImageView(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(layoutParams);
		setContentView(imageView);

		String apkPath = getAPKFilePath(this);
		imageView.setImageDrawable(getApkIcon(this, apkPath));
	}

	/**
	 * Get this APP's APK file.
	 * 
	 * @param context
	 * @return
	 */
	public static String getAPKFilePath(Context context) {
		ApplicationInfo packageInfo = context.getApplicationInfo();
		return packageInfo.sourceDir;
	}

	/*
	 * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过 appInfo.publicSourceDir =
	 * apkPath;来修正这个问题，详情参见:
	 * http://code.google.com/p/android/issues/detail?id=9151
	 */
	/**
	 * Get apk name from an apk file which is not installed.
	 * 
	 * @param context
	 * @param apkPath
	 * @return
	 */
	public static Drawable getApkIcon(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			return appInfo.loadIcon(pm);
		}
		return null;
	}
}
