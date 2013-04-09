package com.example.alex.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.alex.R;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewSelectorActivity extends ListActivity {
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		ListView list = getListView();
		initData();

		final SimpleAdapter listItemAdapter = new SimpleAdapter(
				ListViewSelectorActivity.this, listItem, R.layout.item,// ListItem的XML实现
				new String[] { "more_image", "title", "date" }, new int[] {
						R.id.more_image, R.id.title, R.id.date }) {
		};

		list.setAdapter(listItemAdapter);

		TextView emptyView = (TextView) findViewById(R.id.tv_empty_list);
		list.setEmptyView(emptyView);
	}

	private void initData() {
		String[] title = { "Apple", "Google", "Facebook" };
		String[] date = { "2-12", "5-16", "9-12" };

		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("more_image", R.drawable.ic_launcher);// 图像资源的ID
			map.put("title", title[i]);
			map.put("date", date[i]);
			listItem.add(map);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		v.setBackgroundColor(getResources().getColor(R.color.selected));
	}
}