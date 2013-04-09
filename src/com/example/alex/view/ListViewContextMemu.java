package com.example.alex.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.alex.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListViewContextMemu extends ListActivity {
	private static final String TAG = "ListViewContextMemu";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);

		ListView listView = getListView();

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		initData(data);

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { "item" },
				new int[] { android.R.id.text1 });

		listView.setAdapter(simpleAdapter);
		// listView.setOnCreateContextMenuListener(this);
		registerForContextMenu(listView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listview_contextmenu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo menuInfo = item.getMenuInfo();
		if (menuInfo instanceof AdapterContextMenuInfo) {
			// list view context menu.
			AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;

			Toast.makeText(
					this,
					"list item: " + adapterContextMenuInfo.position
							+ ". Select menu: " + item.getTitle(),
					Toast.LENGTH_SHORT).show();
		} else {
			// submenu context menu.
			Toast.makeText(this, "Select menu: " + item.getTitle(),
					Toast.LENGTH_SHORT).show();
		}

		return super.onContextItemSelected(item);
	}

	private void initData(ArrayList<HashMap<String, String>> data) {
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", "item " + i);
			data.add(map);
		}
	}
	
	
}
