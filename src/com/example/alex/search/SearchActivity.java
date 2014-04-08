package com.example.alex.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.widget.Button;

import com.example.alex.R;

public class SearchActivity extends Activity {

	private String query;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		handleIntent(getIntent());
		
		mButton = (Button)findViewById(R.id.btn_show_result);
		mButton.setText("Start Search");
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
		});
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
