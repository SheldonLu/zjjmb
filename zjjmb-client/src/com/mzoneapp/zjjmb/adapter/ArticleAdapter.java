package com.mzoneapp.zjjmb.adapter;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.github.ignition.core.adapters.EndlessListAdapter;
import com.github.ignition.core.widgets.RemoteImageView;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Article;

public class ArticleAdapter extends EndlessListAdapter<Article> {
	private static final String LOG_TAG = ArticleAdapter.class.getName();
	private final LayoutInflater inflater;
	private long lastUpdate = -1;

	public ArticleAdapter(Activity activity,AbsListView listView) {
		super(activity, listView, R.layout.loading_item);

		inflater = LayoutInflater.from(activity);
	}

	private List<Article> headlines;
	private boolean endReached = false;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			if (msg.what >= 0) {
//				if (headlines != null) {
//					lastUpdate = System.currentTimeMillis();
//					remove(null);
//					for (Headline s : headlines) {
//						if (getPosition(s) < 0) {
//							add(s);
//						}
//					}
//					if (!endReached) {
//						add(null);
//					}
//				}
//				// TODO:callback
//
//			} else {
//				// TODO:error
//			}
		}
	};

	public void addMoreHeadlines(final String url, final int count) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getMoreHeadlines(url, count)) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	private boolean getMoreHeadlines(String url, int count) {
		// TODO
		return true;
	}

	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_headline, parent,
					false);
		}

		Article article = getItem(position);

		RemoteImageView icon = (RemoteImageView) convertView.findViewById(R.id.image);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);
		TextView date = (TextView) convertView.findViewById(R.id.dateText);

		title.setText(article.title);
//		desc.setText(headline.desc);
		desc.setText(article.desc);
		date.setText(article.issuedate.split(" ")[0]);

		return convertView;
	}
}
