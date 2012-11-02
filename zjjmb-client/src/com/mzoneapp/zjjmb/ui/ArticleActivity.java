package com.mzoneapp.zjjmb.ui;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mzoneapp.zjjmb.R;

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
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, f).commit();
		// Display the correct news article on the fragment
		f.displayArticle(null);

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
