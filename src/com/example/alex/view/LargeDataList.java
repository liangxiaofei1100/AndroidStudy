package com.example.alex.view;

import java.util.ArrayList;

import com.example.alex.R;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LargeDataList extends ListActivity implements OnScrollListener {
	private static final String TAG = "LargeDataList";
	private MyAdapter mAdapter;
	ArrayList<String> mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_data_list);
		ListView listView = getListView();

		listView.addFooterView(createFooter(listView));
		mData = new ArrayList<String>();
		initData(mData);

		mAdapter = new MyAdapter(this);
		mAdapter.setData(mData);

		listView.setAdapter(mAdapter);
		listView.setOnScrollListener(this);
	}

	private void initData(ArrayList<String> data) {
		for (int i = 0; i < 200; i++) {
			data.add("Item " + i);
		}
	}

	private View createFooter(ListView listView) {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setText("Loading...");
		return view;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount,
			int totalCount) {

		boolean loadMore = /* maybe add a padding */
		firstVisible + visibleCount >= totalCount;

		if (loadMore) {
			int count = mAdapter.getCount() + visibleCount;
			mAdapter.setCount(count);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	class MyAdapter extends BaseAdapter {
		ArrayList<String> data;
		int count = 40;
		LayoutInflater mInflater;

		public MyAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public void setData(ArrayList<String> data) {
			Log.d(TAG, "Data size: " + data.size());
			this.data = data;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public int getCount() {

			return count;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_2, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(android.R.id.text1);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.text.setText("Item " + position);

			return convertView;
		}

		class ViewHolder {
			TextView text;
		}

	}

}
