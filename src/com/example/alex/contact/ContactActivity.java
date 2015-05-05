package com.example.alex.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.TextView;

public class ContactActivity extends Activity {

	private String TAG = "ContactActivity";
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTextView = new TextView(this);
		setContentView(mTextView);

		Uri uri = Contacts.CONTENT_URI;
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		while (cursor.moveToNext()) {
			int contractID = cursor.getInt(0);
			StringBuilder sb = new StringBuilder("contractID=");
			sb.append(contractID);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contractID + "/data");
			Cursor cursor1 = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			while (cursor1.moveToNext()) {
				String data1 = cursor1.getString(cursor1
						.getColumnIndex("data1"));
				String mimeType = cursor1.getString(cursor1
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(mimeType)) {
					// if (data1.contains("T"))
					sb.append(",name=" + data1);
				} else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) {
					sb.append(",email=" + data1);
				} else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) {
					sb.append(",phone=" + data1);
					Pattern pattern = Pattern.compile("");
					Matcher matcher = pattern.matcher("");
					matcher.matches();
					matcher.find();
				}
			}
			cursor1.close();
			Log.i(TAG, sb.toString());
			mTextView.append(sb.toString() + "\n");
		}
		cursor.close();

	}
}
