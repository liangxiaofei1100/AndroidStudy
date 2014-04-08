package com.example.alex.search;

import java.util.HashMap;

import android.app.SearchManager;
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
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class ArticleProvider extends ContentProvider {
	private static final String TAG = "ArticleProvider";

	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDatabaseHelper;

	public static final int ARTICLE_COLLECTION = 1;
	public static final int ARTICLE_SINGLE = 2;
	public static final int ARTICLE_FILTER = 3;
	public static final int SEARCH_SUGGEST = 4;

	public static final UriMatcher mUriMatcher;

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(ArticleMetaData.AUTHORITY, "article",
				ARTICLE_COLLECTION);
		mUriMatcher.addURI(ArticleMetaData.AUTHORITY, "article/#",
				ARTICLE_SINGLE);
		mUriMatcher.addURI(ArticleMetaData.AUTHORITY, "article_filter/*",
				ARTICLE_FILTER);
		// to get suggestions...
		mUriMatcher.addURI(ArticleMetaData.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		mUriMatcher.addURI(ArticleMetaData.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, ArticleMetaData.DATABASE_NAME, null,
					ArticleMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + ArticleMetaData.Article.TABLE_NAME
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ArticleMetaData.Article.TITLE + " TEXT UNIQUE, "
					+ ArticleMetaData.Article.AUTHOR + " TEXT, "
					+ ArticleMetaData.Article.CONTENT + " TEXT, "
					+ ArticleMetaData.Article.DATE + " INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ ArticleMetaData.Article.TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (mUriMatcher.match(uri)) {
		case ARTICLE_COLLECTION:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			count = mDatabase.delete(ArticleMetaData.Article.TABLE_NAME,
					selection, selectionArgs);
			break;
		case ARTICLE_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			String segment = uri.getPathSegments().get(1);
			if (selection != null && segment.length() > 0) {
				selection = "_id=" + segment + " AND (" + selection + ")";
			} else {
				selection = "_id=" + segment;
			}
			count = mDatabase.delete(ArticleMetaData.Article.TABLE_NAME,
					selection, selectionArgs);
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
		switch (mUriMatcher.match(uri)) {
		case ARTICLE_COLLECTION:
			return ArticleMetaData.Article.CONTENT_TYPE;
		case ARTICLE_SINGLE:
			return ArticleMetaData.Article.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("UnKnown URI" + uri);
		}
	}

	/**
	 * Replace if conflict.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		switch (mUriMatcher.match(uri)) {
		case ARTICLE_COLLECTION:
		case ARTICLE_SINGLE:
			mDatabase = mDatabaseHelper.getWritableDatabase();
			long rowId = mDatabase.insertWithOnConflict(
					ArticleMetaData.Article.TABLE_NAME, "", contentvalues,
					SQLiteDatabase.CONFLICT_REPLACE);
			if (rowId > 0) {
				Uri rowUri = ContentUris.withAppendedId(
						ArticleMetaData.Article.CONTENT_URI, rowId);
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
		int match = mUriMatcher.match(uri);
		Log.d(TAG, "query(), match = " + match);
		switch (match) {
		case ARTICLE_COLLECTION:
			qb.setTables(ArticleMetaData.Article.TABLE_NAME);
			break;
		case ARTICLE_SINGLE:
			qb.setTables(ArticleMetaData.Article.TABLE_NAME);
			qb.appendWhere("_id=");
			qb.appendWhere(uri.getPathSegments().get(1));
			break;
		case ARTICLE_FILTER:
			qb.setTables(ArticleMetaData.Article.TABLE_NAME);

			qb.appendWhere(ArticleMetaData.Article.TITLE + " like \'%"
					+ uri.getPathSegments().get(1) + "%\'");
			qb.appendWhere(" or ");

			qb.appendWhere(ArticleMetaData.Article.AUTHOR + " like \'%"
					+ uri.getPathSegments().get(1) + "%\'");
			qb.appendWhere(" or ");

			qb.appendWhere(ArticleMetaData.Article.CONTENT + " like \'%"
					+ uri.getPathSegments().get(1) + "%\'");
			break;
		case SEARCH_SUGGEST:
			if (selectionArgs == null) {
				throw new IllegalArgumentException(
						"selectionArgs must be provided for the Uri: " + uri);
			}
			return getSuggestions(uri, projection, selection, selectionArgs,
					sortOrder);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		Cursor ret = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);

		return ret;
	}

	private static final HashMap<String, String> mColumnMap = buildColumnMap();
	private static final String[] columns = new String[] { BaseColumns._ID,
			ArticleMetaData.Article.TITLE, ArticleMetaData.Article.AUTHOR,
			ArticleMetaData.Article.DATE };

	private Cursor getSuggestions(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "getSuggestions()  uri = " + uri + ", selection = "
				+ selection + "selectionArgs[0] = " + selectionArgs[0]);
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ArticleMetaData.Article.TABLE_NAME);
		qb.setProjectionMap(mColumnMap);
		if (!TextUtils.isEmpty(selectionArgs[0])) {
			Log.d(TAG, "selection is not empty.");
			qb.appendWhere(ArticleMetaData.Article.TITLE + " like \'%"
					+ selectionArgs[0] + "%\'");
			qb.appendWhere(" or ");

			qb.appendWhere(ArticleMetaData.Article.AUTHOR + " like \'%"
					+ selectionArgs[0] + "%\'");
			qb.appendWhere(" or ");

			qb.appendWhere(ArticleMetaData.Article.CONTENT + " like \'%"
					+ selectionArgs[0] + "%\'");
		}

		Cursor ret = qb.query(db, columns, null, null, null, null, null);
		return ret;
	}

	private static HashMap<String, String> buildColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseColumns._ID, BaseColumns._ID);
//		map.put(BaseColumns._ID, BaseColumns._ID + " AS "
//				+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
		map.put(ArticleMetaData.Article.TITLE, ArticleMetaData.Article.TITLE
				+ " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
		map.put(ArticleMetaData.Article.AUTHOR, ArticleMetaData.Article.AUTHOR
				+ " AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
		map.put(ArticleMetaData.Article.DATE, ArticleMetaData.Article.DATE);
		return map;
	}

	@Override
	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		long rowId = 0;
		int match = mUriMatcher.match(url);
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		switch (match) {
		case ARTICLE_SINGLE:
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update(ArticleMetaData.Article.TABLE_NAME, values,
					"_id=" + rowId, null);
			break;
		default:
			throw new UnsupportedOperationException("Cannot update URL: " + url);

		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}
}
