package com.example.alex.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.TextView;

import com.example.alex.R;

public class MultiQueryDemo extends Activity {
	private String TAG = "MultiQueryDemo";
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView) findViewById(R.id.tv_show_result);

		Cursor result = multiQuery();
		Log.d(TAG, "result cursor count: " + result.getColumnCount());
		showResult(result);
		result.close();
	}

	private Cursor multiQuery() {
		// get contact id and name;
		Uri uri = ContactsContract.Data.CONTENT_URI;
		ContentResolver resolver = getContentResolver();
		Cursor cursor1 = resolver.query(uri, new String[] {
				ContactsContract.Data.RAW_CONTACT_ID,
				ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
				ContactsContract.Data.DATA15 }, null, null, null);

		// get contact id and phone number
		Uri uri2 = ContactsContract.RawContacts.CONTENT_URI;
		Cursor cursor2 = resolver.query(uri2, new String[] {
				ContactsContract.RawContacts._ID, "display_name" }, null, null,
				null);
		Log.d(TAG, "cursor1 count: " + cursor1.getColumnCount());
		Log.d(TAG, "cursor2 count: " + cursor2.getColumnCount());

		// join cursor1 and cursor2.
		CursorJoiner joiner = new CursorJoiner(cursor1,
				new String[] { ContactsContract.Data.RAW_CONTACT_ID }, cursor2,
				new String[] { ContactsContract.RawContacts._ID });

		MatrixCursor result = new MatrixCursor(new String[] {
				ContactsContract.Data.RAW_CONTACT_ID, "display_name",
				ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
				ContactsContract.Data.DATA15 }, Math.max(
				cursor1.getColumnCount(), cursor2.getColumnCount()));
		String name = "";
		for (CursorJoiner.Result joinerResult : joiner) {
			Log.d(TAG, "joiner result: " + joinerResult);

			switch (joinerResult) {
			case BOTH:
				// handle case where a row
				String id = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
				String minetype = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.MIMETYPE));
				String data1 = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.DATA1));
				String data15 = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.DATA15));
				name = cursor2
						.getString(cursor2.getColumnIndex("display_name"));
				result.addRow(new String[] { id, name, minetype, data1, data15 });
				break;
			case LEFT:
				// handle case where a row
				String lid = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
				String lminetype = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.MIMETYPE));
				String ldata1 = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.DATA1));
				String ldata15 = cursor1.getString(cursor1
						.getColumnIndex(ContactsContract.Data.DATA15));
				result.addRow(new String[] { lid, name, lminetype, ldata1,
						ldata15 });
				break;
			case RIGHT:

				break;
			}
		}
		cursor1.close();
		cursor2.close();
		return result;
	}

	private void showResult(Cursor result) {
		while (result.moveToNext()) {
			String id = result.getString(result
					.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
			String minetype = result.getString(result
					.getColumnIndex(ContactsContract.Data.MIMETYPE));
			String data1 = result.getString(result
					.getColumnIndex(ContactsContract.Data.DATA1));
			String data15 = result.getString(result
					.getColumnIndex(ContactsContract.Data.DATA15));
			String name = result.getString(result
					.getColumnIndex("display_name"));
			mTextView.append("id: " + id + ", name: " + name + ", minitype: "
					+ minetype + ", data1: " + data1 + ", data15: " + data15
					+ "\n");
		}
	}

}
