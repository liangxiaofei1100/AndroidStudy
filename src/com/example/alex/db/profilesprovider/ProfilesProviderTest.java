package com.example.alex.db.profilesprovider;

import java.util.Iterator;
import java.util.Random;

import com.example.alex.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilesProviderTest extends Activity {
	private TextView mTextView;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		initView();

		showDBItems();

		getContentResolver().registerContentObserver(
				ProfilesMetaData.Profiles.CONTENT_URI, true,
				new ProfilesContentObserver(new Handler()));
		testInsert();
		testSingleItemUpdate();
		testSingleItemDelete();
	}

	class ProfilesContentObserver extends ContentObserver {

		public ProfilesContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			showDBItems();
		}

	}

	private void initView() {
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		mButton = (Button) findViewById(R.id.btn_show_result);

		mButton.setText("delete or add profile");
		mButton.setOnClickListener(new OnClickListener() {
			Random random = new Random();
			int i = random.nextInt(9);
			boolean b = random.nextBoolean();

			@Override
			public void onClick(View v) {
				if (b) {
					insertReord("profile" + i, "ringtone" + i, i);
				} else {
					testDelete("profile" + i);
				}

				i = random.nextInt(9);
				b = random.nextBoolean();
				mButton.setText(b ? "insert profile" + i : "delete profile" + i);
			}
		});
	}

	String columns[] = new String[] { ProfilesMetaData.Profiles._ID,
			ProfilesMetaData.Profiles.NAME, ProfilesMetaData.Profiles.RINGTONE,
			ProfilesMetaData.Profiles.RINGTONE_VOLUME };

	private void showDBItems() {
		mTextView.setText("");
		Uri myUri = ProfilesMetaData.Profiles.CONTENT_URI;
		Cursor cur = getContentResolver().query(myUri, columns, null, null,
				null);
		showDBItems(cur);
		cur.close();
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
			} while (cur.moveToNext());
		}
	}

	private void testDelete(String name) {
		String selection = ProfilesMetaData.Profiles.NAME + " = \"" + name
				+ "\"";
		getContentResolver().delete(ProfilesMetaData.Profiles.CONTENT_URI,
				selection, null);
	}

	private void deleteAll() {
		String selection = ProfilesMetaData.Profiles._ID + " is not " + null;
		getContentResolver().delete(ProfilesMetaData.Profiles.CONTENT_URI,
				selection, null);
	}

	private void testInsert() {
		for (int i = 0; i < 10; i++) {
			insertReord("profile" + i, "ringtone" + i, i);
		}
	}

	private Uri insertReord(String name, String ringtone, int ringtoneVolume) {
		ContentValues values = new ContentValues();
		values.put(ProfilesMetaData.Profiles.NAME, name);
		values.put(ProfilesMetaData.Profiles.RINGTONE, ringtone);
		values.put(ProfilesMetaData.Profiles.RINGTONE_VOLUME, ringtoneVolume);
		return getContentResolver().insert(ProfilesMetaData.Profiles.CONTENT_URI,
				values);
	}
	
	private void testSingleItemUpdate () {
		ContentResolver resolver = getContentResolver();
		Uri uri = insertReord("profilex", "ringtonex", 20);
		ContentValues values = new ContentValues();
		values.put(ProfilesMetaData.Profiles.RINGTONE, "ringtonex2");
		resolver.update(uri, values, null, null);
		
		
	}
	
	private void testSingleItemDelete(){
		Uri uri2 = insertReord("profiley", "ringtoney", 20);
		getContentResolver().delete(uri2, null, null);
	}

}
