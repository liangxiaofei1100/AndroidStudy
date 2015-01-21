package com.example.alex.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.alex.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Utils to find package and application.
 */
public class PackageFinder extends ListActivity {
	private static final String TAG = PackageFinder.class.getSimpleName();
	private TextView mNoteTextView;
	private EditText mPackageNameEditText;
	private SimpleAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_package_finder);
		initView();
		showLauncherActivities();
	}

	private void initView() {
		mPackageNameEditText = (EditText) findViewById(R.id.et_package);

		mAdapter = new SimpleAdapter(this, mData,
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 });
		setListAdapter(mAdapter);

		mNoteTextView = (TextView) findViewById(R.id.tv_note);
	}

	/**
	 * Search Button
	 * 
	 * @param view
	 */
	public void startSearch(View view) {
		String packageName = mPackageNameEditText.getText().toString();

		if (TextUtils.isEmpty(packageName)) {
			showLauncherActivities();
			return;
		}

		searchActivity(packageName);
	}

	private void showLauncherActivities() {
		mData.clear();

		Intent queryIntent = new Intent();
		queryIntent.setAction(Intent.ACTION_MAIN);
		queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(queryIntent, 0);
		for (ResolveInfo info : list) {
			Log.d(TAG,
					"label=" + info.activityInfo.loadLabel(getPackageManager()));

			addItem(mData,
					info.activityInfo.loadLabel(getPackageManager()).toString()
							+ " - " + info.activityInfo.packageName,
					activityIntent(
							info.activityInfo.applicationInfo.packageName,
							info.activityInfo.name));
		}

		Collections.sort(mData, sDisplayNameComparator);
		mAdapter.notifyDataSetChanged();

		mNoteTextView.setText("Packages in Launcher.");
	}

	private void searchActivity(String packageName) {
		Log.d(TAG, "searchActivity");
		mData.clear();

		Intent queryIntent = new Intent();
		queryIntent.setPackage(packageName);

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(queryIntent, 0);
		for (ResolveInfo info : list) {
			Log.d(TAG,
					"label=" + info.activityInfo.loadLabel(getPackageManager()));

			addItem(mData,
					info.activityInfo.loadLabel(getPackageManager()).toString(),
					activityIntent(
							info.activityInfo.applicationInfo.packageName,
							info.activityInfo.name));
		}

		Collections.sort(mData, sDisplayNameComparator);
		mAdapter.notifyDataSetChanged();

		mNoteTextView.setText("Activities in package: " + packageName);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "Activity launch failed. intent=" + intent,
					Toast.LENGTH_SHORT).show();
		}
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

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};
}
