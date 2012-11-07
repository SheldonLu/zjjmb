package com.mzoneapp.zjjmb.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
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
import com.mzoneapp.zjjmb.adapter.ArticleAdapter;
import com.mzoneapp.zjjmb.api.ApiConstants;
import com.mzoneapp.zjjmb.api.Article;

/**
 * Fragment that displays the news headlines for a particular news category.
 *
 * This Fragment displays a list with the news headlines for a particular news category.
 * When an item is selected, it notifies the configured listener that a headlines was selected.
 */
public class HeadlinesFragment extends SherlockListFragment implements OnItemClickListener,OnScrollListener {
	
    // The list adapter for the list we are displaying
    ArticleAdapter adapter;
    
    private IgnitedHttp http;
    
    private String type;
    private boolean isLoaded = false;
    private boolean isNull = false;

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
    
//    public HeadlinesFragment(Activity activity){
//    }
//
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	if(isLoaded) return;
    	
    	type = getArguments().getString(ApiConstants.TYPE);
    	http = new IgnitedHttp(getActivity());
    	ListView listView = getListView();
    	listView.setCacheColorHint(0);
    	listView.setDivider(null);
    	adapter = new ArticleAdapter(getActivity(),listView);
    	
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setOnScrollListener(this);
        loadNextPage();
        
        isLoaded = true;
    }
    
    private void loadNextPage() {
        adapter.setIsLoadingData(true);
        IgnitedAsyncTask<MainActivity, Void, Void, Void> task = new IgnitedAsyncTask<MainActivity, Void, Void, Void>() {
            @Override
            public Void run(Void... params) throws Exception {
            	int start = adapter.getItemCount();
            	int pageno = start / ApiConstants.DEFAULT_SIZE + 1;
            	String url = ApiConstants.instance().
            			getListUrl(type, start);
            	IgnitedHttpResponse response =  http.get(url).retries(3).expecting(200).send();
            	String responseBody = response.getResponseBodyAsString();
            	
            	Object result = null;
         		responseBody = responseBody.trim();
         		if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
         			result = new JSONTokener(responseBody).nextValue();
         		}
         		if(result instanceof JSONObject) {
         			// TODO
                    JSONObject jsonObject = (JSONObject)result;
                } else if(result instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray)result;
                    int length = jsonArray.length();
                    // no result, not load next time
                    if( 0 == length) isNull = true;
//                    if( length < ApiConstants.DEFAULT_SIZE) isNull = true;
                    ArrayList<Article> list = new ArrayList<Article>();
                    for(int i = 0; i < length; i++){
                    	JSONObject jsonObject = jsonArray.getJSONObject(i);
                    	Article line = new Article();
                    	line.id = jsonObject.getString("id");
                    	line.author = jsonObject.getString("author");
                    	line.issuedate = jsonObject.getString("issuedate");
                    	line.title = jsonObject.getString("title");
                    	line.type = jsonObject.getString("type");
                    	line.desc = "有专家建议2015年全面放开二胎政策,也有专家说放开二胎政策,百害而无一利. 网间对其利弊也开始了热闹的讨论";
                    	list.add(line);
                    }
                    
                    adapter.getData().addAll(list);
                } else {
                	// TODO 更完善的异常处理
                	isNull = true;
                	throw new JSONException("Unexpected type " + result.getClass().getName());
                }
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
            	ArrayList<Article> list = new ArrayList<Article>();
            	Article line = new Article();
            	int start = adapter.getItemCount();
            	for(int i = start;i<start+10;i++){
            		line = new Article();
            		line.id = i+"";
            		line.title = "titlexxxxtitleoooootitle"+i+"type="+type;
            		line.author = "author"+i;
            		line.issuedate = "2012121"+i;
            		line.desc = "desc1234desc123123desc234"+i;
            		line.type = type;
            		list.add(line);
            	}
            	
            	adapter.getData().addAll(list);
            	return true;
            }
        };
        task.execute();
    }
    
    public void refresh(){
    	clear();
    	loadNextPage();
    }
    
    public void clear(){
    	adapter.getData().clear();
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
        Article article = adapter.getData().get(position);
        Intent i = new Intent(getActivity(), ArticleActivity.class);
        i.putExtras(Article.convertArticleToBundle(article));
        startActivity(i);
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
        if (!isNull && adapter.shouldRequestNextPage(firstVisibleItem, visibleItemCount, totalItemCount)) {
            loadNextPage();
        }
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
