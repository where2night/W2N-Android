package es.where2night.activities;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.where2night.R;

import es.where2night.fragments.DJsFragment;
import es.where2night.fragments.EventsFragment;
import es.where2night.fragments.FriendsFragment;
import es.where2night.fragments.HomeFragment;
import es.where2night.fragments.LocalsFragment;
import es.where2night.fragments.PhotosFragment;
import es.where2night.fragments.ProfileFragment;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.FbManagement;
import es.where2night.utilities.Helper;

/*
 * Main Activity
 * In this Activity is implemented the navigation drawer
 */

public class MainActivity extends FragmentActivity { 

	protected static final String EMAIL = "email";
	protected static final String TYPE = "type";
	protected static final String PARENT = "parent";
	private ListView drawerList;
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private int lastIndex = 0;
    private ActionBarDrawerToggle drawerToggle;
    private String type;
    private String email;
    private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    private Fragment[] fragments = new Fragment[]{new HomeFragment(),
    									  new ProfileFragment(),
    									  new EventsFragment(),
    									  new PhotosFragment(),
    									  new FriendsFragment(),    									  
    									  new LocalsFragment(),
    									  new DJsFragment()};
	
    private ProgressDialog connectionProgressDialog;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        if (getIntent().getStringExtra(PARENT).equals("1")){
	        email = getIntent().getStringExtra(EMAIL);
	        type = getIntent().getStringExtra(TYPE);
	        connectionProgressDialog = new ProgressDialog(this);
	        connectionProgressDialog.setMessage("Cargando tu perfil...");
	        getDataFromServer(email, type);
        }
        
        if (getIntent().getStringExtra(PARENT).equals("2")){
	        
	        email = getIntent().getStringExtra(EMAIL);
	        type = getIntent().getStringExtra(TYPE);
	        connectionProgressDialog = new ProgressDialog(this);
	        connectionProgressDialog.setMessage("Cargando tu perfil...");
	        getDataFromServer(email, type);
        }
        
        
        
        
        
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                                                       R.layout.drawer_list_item, 
                                                       drawerOptions));
        
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        drawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        drawerList.setItemChecked(0, true);
        
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
            
        	public void onDrawerClosed(View view) {
        		
            	invalidateOptionsMenu();
            	getActionBar().setIcon(R.drawable.logo7);
            	getActionBar().setTitle(drawerOptions[lastIndex]);
            }

        	public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu();
            	getActionBar().setTitle("");
            	getActionBar().setIcon(R.drawable.open_drawer);
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
    
        getActionBar().setIcon(R.drawable.logo7);
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
        }else if (item.getItemId() == R.id.action_logout){
        	DataManager dm = new DataManager(getApplicationContext());
 			dm.logout();
			FbManagement.callFacebookLogout(getApplicationContext());
			
			Intent i = new Intent(getApplicationContext(), InitActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
        }else if (item.getItemId() == R.id.action_edit_profile){
        	Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
			startActivity(i);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void getDataFromServer(final String email,final String type){
    	
    	
    	final DataManager dm = new DataManager(getApplicationContext());
    	
    	if (!type.equals("-1")  && dm.checkLogin() == -2){
    		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
    		String url = "";
    		
    		if (type.equals("0"))
    				url = Helper.getLoginFBUrl();
    		else
    				url = Helper.getLoginGPUrl();
    		
    		Response.Listener<String> succeedListener = new Response.Listener<String>() 
    	    {
    	        @Override
    	        public void onResponse(String response) {
    	            // response
    	        	Log.e("Response", response);
    	            try {
    		            respuesta = new JSONObject(response);
    					String token = respuesta.getString("Token");
    					String idProfile = respuesta.getString("id");
    					if (!(token.equals("0")))
    					{ 
    						dm.login(email,idProfile,token,type);
    						connectionProgressDialog.dismiss();
    					}else{}
    	            } catch(JSONException e) {}
    	        }
    	    };
    	    Response.ErrorListener errorListener = new Response.ErrorListener() 
    	    {
    	         @Override
    	         public void onErrorResponse(VolleyError error) {
    	             // error
    	             Log.e("Error.Response", error.toString());
    	             connectionProgressDialog.dismiss();
    	       }
    	    };
    		
    		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener) 
    		{     
    			    @Override
    			    protected Map<String, String> getParams() 
    			    {  
    			        return dm.getUser(email);  
    			    }
    		};
    		
    		requestQueue.add(request);	
    		
    		
    		
    		
    		}
    		
    	}

    	
    
    
}
