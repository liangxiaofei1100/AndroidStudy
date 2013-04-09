package com.example.alex.db;

import java.util.Arrays;

import com.example.alex.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseTest extends Activity {
	TextView mTextView;
	Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mTextView = (TextView) findViewById(R.id.tv_show_result);
		mButton = (Button) findViewById(R.id.btn_show_result);
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteAll();
				displayRecords();
			}
		});
		Thread thread = new Thread(runnable);
		thread.start();
		displayRecords();
		mTextView.append("--------------------");
		getRecords(null);
		Long[] ids = new Long[20];
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			for(long i = 0; i < 1000; i++) {
				insertRecord(i);
			}
		}
	};

	private void getRecords(Long[] ids) {
		String columns[] = new String[] {  FavoriteMetaData.Audio._ID,
				FavoriteMetaData.Audio.AUDIO_ID };
		Uri myUri = FavoriteMetaData.Audio.CONTENT_URI;
		String selection = FavoriteMetaData.Audio.AUDIO_ID + " in (100,200) ";
		Cursor cur = getContentResolver().query(myUri, columns, selection,
				null, null);
		long id = 0;
		long audioId = 0l;

		while (cur.moveToNext()) {
			id = cur.getLong(cur.getColumnIndex(FavoriteMetaData.Audio._ID));
			audioId = cur.getLong(cur
					.getColumnIndex(FavoriteMetaData.Audio.AUDIO_ID));
			mTextView.append("id = " + id + " audioId = " + audioId + "\n");
		}
		cur.close();
	}

	private void deleteAll() {
		String selection = FavoriteMetaData.Audio.AUDIO_ID + " is not " + null;
		getContentResolver().delete(FavoriteMetaData.Audio.CONTENT_URI,
				selection, null);
	}

	private void delete(Long id) {
		String selection = FavoriteMetaData.Audio.AUDIO_ID + " = " + id;
		getContentResolver().delete(FavoriteMetaData.Audio.CONTENT_URI,
				selection, null);
	}
	

	private void insertRecordIgnoreExist(Long id) {
		ContentResolver resolver = getContentResolver();
		String columns[] = new String[] { FavoriteMetaData.Audio.AUDIO_ID };
		Uri myUri = FavoriteMetaData.Audio.CONTENT_URI;
		String selection = FavoriteMetaData.Audio.AUDIO_ID + " = " + id;
		Cursor cur = getContentResolver().query(myUri, columns, selection,
				null, null);
		if (cur.getCount() > 0) {
			// ignore
		} else {
			ContentValues values = new ContentValues();
			values.put(FavoriteMetaData.Audio.AUDIO_ID, id);
			getContentResolver().insert(FavoriteMetaData.Audio.CONTENT_URI,
					values);
		}

		cur.close();
	}

	private void insertRecord(Long id) {
		ContentValues values = new ContentValues();
		values.put(FavoriteMetaData.Audio.AUDIO_ID, id);
		getContentResolver().insert(FavoriteMetaData.Audio.CONTENT_URI, values);
	}

	private void displayRecords() {
		mTextView.setText("");
		String columns[] = new String[] { FavoriteMetaData.Audio._ID,
				FavoriteMetaData.Audio.AUDIO_ID };
		Uri myUri = FavoriteMetaData.Audio.CONTENT_URI;
		Cursor cur = getContentResolver().query(myUri, columns, null, null,
				null);
		if (cur.moveToFirst()) {
			long id = 0;
			long audioId = 0l;
			do {
				id = cur.getLong(cur.getColumnIndex(FavoriteMetaData.Audio._ID));
				audioId = cur.getLong(cur
						.getColumnIndex(FavoriteMetaData.Audio.AUDIO_ID));
				mTextView.append("id = " + id + " audioId = " + audioId + "\n");
			} while (cur.moveToNext());
		}
		cur.close();
	}
}
