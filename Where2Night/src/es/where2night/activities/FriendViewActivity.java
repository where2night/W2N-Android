package es.where2night.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.fragments.friendProfile.FriendEventsFragment;
import es.where2night.fragments.friendProfile.FriendInfoFragment;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class FriendViewActivity extends FragmentActivity implements ActionBar.TabListener  {

	public static final String ID = "id";
	private Bundle bundle;
    private String friendId = "";
    private int lastIndex = 0;
    
  
    
    private Fragment[] fragments = new Fragment[] {new FriendInfoFragment(),
    											   new FriendEventsFragment()};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);
        
        friendId = getIntent().getStringExtra(ID);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.logo7);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        
        String[] tabs = getResources().getStringArray(R.array.friend_tabs);
        
        bundle = new Bundle();
		bundle.putString(ID, friendId);
		
        for (int i = 0; i<tabs.length; i++){
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText(tabs[i])
	                        .setTabListener(this));
			fragments[i].setArguments(bundle);
        }
        
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.selectedTabFragment, fragments[0])
        	    .add(R.id.selectedTabFragment, fragments[1])
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
				        		  .commit();
        
        setContent(0);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_view_activty, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		setContent(tab.getPosition());
	}
	
	public void setContent(int index) {
	    Fragment toHide = null;
		Fragment toShow = null;
		
		toHide = fragments[lastIndex];
		toShow =  fragments[index];
		lastIndex = index;
		
		FragmentManager manager = getSupportFragmentManager();
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
		
		if (index == 1) ((FriendEventsFragment) toShow).fill();
    }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Intent getParentActivityIntent() {
		Intent intent = new Intent(this, MainActivity.class);
		return intent;
	}

	

}
