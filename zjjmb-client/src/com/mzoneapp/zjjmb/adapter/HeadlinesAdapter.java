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

		RemoteImageView icon = (RemoteImageView) convertView.findViewById(R.id.image);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);
		TextView date = (TextView) convertView.findViewById(R.id.dateText);

		title.setText(headline.title);
//		desc.setText(headline.desc);
		desc.setText("有专家建议2015年全面放开二胎政策,也有专家说放开二胎政策,百害而无一利. 网间对其利弊也开始了热闹的讨论");
		date.setText(headline.issuedate.split(" ")[0]);

		return convertView;
	}
}
