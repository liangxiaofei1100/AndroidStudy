package com.example.alex.search;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSearchProvider extends SearchRecentSuggestionsProvider {

	public final static int MODE = DATABASE_MODE_QUERIES;
	public final static String AUTHORITY = "com.example.alex.search.RecentSearchProvider";

	@Override
	public boolean onCreate() {
		setupSuggestions(AUTHORITY, MODE);
		return super.onCreate();
	}
}
