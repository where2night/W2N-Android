package com.where2night.activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.where2night.R;
import com.where2night.fragments.DJsFragment;
import com.where2night.fragments.EventsFragment;
import com.where2night.fragments.FriendsFragment;
import com.where2night.fragments.HomeFragment;
import com.where2night.fragments.LocalsFragment;
import com.where2night.fragments.PhotosFragment;
import com.where2night.fragments.ProfileFragment;

/*
 * Main Activity
 * In this Activity is implemented the navigation drawer
 */

public class MainActivity extends FragmentActivity{ 

	private ListView drawerList;
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private int lastIndex = 0;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment[] fragments = new Fragment[]{new HomeFragment(),
    									  new ProfileFragment(),
    									  new EventsFragment(),
    									  new PhotosFragment(),
    									  new FriendsFragment(),    									  
    									  new LocalsFragment(),
    									  new DJsFragment()};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                                                       R.layout.drawer_list_item, 
                                                       drawerOptions));
        
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        drawerList.setItemChecked(0, true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
            
        	public void onDrawerClosed(View view) {
            	invalidateOptionsMenu();
            }

        	public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu();
            }
        };
        
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.contentFrame, fragments[0])
        		.add(R.id.contentFrame, fragments[1])
        		.add(R.id.contentFrame, fragments[2])
        		.add(R.id.contentFrame, fragments[3])
        		.add(R.id.contentFrame, fragments[4])
        		.add(R.id.contentFrame, fragments[5])
        		.add(R.id.contentFrame, fragments[6])
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
				        		  .hide(fragments[2])
				        		  .hide(fragments[3])
				        		  .hide(fragments[4])
				        		  .hide(fragments[5])
				        		  .hide(fragments[6])
				        		  .commit();
        
        setContent(0);
    
        
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
          	 
            if (drawerLayout.isDrawerOpen(drawerList)) {
            	drawerLayout.closeDrawer(drawerList);
            } else {
            	drawerLayout.openDrawer(drawerList);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void setContent(int index) {
	    Fragment toHide = null;
		Fragment toShow = null;
		final ActionBar actionBar = getActionBar();
		toHide = fragments[lastIndex];
		toShow = fragments[index];
		lastIndex = index;
		
		FragmentManager manager = getSupportFragmentManager();
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(drawerOptions[index]);
		drawerList.setItemChecked(index, true);
	    drawerLayout.closeDrawer(drawerList);	
    }

private class DrawerItemClickListener implements ListView.OnItemClickListener {
	
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	setContent(position);
    }
}	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
