package com.example.alex;

import com.example.alex.crash.CrashHandler;

import android.app.Application;

public class CrashHandleApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
//        CrashHandler crashHandler = CrashHandler.getInstance();  
//        crashHandler.init(getApplicationContext());
	}

}
