package com.example.alex.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.alex.R;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MulitiChoiceListViewActivity extends ListActivity implements
		LoaderCallbacks<Cursor> {
	private static final String TAG = "MulitiChoiceListViewActivity";
	private SimpleCursorAdapter mAdapter;
	private ArrayList<Integer> mCheckedList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoaderManager manager = getLoaderManager();
		manager.initLoader(0, null, this);
		mAdapter = new MyAdapter(this, R.layout.mulit_choice_list_item,
				null, new String[] { Contacts.DISPLAY_NAME,
						Contacts.CONTACT_STATUS }, new int[] {
						R.id.multiple_title, R.id.multiple_summary }, 0);
		setContentView(R.layout.listview_main);
		setListAdapter(mAdapter);
		
		mCheckedList = new ArrayList<Integer>();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (mCheckedList.contains(position)) {
			mCheckedList.remove(Integer.valueOf(position));
		} else {
			mCheckedList.add(position);
		}
		getListView().invalidateViews();
		
	}

	class MyAdapter extends SimpleCursorAdapter {

		public MyAdapter(Context context, int layout, Cursor c, String[] from,
				int[] to, int flags) {
			super(context, layout, c, from, to, flags);

		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.mCheckBox.setChecked(mCheckedList.contains(cursor.getPosition()));
			Log.d(TAG, "bindview: checkbox change: cursor position: " + cursor.getPosition());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = super.newView(context, cursor, parent);
			ViewHolder holder = new ViewHolder();
			holder.mCheckBox = (CheckBox) view.findViewById(R.id.multiple_checkbox);
			holder.mNoteTitleTextView =  (TextView) view.findViewById(R.id.multiple_summary);
			view.setTag(holder);
			return view;
		}

		class ViewHolder {
			TextView mNoteTitleTextView;
			CheckBox mCheckBox;
		}

	}

	// These are the Contacts rows that we will retrieve.
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS,
			Contacts.CONTACT_PRESENCE, Contacts.PHOTO_ID, Contacts.LOOKUP_KEY, };

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri;
		baseUri = Contacts.CONTENT_URI;

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ))";
		return new CursorLoader(this, baseUri, CONTACTS_SUMMARY_PROJECTION,
				select, null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
}
