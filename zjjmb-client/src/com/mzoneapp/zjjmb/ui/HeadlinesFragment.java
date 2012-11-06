package com.mzoneapp.zjjmb.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.support.http.IgnitedHttp;
import com.github.ignition.support.http.IgnitedHttpResponse;
import com.mzoneapp.zjjmb.adapter.HeadlinesAdapter;
import com.mzoneapp.zjjmb.api.ApiConstants;
import com.mzoneapp.zjjmb.api.Headline;

/**
 * Fragment that displays the news headlines for a particular news category.
 *
 * This Fragment displays a list with the news headlines for a particular news category.
 * When an item is selected, it notifies the configured listener that a headlines was selected.
 */
public class HeadlinesFragment extends SherlockListFragment implements OnItemClickListener,OnScrollListener {
	
    // The list adapter for the list we are displaying
    HeadlinesAdapter adapter;
    
    private IgnitedHttp http;

    // The listener we are to notify when a headline is selected
    OnHeadlineSelectedListener mHeadlineSelectedListener = null;

    /**
     * Represents a listener that will be notified of headline selections.
     */
    public interface OnHeadlineSelectedListener {
        /**
         * Called when a given headline is selected.
         * @param index the index of the selected headline.
         */
        public void onHeadlineSelected(int index);
    }

    /**
     * Default constructor required by framework.
     */
    public HeadlinesFragment() {
        super();
    }
    
    public HeadlinesFragment(Activity activity){
    }

    @Override
    public void onStart() {
        super.onStart();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnScrollListener(this);
        loadNextPage();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	http = new IgnitedHttp(getActivity());
    	ListView listView = getListView();
    	listView.setCacheColorHint(0);
    	listView.setDivider(null);
    	adapter = new HeadlinesAdapter(getActivity(),listView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    private void loadNextPage() {
        adapter.setIsLoadingData(true);
        IgnitedAsyncTask<MainActivity, Void, Void, Void> task = new IgnitedAsyncTask<MainActivity, Void, Void, Void>() {
            @Override
            public Void run(Void... params) throws Exception {
            	String url = ApiConstants.instance().getListUrl(0);
            	IgnitedHttpResponse response =  http.get(url).retries(3).expecting(200).send();
            	String tmp = response.getResponseBodyAsString();
//            	adapter.getData().add();
                return null;
            }

            @Override
            public boolean onTaskCompleted(Void result) {
                adapter.setIsLoadingData(false);
                adapter.notifyDataSetChanged();
                return true;
            }
            
            @Override
            public boolean onTaskFailed(Exception ex){
            	ArrayList<Headline> list = new ArrayList<Headline>();
            	Headline line = new Headline();
            	int start = adapter.getCount();
            	for(int i = start;i<start+10;i++){
            		line = new Headline();
            		line.id = i+"";
            		line.title = "titlexxxxtitleoooootitle"+i;
            		line.author = "author"+i;
            		line.issuedate = "2012121"+i;
            		line.desc = "desc1234desc123123desc234"+i;
            		line.type = "0";
            		list.add(line);
            	}
            	
            	adapter.getData().addAll(list);
            	return true;
            }
        };
        task.execute();
    }

    /**
     * Sets the listener that should be notified of headline selection events.
     * @param listener the listener to notify.
     */
    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener listener) {
        mHeadlineSelectedListener = listener;
    }

    /**
     * Handles a click on a headline.
     *
     * This causes the configured listener to be notified that a headline was selected.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mHeadlineSelectedListener) {
            mHeadlineSelectedListener.onHeadlineSelected(position);
        }
    }

    /** Sets choice mode for the list
     *
     * @param selectable whether list is to be selectable.
     */
    public void setSelectable(boolean selectable) {
        if (selectable) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
        if (adapter.shouldRequestNextPage(firstVisibleItem, visibleItemCount, totalItemCount)) {
            loadNextPage();
        }
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
