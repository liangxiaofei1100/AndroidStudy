package com.example.alex.search;

import com.example.alex.R;
import com.example.alex.common.TimeUtil;

import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class ArticleListActivity extends ListActivity implements
		OnQueryTextListener, LoaderCallbacks<Cursor> {
	private static final String TAG = "ActicleListActivity";
	private ArticleAdapter mAdapter;
	private ListView mListView;
	private SearchView mSearchView;

	private static final String[] ARTICLE_PROJECTION = new String[] {
			ArticleMetaData.Article._ID, ArticleMetaData.Article.TITLE,
			ArticleMetaData.Article.AUTHOR, ArticleMetaData.Article.DATE };
	// If non-null, this is the current filter the user has provided.
	private String mCurFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articlelist_activity);
		getLoaderManager().initLoader(0, null, this);
		handleIntent(getIntent());
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handleIntent(getIntent());
	}

	/**
	 * Handle search intent.
	 * 
	 * @param intent
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchArticle(query);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Uri uri = intent.getData();
			Log.d(TAG, "Action view, uri = " + uri);
			Log.d(TAG, "article id = " + uri.getLastPathSegment());
		}
	}

	private void searchArticle(String query) {
		if (query != null && !query.isEmpty()) {
			mCurFilter = !TextUtils.isEmpty(query) ? query : null;
			getLoaderManager().restartLoader(0, null, this);
		}
	}

	private void initView() {
		mListView = getListView();
		mAdapter = new ArticleAdapter(this, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mListView.setAdapter(mAdapter);
	}

	class ArticleAdapter extends CursorAdapter {

		public ArticleAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int titleColumn = cursor
					.getColumnIndex(ArticleMetaData.Article.TITLE);
			int authorColumn = cursor
					.getColumnIndex(ArticleMetaData.Article.AUTHOR);
			int dateColumn = cursor
					.getColumnIndex(ArticleMetaData.Article.DATE);

			String title = cursor.getString(titleColumn);
			String author = cursor.getString(authorColumn);
			String date = TimeUtil.getCurrentTime(cursor.getLong(dateColumn));

			TextView titleTextView = (TextView) view
					.findViewById(R.id.tv_article_list_item_title);
			TextView authorTextView = (TextView) view
					.findViewById(R.id.tv_article_list_item_author);
			TextView dateTextView = (TextView) view
					.findViewById(R.id.tv_article_list_item_date);

			titleTextView.setText(title);
			authorTextView.setText(author);
			dateTextView.setText(date);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = getLayoutInflater().inflate(R.layout.articlelist_item,
					parent, false);
			return view;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.article_list_activity, menu);

		mSearchView = (SearchView) menu.findItem(R.id.menu_article_search)
				.getActionView();
//		mSearchView.setOnQueryTextListener(this);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_article_new:
			Intent intent = new Intent();
			intent.setClass(this, NewArticleActivity.class);
			startActivity(intent);
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;

		if (mCurFilter != null) {
			baseUri = Uri.withAppendedPath(
					ArticleMetaData.Article.CONTENT_FILTER_URI,
					Uri.encode(mCurFilter));
		} else {
			baseUri = ArticleMetaData.Article.CONTENT_URI;
		}
		return new CursorLoader(this, baseUri, ARTICLE_PROJECTION, null, null,
				ArticleMetaData.Article.SORT_ORDER_DEFAULT);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// Called when the action bar search text has changed. Update
		// the search filter, and restart the loader to do a new query
		// with this filter.
		mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
		getLoaderManager().restartLoader(0, null, this);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// Don't care about this.
		return true;
	}

}
