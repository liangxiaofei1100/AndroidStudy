package com.example.alex;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.alex.crash.CrashDialog;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * browseIntent with package name equals this package name;<\br> list activities
 * classified by sub package name;
 */
public class MainActivity extends ListActivity {
	private static final String TAG = "MainActivity";
	public static final String CATEGORY_ALEX = "com.example.category.alex";
	private static final String EXTRA_PATH = "com.example.alex.MainActivity.EXTRA_PATH";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String path = intent.getStringExtra(EXTRA_PATH);

		if (path == null) {
			path = getPackageName();
		}

		setListAdapter(new SimpleAdapter(this, getData(path),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	protected List<Map<String, Object>> getData(String prefix) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.setPackage(getPackageName());

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

		if (null == list)
			return myData;

		int len = list.size();

		Map<String, Boolean> entries = new HashMap<String, Boolean>();

		for (int i = 0; i < len; i++) {
			ResolveInfo info = list.get(i);
			String activityClassName = info.activityInfo.name;
			if (activityClassName.startsWith(prefix)) {
				// get activity name except package name. Note: don't forget
				// there is a "." character after package name;
				String activitySubClassName = activityClassName.substring(
						prefix.length() + 1, activityClassName.length());
				String[] a = activitySubClassName.split("\\.");
				if (a.length == 1) {
					// there is no sub path.
					addItem(myData,
							a[0],
							activityIntent(
									info.activityInfo.applicationInfo.packageName,
									info.activityInfo.name));
				} else if (a.length > 1) {
					// there are sub path
					if (entries.get(a[0]) == null) {
						addItem(myData, a[0], browseIntent(prefix + "." + a[0]));
						entries.put(a[0], true);
					}
				}
			}
		}
		Collections.sort(myData, sDisplayNameComparator);
		return myData;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	protected Intent browseIntent(String path) {
		Intent result = new Intent();
		result.setClass(this, MainActivity.class);
		result.putExtra(EXTRA_PATH, path);
		return result;
	}

	/**
	 * Create intent with package and class name.
	 * 
	 * @param pkg
	 * @param componentName
	 * @return
	 */
	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	/**
	 * Add title and intent to Map.
	 * 
	 * @param data
	 * @param name
	 * @param intent
	 */
	protected void addItem(List<Map<String, Object>> data, String name,
			Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
