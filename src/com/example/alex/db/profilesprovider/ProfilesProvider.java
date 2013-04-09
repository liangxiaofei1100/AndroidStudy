package com.example.alex.db.profilesprovider;

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

public class ProfilesProvider extends ContentProvider {
	private static final String TAG = "ProfilesProvider";

	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDatabaseHelper;

	public static final int PROFILES_COLLECTION = 1;
	public static final int PROFILES_SINGLE = 2;

	public static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ProfilesMetaData.AUTHORITY, "profiles",
				PROFILES_COLLECTION);
		uriMatcher.addURI(ProfilesMetaData.AUTHORITY, "profiles/#",
				PROFILES_SINGLE);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, ProfilesMetaData.DATABASE_NAME, null,
					ProfilesMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("Create table " + ProfilesMetaData.Profiles.TABLE_NAME
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ProfilesMetaData.Profiles.NAME + " TEXT UNIQUE, "
					+ ProfilesMetaData.Profiles.ENABLED + " BOOLEAN, "
					+ ProfilesMetaData.Profiles.RINGTONE + " TEXT, "
					+ ProfilesMetaData.Profiles.RINGTONE_VOLUME + " INTEGER, "
					+ ProfilesMetaData.Profiles.RING_VIBRATION + " INTERGER, "
					+ ProfilesMetaData.Profiles.NOTIFICATION_TONE + " TEXT, "
					+ ProfilesMetaData.Profiles.NOTIFICATION_VOLUME
					+ " INTERGER, " + ProfilesMetaData.Profiles.MEDIA_VOLUME
					+ " INTERGER, " + ProfilesMetaData.Profiles.AlARM_VOLUME
					+ " INTERGER);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ ProfilesMetaData.Profiles.TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case PROFILES_COLLECTION:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			count = mDatabase.delete(ProfilesMetaData.Profiles.TABLE_NAME,
					selection, selectionArgs);
			break;
		case PROFILES_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			String segment = uri.getPathSegments().get(1);
            if (selection != null && segment.length() > 0) {
            	selection = "_id=" + segment + " AND (" + selection + ")";
            } else {
            	selection = "_id=" + segment;
            }
            count = mDatabase.delete(ProfilesMetaData.Profiles.TABLE_NAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}
		
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case PROFILES_COLLECTION:
			return ProfilesMetaData.Profiles.CONTENT_TYPE;
		case PROFILES_SINGLE:
			return ProfilesMetaData.Profiles.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}
	}

	/**
	 * Replace if conflict.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		switch (uriMatcher.match(uri)) {
		case PROFILES_COLLECTION:
		case PROFILES_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			long rowId = mDatabase.insertWithOnConflict(
					ProfilesMetaData.Profiles.TABLE_NAME, "", contentvalues,
					SQLiteDatabase.CONFLICT_REPLACE);
			if (rowId > 0) {
				Uri rowUri = ContentUris.withAppendedId(
						ProfilesMetaData.Profiles.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(rowUri, null);
				return rowUri;
			}
			throw new IllegalArgumentException("Cannot insert into uri: " + uri);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
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
		case PROFILES_COLLECTION:
			qb.setTables(ProfilesMetaData.Profiles.TABLE_NAME);
			break;
		case PROFILES_SINGLE:
			qb.setTables(ProfilesMetaData.Profiles.TABLE_NAME);
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
		case PROFILES_SINGLE:
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update(ProfilesMetaData.Profiles.TABLE_NAME, values, "_id="
					+ rowId, null);
			break;
		default:
			throw new UnsupportedOperationException("Cannot update URL: " + url);

		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}
}
