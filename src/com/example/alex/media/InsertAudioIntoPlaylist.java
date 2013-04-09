package com.example.alex.media;

import java.io.File;
import java.util.HashSet;

import com.example.alex.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

public class InsertAudioIntoPlaylist extends Activity {
	private final String TAG = "InsertAudioIntoPlaylist";
	private TextView mResulTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		mResulTextView = (TextView) findViewById(R.id.tv_show_result);

		// show all audio info.
		mResulTextView.append("External Audio:\n");
		mResulTextView
				.append(showAllAudioInfo(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,this));
		mResulTextView.append("Internal Audio:\n");
		mResulTextView
				.append(showAllAudioInfo(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,this));

		// show all audio play list.
		mResulTextView.append("play list:\n");
		mResulTextView.append(getPlaylist());

		// get audio id of file "/mnt/sdcard/because of you.mp3"
		String audioFile = "/mnt/sdcard/because of you.mp3";
		mResulTextView.append("audio id of " + audioFile + " is:\n");
		mResulTextView.append(getAudioId(audioFile, this));
		
		// show all audio folder
		mResulTextView.append("All audio folder:\n");
		for (String folder : getAllAudioFolder(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, this)) {
			mResulTextView.append("Audio folder: " + folder + "\n");
		}
	}

	public static String[] getAllAudioFolder(Uri uri, Context context) {
		StringBuilder stringBuilder = new StringBuilder();
		ContentResolver resolver = context.getContentResolver();
		HashSet<String> audioFolder = new HashSet<String>();
		
		String[] cols = new String[] { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };
		Cursor cursor = resolver.query(uri, cols, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
			String tilte = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			String data = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			stringBuilder.append("id = " + id + "\n");
			stringBuilder.append("title = " + tilte + "\n");
			stringBuilder.append("data = " + data + "\n");
			audioFolder.add(new File(data).getParent());
		}
		String[] result = new String[audioFolder.size()];
		return audioFolder.toArray(result);
	}
	/**
	 * 
	 * @param uri
	 *            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI or
	 *            MediaStore.Audio.Media.INTERNAL_CONTENT_URI
	 * @return
	 */
	public static String showAllAudioInfo(Uri uri, Context context) {
		StringBuilder stringBuilder = new StringBuilder();

		ContentResolver resolver = context.getContentResolver();

		String[] cols = new String[] { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };
		Cursor cursor = resolver.query(uri, cols, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
			String tilte = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			String data = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			stringBuilder.append("id = " + id + "\n");
			stringBuilder.append("title = " + tilte + "\n");
			stringBuilder.append("data = " + data + "\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * get all audio play list
	 * @return
	 */
	private String getPlaylist() {
		StringBuilder stringBuilder = new StringBuilder();
		ContentResolver resolver = getContentResolver();

		Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
		String[] cols = new String[] { MediaStore.Audio.Playlists._ID,
				MediaStore.Audio.Playlists.NAME };
		Cursor cursor = resolver.query(uri, cols, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
			String tilte = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
			stringBuilder.append("id = " + id + "\n");
			stringBuilder.append("title = " + tilte + "\n");
		}
		return stringBuilder.toString();
	}

	public static String getAudioId(String audioFile, Context context) {
		StringBuilder stringBuilder = new StringBuilder();

		ContentResolver resolver = context.getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] cols = new String[] { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };

		String selection = MediaStore.Audio.Media.DATA + " like ?";
		String[] selectionArgs = new String[] { audioFile };

		Cursor cursor = resolver.query(uri, cols, selection, selectionArgs,
				null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
			String data = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			String tilte = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			stringBuilder.append("Got audio in media store:\n");
			stringBuilder.append("id = " + id + "\n");
			stringBuilder.append("data = " + data + "\n");
			stringBuilder.append("title = " + tilte + "\n");
		}
		return stringBuilder.toString();
	}

	private void insetFileToMediaPlayer(String file) {
		ContentResolver resolver = getContentResolver();
		File sourceFile = new File(file);
		ContentValues newValues = new ContentValues();
		String title = file;
		newValues.put(MediaStore.Audio.Media.TITLE, title);
		newValues
				.put(MediaStore.Audio.Media.DISPLAY_NAME, sourceFile.getName());
		newValues.put(MediaStore.Audio.Media.DATA, sourceFile.getPath());
		Uri uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				newValues);
	}
}
