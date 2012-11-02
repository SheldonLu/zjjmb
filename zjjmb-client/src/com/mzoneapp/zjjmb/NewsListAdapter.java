package com.mzoneapp.zjjmb;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.mzoneapp.zjjmb.api.News;

public class NewsListAdapter extends ArrayAdapter<News> {
	
	public NewsListAdapter(Context context){
		super(context, R.layout.news_summary_item);
	}
}
