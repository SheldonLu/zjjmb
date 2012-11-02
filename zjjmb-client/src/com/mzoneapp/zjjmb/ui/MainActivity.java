package com.mzoneapp.zjjmb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.mzoneapp.zjjmb.R;

public class MainActivity extends SherlockFragmentActivity implements
		HeadlinesFragment.OnHeadlineSelectedListener, ActionBar.TabListener,
		CompatActionBarNavListener {

	private boolean useLogo = true;
	private boolean showHomeUp = false;

	private HeadlinesFragment mHeadlinesFragment = null;
	private ArticleFragment mArticleFragment = null;

	// Whether or not we are in dual-panel mode
	boolean mIsDualPane = false;

	// The news category and article index currently being displayed
	int mCatIndex;
	int mArtIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar ab = getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(showHomeUp);
		ab.setDisplayUseLogoEnabled(useLogo);
		// ab.setLogo(R.drawable.ic_stat_android);

		// set up tabs nav
		ab.addTab(ab.newTab().setText("通知公告").setTabListener(this));
		ab.addTab(ab.newTab().setText("局内动态").setTabListener(this));
		ab.addTab(ab.newTab().setText("工作动态").setTabListener(this));

		// default to tab navigation
		showTabsNav();

		// find our fragments
		mHeadlinesFragment = (HeadlinesFragment) getSupportFragmentManager()
				.findFragmentById(R.id.headlines);
		mArticleFragment = (ArticleFragment) getSupportFragmentManager()
				.findFragmentById(R.id.article);

		// Determine whether we are in single-pane or dual-pane mode by testing
		// the visibility
		// of the article view.
		View articleView = findViewById(R.id.article);
		mIsDualPane = articleView != null
				&& articleView.getVisibility() == View.VISIBLE;

		// Register ourselves as the listener for the headlines fragment events.
		mHeadlinesFragment.setOnHeadlineSelectedListener(this);

		// Set up headlines fragment
		mHeadlinesFragment.setSelectable(mIsDualPane);
		restoreSelection(savedInstanceState);

	}

	/** Restore category/article selection from saved state. */
	void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setNewsCategory(savedInstanceState.getInt("catIndex", 0));
			if (mIsDualPane) {
				int artIndex = savedInstanceState.getInt("artIndex", 0);
				mHeadlinesFragment.setSelection(artIndex);
				onHeadlineSelected(artIndex);
			}
		}
	}

	/**
	 * Sets the displayed news category.
	 * 
	 * This causes the headlines fragment to be repopulated with the appropriate
	 * headlines.
	 */
	void setNewsCategory(int catIndex) {
		mCatIndex = catIndex;
		mHeadlinesFragment.loadCategory(catIndex);

		// If we are displaying the article on the right, we have to update that
		// too
		if (mIsDualPane) {
			mArticleFragment.displayArticle(null);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setNewsCategory(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void showTabsNav() {
		ActionBar ab = getSupportActionBar();
		if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
			ab.setDisplayShowTitleEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		onCategorySelected(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onHeadlineSelected(int index) {
		mArtIndex = index;
		if (mIsDualPane) {
			// display it on the article fragment
			mArticleFragment.displayArticle(null);
		} else {
			// use separate activity
			Intent i = new Intent(this, NewsArticleActivity.class);
			i.putExtra("catIndex", mCatIndex);
			i.putExtra("artIndex", index);
			startActivity(i);
		}
	}

	@Override
	public void onCategorySelected(int catIndex) {
		setNewsCategory(catIndex);
	}

	/** Save instance state. Saves current category/article index. */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("catIndex", mCatIndex);
		outState.putInt("artIndex", mArtIndex);
		super.onSaveInstanceState(outState);
	}

}
