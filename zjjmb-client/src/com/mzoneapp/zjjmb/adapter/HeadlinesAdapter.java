package com.mzoneapp.zjjmb.adapter;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ignition.core.adapters.EndlessListAdapter;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Headline;

public class HeadlinesAdapter extends EndlessListAdapter<Headline> {
	private static final String LOG_TAG = HeadlinesAdapter.class.getName();
	private final LayoutInflater inflater;
	private long lastUpdate = -1;

	public HeadlinesAdapter(Activity activity,AbsListView listView) {
		super(activity, listView, R.layout.loading_item);

		inflater = LayoutInflater.from(activity);
	}

	private List<Headline> headlines;
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

		Headline headline = getItem(position);

		ImageView icon = (ImageView) convertView.findViewById(R.id.typeIcon);
		TextView title = (TextView) convertView.findViewById(R.id.titleText);
		TextView author = (TextView) convertView.findViewById(R.id.authorText);
		TextView date = (TextView) convertView.findViewById(R.id.dateText);

		// TODO： modify icon
		switch (Integer.valueOf(headline.type)) {
		case 1:
			// 通知公告 icon
			icon.setImageResource(R.drawable.ic_launcher);
			break;
		case 2:
			// 局内动态 icon
			icon.setImageResource(R.drawable.ic_launcher);
			break;
		case 3:
			// 工作动态 icon
			icon.setImageResource(R.drawable.ic_launcher);
			break;
		}

		title.setText(headline.title);
		author.setText(headline.author);
		date.setText(headline.issuedate);

		return convertView;
	}
}
