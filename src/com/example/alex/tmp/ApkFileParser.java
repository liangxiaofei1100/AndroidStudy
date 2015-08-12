package com.example.alex.tmp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.example.alex.common.ShowResultActivity;

public class ApkFileParser extends ShowResultActivity {

    @Override
    protected void onButtonClick() {
        PackageManager pm = this.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(Environment.getExternalStorageDirectory() + "/test.apk", PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;
            mTextView.append("Parse success. packageName=" + packageName);
        } else {
            mTextView.append("Parse fail.");
        }
    }
}
