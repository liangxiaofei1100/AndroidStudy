package com.example.alex.mms;

import com.example.alex.R;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ConversationList extends ListActivity {
	private static final String TAG = "ConversationList";
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mms_conversationlist);
		mListView = getListView();
		ConversationAdapter conversationAdapter = new ConversationAdapter(this,
				android.R.layout.simple_list_item_2, null, null, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mListView.setAdapter(conversationAdapter);
		
	}

	class ConversationAdapter extends SimpleCursorAdapter {

		public ConversationAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}

	}
}
