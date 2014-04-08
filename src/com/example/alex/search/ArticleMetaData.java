package com.example.alex.search;

import android.net.Uri;
import android.provider.BaseColumns;

public class ArticleMetaData {
	public static final String DATABASE_NAME = "article.db";
	public static final int DATABASE_VERSION = 1;

	public static final String AUTHORITY = "com.example.alex.search.article";

	/**
	 * Article table
	 */
	public static final class Article implements BaseColumns {
		public static final String TABLE_NAME = "article";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);
		public static final Uri CONTENT_FILTER_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + "article_filter");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/article";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/article";

		// Columns
		/** Article title. Type: String. */
		public static final String TITLE = "title";
		/** Article author. Type: String. */
		public static final String AUTHOR = "author";
		/** Article content. Type: String. */
		public static final String CONTENT = "content";
		/** Article write/modified date. Type: Long. */
		public static final String DATE = "date";
		
		/** Order by _ID ASC */
		public static final String SORT_ORDER_DEFAULT = DATE + " DESC";
	}
}
