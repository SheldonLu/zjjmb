package com.mzoneapp.zjjmb.ui;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.ui.ArticleFragment.ArticleTaskListener;

public class ArticleActivity extends SherlockFragmentActivity implements
		ArticleTaskListener {

	private boolean useLogo = true;
	private boolean showHomeUp = true;

	// The news the article index for the article we are to
	// display
	int mArtIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//This has to be called before setContentView and you must use the
        //class in com.actionbarsherlock.view and NOT android.view
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
		ab.setDisplayShowTitleEnabled(true);

		// Place an ArticleFragment as our content pane
		ArticleFragment f = new ArticleFragment();
		f.setOnArticleTaskListener(this);
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, f).commit();
		// Display the correct news article on the fragment
		Bundle bd = getIntent().getExtras();
		String type = bd.getString("type");
		if(null != type){
			switch (1) {
			case 0:
				setTitle("通知公告");
				break;
			case 1:
				setTitle("局内动态");
				break;
			case 2:
				setTitle("工作动态");
				break;
			default:
				finish();
				return;
			}
			f.setArguments(bd);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_article, menu);
		return (super.onCreateOptionsMenu(menu));
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

	@Override
	public void onTaskStarted() {
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void onTaskCompleted() {
		setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onTaskFailed() {
		setSupportProgressBarIndeterminateVisibility(false);
	}

}
