package com.mzoneapp.zjjmb.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.ApiConstants;

public class MainActivity extends SherlockFragmentActivity implements
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

	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		// TODO: 更改创建时间
		ApiConstants.createInstance();

		final ActionBar ab = getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(showHomeUp);
		ab.setDisplayUseLogoEnabled(useLogo);
		// ab.setLogo(R.drawable.ic_stat_android);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		Bundle bundle = new Bundle();
		bundle.putString(ApiConstants.TYPE, ApiConstants.ANNOUNCEMENT);
		mTabsAdapter.addTab(ab.newTab().setText("通知公告"),
				HeadlinesFragment.class, bundle);
		bundle = new Bundle();
		bundle.putString(ApiConstants.TYPE, ApiConstants.IN_DYNAMIC);
		mTabsAdapter.addTab(ab.newTab().setText("局内动态"),
				HeadlinesFragment.class, bundle);
		bundle = new Bundle();
		bundle.putString(ApiConstants.TYPE, ApiConstants.WORK_DYNAMIC);
		mTabsAdapter.addTab(ab.newTab().setText("工作动态"),
				HeadlinesFragment.class, bundle);

		// set up tabs nav
		// ab.addTab(ab.newTab().setText("通知公告").setTabListener(this));
		// ab.addTab(ab.newTab().setText("局内动态").setTabListener(this));
		// ab.addTab(ab.newTab().setText("工作动态").setTabListener(this));

		// default to tab navigation
		showTabsNav();

		// find our fragments
		mArticleFragment = (ArticleFragment) getSupportFragmentManager()
				.findFragmentById(R.id.article);

		// Determine whether we are in single-pane or dual-pane mode by testing
		// the visibility
		// of the article view.
		View articleView = findViewById(R.id.article);
		mIsDualPane = articleView != null
				&& articleView.getVisibility() == View.VISIBLE;

		// Register ourselves as the listener for the headlines fragment events.
		// mHeadlinesFragment.setOnHeadlineSelectedListener(this);

		// Set up headlines fragment
		// mHeadlinesFragment.setSelectable(mIsDualPane);
		// restoreSelection(savedInstanceState);

	}

	/** Restore category/article selection from saved state. */
	void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setNewsCategory(savedInstanceState.getInt("catIndex", 0));
			if (mIsDualPane) {
				int artIndex = savedInstanceState.getInt("artIndex", 0);
				mHeadlinesFragment.setSelection(artIndex);
				// onHeadlineSelected(artIndex);
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
		// mHeadlinesFragment.loadCategory(catIndex);

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

	// @Override
	// public void onHeadlineSelected(int index) {
	// mArtIndex = index;
	// if (mIsDualPane) {
	// // display it on the article fragment
	// mArticleFragment.displayArticle(null);
	// } else {
	// // use separate activity
	// Intent i = new Intent(this, ArticleActivity.class);
	// i.putExtra("catIndex", mCatIndex);
	// i.putExtra("artIndex", index);
	// startActivity(i);
	// }
	// }

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

	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
//		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
//
//		static final class TabInfo {
//			private final Class<?> clss;
//			private final Bundle args;
//
//			TabInfo(Class<?> _class, Bundle _args) {
//				clss = _class;
//				args = _args;
//			}
//		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
//			TabInfo info = new TabInfo(clss, args);
			Fragment fragment = Fragment.instantiate(mContext, clss.getName(),args);
			tab.setTag(fragment);
			tab.setTabListener(this);
			mFragments.add(fragment);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mFragments.size(); i++) {
				if (mFragments.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}

}
