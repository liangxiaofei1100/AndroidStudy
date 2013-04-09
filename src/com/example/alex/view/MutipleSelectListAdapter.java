package com.example.alex.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MutipleSelectListAdapter extends BaseAdapter {
	private ArrayList<String> list;
	private static HashMap<Integer, Boolean> isSelected;
	private Context context;
	private LayoutInflater inflater = null;

	public MutipleSelectListAdapter(ArrayList<String> list, Context context) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();

		initDate();
	}

	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.file_chooser_row, null);
//			holder.mTextView = (TextView) convertView.findViewById(R.id.fdrowtext);
//			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.multiple_checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTextView.setText(list.get(position));
		holder.mCheckBox.setChecked(getIsSelected().get(position));
		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		MutipleSelectListAdapter.isSelected = isSelected;
	}

	public class ViewHolder {
		private ImageView mImageView;
		private TextView mTextView;
		private CheckBox mCheckBox;
	}
}