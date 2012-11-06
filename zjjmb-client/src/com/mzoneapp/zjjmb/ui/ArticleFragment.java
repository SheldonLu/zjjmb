/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mzoneapp.zjjmb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.github.ignition.core.widgets.RemoteImageView;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Article;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Fragment that displays a news article.
 */
public class ArticleFragment extends SherlockFragment {

	View mView;
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	TextView mTitle;
	TextView mAuthor;
	TextView mDatetime;
	TextView mContent;

	// The article we are to display
	Article mArticle = null;

	// Parameterless constructor is needed by framework
	public ArticleFragment() {
		super();
	}

	/**
	 * Sets up the UI. It consists if a single WebView.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_article, null);
		mPager = (ViewPager) mView.findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) mView.findViewById(R.id.indicator);
		mTitle = (TextView) mView.findViewById(R.id.txt_title);
		mAuthor = (TextView) mView.findViewById(R.id.txt_author);
		mDatetime = (TextView) mView.findViewById(R.id.txt_datetime);
		mContent = (TextView) mView.findViewById(R.id.txt_content);
		return mView;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadArticleView();
	}

	/**
	 * Displays a particular article.
	 * 
	 * @param article
	 *            the article to display
	 */
	public void displayArticle(Article article) {
		mArticle = article;
	}

	/**
	 * Loads article data into the article view.
	 * 
	 * This method is called internally to update the webview's contents to the
	 * appropriate article's text.
	 */
	void loadArticleView() {
		if (null != mView) {
			ImageFragmentAdapter adapter = new ImageFragmentAdapter(
					getFragmentManager());
			adapter.setImages(mArticle.images);
			mPager.setAdapter(adapter);
			mAuthor.setText(mArticle.author);
			mTitle.setText(mArticle.title);
			mDatetime.setText(mArticle.issuedate);
			mContent.setText(Html.fromHtml(mArticle.content));
			mIndicator.setViewPager(mPager);
		}
	}

	public static class ImageFragmentAdapter extends FragmentPagerAdapter {

		private String[] mImages = new String[] {};

		private int mCount = 0;

		public ImageFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ImageFragment(mImages[position]);
		}

		public void setImages(String[] images) {
			mImages = images;
			mCount = mImages.length;
		}

		@Override
		public int getCount() {
			return mCount;
		}

		public static class ImageFragment extends Fragment {

			private String mImageUrl;

			public ImageFragment(String mImageUrl) {
				super();
				this.mImageUrl = mImageUrl;
			}
			
			public ImageFragment() {
				super();
			}

			@Override
			public View onCreateView(LayoutInflater inflater,
					ViewGroup container, Bundle savedInstanceState) {
				return new RemoteImageView(getActivity(), mImageUrl, true);
			}

		}

	}

}
