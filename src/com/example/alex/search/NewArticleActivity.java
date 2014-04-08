package com.example.alex.search;

import com.example.alex.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NewArticleActivity extends Activity {
	private EditText mTitleEditText;
	private EditText mAuthorEditText;
	private EditText mContentEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_new);

		initView();
	}

	private void initView() {
		mTitleEditText = (EditText) findViewById(R.id.et_article_new_title);
		mAuthorEditText = (EditText) findViewById(R.id.et_article_new_autor);
		mContentEditText = (EditText) findViewById(R.id.et_article_new_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.article_new_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_article_save:
			saveArticle();
			finish();
			return true;
		case R.id.menu_article_delete:

			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveArticle() {
		String title = mTitleEditText.getText().toString();
		String author = mAuthorEditText.getText().toString();
		String content = mContentEditText.getText().toString();

		ContentValues values = new ContentValues();
		values.put(ArticleMetaData.Article.TITLE, title);
		values.put(ArticleMetaData.Article.AUTHOR, author);
		values.put(ArticleMetaData.Article.CONTENT, content);
		values.put(ArticleMetaData.Article.DATE, System.currentTimeMillis());

		ContentResolver contentResolver = getContentResolver();
		contentResolver.insert(ArticleMetaData.Article.CONTENT_URI, values);
	}
}
