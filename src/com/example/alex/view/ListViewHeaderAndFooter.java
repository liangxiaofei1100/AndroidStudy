package com.example.alex.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.alex.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewHeaderAndFooter extends ListActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_header_footer);
		ListView listView = getListView();

		listView.addHeaderView(createHeader(listView));
		listView.addFooterView(createFooter(listView));

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		initData(data);

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { "item" },
				new int[] { android.R.id.text1 });
		
		listView.setAdapter(simpleAdapter);
	}

	private void initData(ArrayList<HashMap<String, String>> data) {
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", "item " + i);
			data.add(map);
		}
	}

	private View createHeader(ListView listView) {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater
				.inflate(android.R.layout.simple_list_item_1, null);
		TextView textView = (TextView) view
				.findViewById(android.R.id.text1);
		textView.setText("Header");
		return view;
	}

	private View createFooter(ListView listView) {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater
				.inflate(android.R.layout.simple_list_item_1, null);
		TextView textView = (TextView) view
				.findViewById(android.R.id.text1);
		textView.setText("Footer");
		return view;
	}

}
