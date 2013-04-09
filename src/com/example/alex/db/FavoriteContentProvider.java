package com.example.alex.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FavoriteContentProvider extends ContentProvider {
	private static final String TAG = "FavoriteContentProvider";

	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDatabaseHelper;

	public static final int FAVORITE_AUDIO_COLLECTION = 1;
	public static final int FAVORITE_AUDIO_SINGLE = 2;

	public static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(FavoriteMetaData.AUTHORITY, "audio",
				FAVORITE_AUDIO_COLLECTION);
		uriMatcher.addURI(FavoriteMetaData.AUTHORITY, "audio/#",
				FAVORITE_AUDIO_SINGLE);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, FavoriteMetaData.DATABASE_NAME, null,
					FavoriteMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("Create table "
					+ FavoriteMetaData.Audio.TABLE_NAME
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ FavoriteMetaData.Audio.AUDIO_ID + " LONG UNIQUE);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ FavoriteMetaData.Audio.TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case FAVORITE_AUDIO_COLLECTION:
		case FAVORITE_AUDIO_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			int count = mDatabase.delete(
					FavoriteMetaData.Audio.TABLE_NAME, selection,
					selectionArgs);
			return count;
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case FAVORITE_AUDIO_COLLECTION:
			return FavoriteMetaData.Audio.CONTENT_TYPE;
		case FAVORITE_AUDIO_SINGLE:
			return FavoriteMetaData.Audio.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		switch (uriMatcher.match(uri)) {
		case FAVORITE_AUDIO_COLLECTION:
		case FAVORITE_AUDIO_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			long rowId = mDatabase.insertWithOnConflict(
					FavoriteMetaData.Audio.TABLE_NAME, "",
					contentvalues, SQLiteDatabase.CONFLICT_REPLACE);
			if (rowId > 0) {
				Uri rowUri = ContentUris.withAppendedId(
						FavoriteMetaData.Audio.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(rowUri, null);
				return rowUri;
			}
			throw new IllegalArgumentException("Cannot insert into uri: " + uri);
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}

	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return (mDatabaseHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		// Generate the body of the query
		int match = uriMatcher.match(uri);
		switch (match) {
		case FAVORITE_AUDIO_COLLECTION:
			qb.setTables(FavoriteMetaData.Audio.TABLE_NAME);
			break;
		case FAVORITE_AUDIO_SINGLE:
			qb.setTables(FavoriteMetaData.Audio.TABLE_NAME);
			qb.appendWhere("_id=");
			qb.appendWhere(uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		Cursor ret = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);

		if (ret != null) {
			ret.setNotificationUri(getContext().getContentResolver(), uri);
		}

		return ret;
	}

	@Override
	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		long rowId = 0;
		int match = uriMatcher.match(url);
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		switch (match) {
		case FAVORITE_AUDIO_SINGLE:
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("alarms", values, "_id=" + rowId, null);
			break;
		default:
			throw new UnsupportedOperationException("Cannot update URL: " + url);

		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

}
