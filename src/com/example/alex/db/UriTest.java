package com.example.alex.db;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class UriTest extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri uri = Uri.parse("content://com.alex.authority/path1/path2?a=1&b=2#133");
		System.out.println("authority: "+uri.getAuthority());
		System.out.println("scheme: " + uri.getScheme());
		System.out.println("path: " + uri.getPath());
		System.out.println("query: " + uri.getQuery());
		System.out.println("fragment: " + uri.getFragment());
	};

	
}
