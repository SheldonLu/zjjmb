package com.mzoneapp.zjjmb.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Headline;

public class HeadlinesAdapter extends ArrayAdapter<Headline> {
	
	public HeadlinesAdapter(Context context){
		super(context, R.layout.list_item_headline);
	}
}
