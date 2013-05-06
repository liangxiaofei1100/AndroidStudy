package com.example.alex.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import com.example.alex.R;

public class SearchActivity extends Activity {

	private String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		handleIntent(getIntent());
	}

	@Override
	public boolean onSearchRequested() {
		// �򿪸��������򣨵�һ������Ĭ����ӵ��������ֵ��
		startSearch(null, false, null, false);
		return true;
	}

	// �õ��������
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// �����������ֵ
			query = intent.getStringExtra(SearchManager.QUERY);
			System.out.println(query);
			// ����������¼
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			System.out.println("����ɹ�");
		}
	}
}
