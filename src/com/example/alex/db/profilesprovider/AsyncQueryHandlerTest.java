package com.example.alex.db.profilesprovider;

import com.example.alex.R;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AsyncQueryHandlerTest extends Activity {
	private static final String TAG = "AsyncQueryHandler";
	private AsyncQueryHandler mAsyncQueryHandler;
	private static final int TOKEN_QUERY_PROFILES = 1;

	String columns[] = new String[] { ProfilesMetaData.Profiles._ID,
			ProfilesMetaData.Profiles.NAME, ProfilesMetaData.Profiles.RINGTONE,
			ProfilesMetaData.Profiles.RINGTONE_VOLUME };
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView) findViewById(R.id.tv_show_result);

		mAsyncQueryHandler = new MyHandler(getContentResolver());
		mAsyncQueryHandler.startQuery(TOKEN_QUERY_PROFILES, null,
				ProfilesMetaData.Profiles.CONTENT_URI, columns, null, null,
				null);
	}

	class MyHandler extends AsyncQueryHandler {

		public MyHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			showDBItems(cursor);
		}

	}

	private void showDBItems(Cursor cur) {
		if (cur.moveToFirst()) {
			long id = 0;
			String profileName = "";
			String ringtone = "";
			int ringtoneVolume = 0;
			do {
				id = cur.getLong(cur
						.getColumnIndex(ProfilesMetaData.Profiles._ID));
				profileName = cur.getString(cur
						.getColumnIndex(ProfilesMetaData.Profiles.NAME));
				ringtone = cur.getString(cur
						.getColumnIndex(ProfilesMetaData.Profiles.RINGTONE));
				ringtoneVolume = cur
						.getInt(cur
								.getColumnIndex(ProfilesMetaData.Profiles.RINGTONE_VOLUME));
				mTextView.append("id = " + id + " name = " + profileName
						+ ", ringtone: " + ringtone + ", ringtone volume: "
						+ ringtoneVolume + "\n");
				Log.d(TAG, "id = " + id + " name = " + profileName
						+ ", ringtone: " + ringtone + ", ringtone volume: "
						+ ringtoneVolume + "\n");
			} while (cur.moveToNext());
		}
	}
}
