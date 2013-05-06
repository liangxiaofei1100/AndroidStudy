package com.example.alex.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMetaData {
	public static final String DATABASE_NAME = "Favorite.db";
	public static final int DATABASE_VERSION = 1;

	public static final String AUTHORITY = "com.example.alex.db.Favorite";
	

	/**
	 *  Favorite audio table
	 */
	public static final class Audio implements BaseColumns {
		public static final String TABLE_NAME = "audio";
		public static final Uri CONTENT_URI = Uri
				.parse("content://" + AUTHORITY + "/audio");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/audio";
		public static final String AUDIO_ID = "audio_id";
	}
	
	/**
	 *  Favorite video table
	 */
	public static final class Video implements BaseColumns {
		public static final String TABLE_NAME = "video";
		public static final Uri CONTENT_URI = Uri
				.parse("content://" + AUTHORITY + "/video");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/video";
		public static final String AUDIO_ID = "video_id";
	}
}
