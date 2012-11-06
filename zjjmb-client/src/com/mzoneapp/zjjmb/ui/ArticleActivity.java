package com.mzoneapp.zjjmb.ui;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Article;

public class ArticleActivity extends SherlockFragmentActivity {

	private boolean useLogo = true;
	private boolean showHomeUp = true;

	// The news category index and the article index for the article we are to
	// display
	int mCatIndex, mArtIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCatIndex = getIntent().getExtras().getInt("catIndex", 0);
		mArtIndex = getIntent().getExtras().getInt("artIndex", 0);

		// If we are in two-pane layout mode, this activity is no longer
		// necessary
		if (getResources().getBoolean(R.bool.has_two_panes)) {
			finish();
			return;
		}

		final ActionBar ab = getSupportActionBar();
		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(showHomeUp);
		ab.setDisplayUseLogoEnabled(useLogo);

		// Place an ArticleFragment as our content pane
		ArticleFragment f = new ArticleFragment();
		Article article = new Article();
		article.author = "XXXXXXXXXXXXXXXX";
		article.title = "YYYYYYYYYYYYYYYYYYYYYYYYYYYYY";
		article.issuedate = "2012-10-11";
		article.images = new String[] {
				"http://farm8.staticflickr.com/7017/6589270313_1236f3546f.jpg",
				"http://farm8.staticflickr.com/7006/6647956613_a9dcecafeb.jpg",
				"http://farm8.staticflickr.com/7143/6621178411_e52b6ab043.jpg" };
		article.content = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
		f.displayArticle(article);
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, f).commit();
		// Display the correct news article on the fragment

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_article, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
