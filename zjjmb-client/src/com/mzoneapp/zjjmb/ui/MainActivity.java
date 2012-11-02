package com.mzoneapp.zjjmb.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.mzoneapp.zjjmb.R;

public class MainActivity extends SherlockFragmentActivity 
		implements ActionBar.TabListener {

    private final Handler handler = new Handler();
    private boolean useLogo = false;
    private boolean showHomeUp = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ActionBar ab = getSupportActionBar();

        // set defaults for logo & home up
        ab.setDisplayHomeAsUpEnabled(showHomeUp);
        ab.setDisplayUseLogoEnabled(useLogo);
//        ab.setLogo(R.drawable.ic_stat_android);

        // set up tabs nav
        ab.addTab(ab.newTab().setText("通知公告").setTabListener(this));
        ab.addTab(ab.newTab().setText("局内动态").setTabListener(this));
        ab.addTab(ab.newTab().setText("工作动态").setTabListener(this));
        
        // default to tab navigation
        showTabsNav();
        
        // create a couple of simple fragments as placeholders
        final int MARGIN = 8;

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
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
