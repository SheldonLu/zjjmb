package com.mzoneapp.zjjmb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nineoldandroids.animation.ObjectAnimator;

public class MainActivity extends SherlockFragmentActivity 
		implements ActionBar.TabListener {

    private final Handler handler = new Handler();
    private RoundedColourFragment leftFrag;
    private RoundedColourFragment rightFrag;
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
        leftFrag = new RoundedColourFragment(getResources().getColor(
                R.color.android_green), 1f, MARGIN, MARGIN / 2, MARGIN, MARGIN);
        rightFrag = new RoundedColourFragment(getResources().getColor(
                R.color.honeycombish_blue), 2f, MARGIN / 2, MARGIN, MARGIN,
                MARGIN);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.root, leftFrag);
        ft.add(R.id.root, rightFrag);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void rotateLeftFrag() {
        if (leftFrag != null) {
            ObjectAnimator.ofFloat(leftFrag.getView(), "rotationY", 0, 180)
                    .setDuration(500).start();
        }
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
		rotateLeftFrag();
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
