package es.where2night.activities;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.fragments.EventsFragment;
import es.where2night.fragments.FriendsFragment;
import es.where2night.fragments.HomeFragment;
import es.where2night.fragments.LocalsFragment;
import es.where2night.fragments.MessagesFragment;
import es.where2night.fragments.ProfileFragment;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.FbManagement;
import es.where2night.utilities.Helper;

/*
 * Main Activity
 * In this Activity is implemented the navigation drawer
 */

public class MainActivity extends FragmentActivity{ 

	protected static final String EMAIL = "email";
	protected static final String TYPE = "type";
	protected static final String PARENT = "parent";
	protected static final String OPTION = "option";
	private ListView drawerList;
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private int lastIndex = 0;
    private ActionBarDrawerToggle drawerToggle;
    private String type;
    private String email;
    private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    private Menu actionBarMenu;
    private Fragment[] fragments = new Fragment[]{new HomeFragment(),
    									  new ProfileFragment(),
    									  new EventsFragment(),
    									  new FriendsFragment(),    									  
    									  new LocalsFragment(),
    									  new MessagesFragment()};
	
    private ProgressDialog connectionProgressDialog;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        if (getIntent().hasExtra(PARENT) && getIntent().getStringExtra(PARENT).equals("1")){
	        email = getIntent().getStringExtra(EMAIL);
	        type = getIntent().getStringExtra(TYPE);
	        connectionProgressDialog = new ProgressDialog(this);
	        connectionProgressDialog.setMessage("Cargando tu perfil...");
	        getDataFromServer(email, type);
        }
        
        if (getIntent().hasExtra(PARENT) && getIntent().getStringExtra(PARENT).equals("2")){
	        
	        email = getIntent().getStringExtra(EMAIL);
	        type = getIntent().getStringExtra(TYPE);
	        connectionProgressDialog = new ProgressDialog(this);
	        connectionProgressDialog.setMessage("Cargando tu perfil...");
	        getDataFromServer(email, type);
        }
        int option = 0;
        if (getIntent().hasExtra(OPTION)){
        	option = Integer.parseInt(getIntent().getStringExtra(OPTION));
        }
        
        
        
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawerLayout);
        drawerList = (ListView) this.findViewById(R.id.leftDrawer);
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
            	getActionBar().setSubtitle("");
            }

        	public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu();
            	getActionBar().setTitle("");
            	getActionBar().setSubtitle("");
            //	getActionBar().setIcon(R.drawable.open_drawer);
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
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
				        		  .hide(fragments[2])
				        		  .hide(fragments[3])
				        		  .hide(fragments[4])
				        		  .hide(fragments[5])
				        		  .commit();
        
        setContent(option);
    
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
        } else if (item.getItemId() == R.id.action_search){
            return true;
        }else if (item.getItemId() == R.id.action_notifications){
        	Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
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
		 
		/*if (lastIndex != 1 || index != 1) 
		manager.beginTransaction().detach(toHide).attach(toShow).commit();*/
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(drawerOptions[index]);
		drawerList.setItemChecked(index, true);
	    drawerLayout.closeDrawer(drawerList);	
	    
	    if (index == 1) {
	    	Intent i = new Intent(MainActivity.this, ProfileViewActivity.class);
			startActivity(i);
	    	((ProfileFragment) toShow).fill();
	    }
	    if (index == 4) {
	    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    	((LocalsFragment) toShow).setTabs();
	    }
	    if (index == 2) ((EventsFragment) toShow).fill();
	    if (index == 3) ((FriendsFragment) toShow).fill();
	    if (index == 5) ((MessagesFragment) toShow).fill();
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
        
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo( getComponentName()));
	    searchView.setIconifiedByDefault(true);
	    actionBarMenu = menu;
	    
	    RelativeLayout badgeLayout = (RelativeLayout) actionBarMenu.findItem(R.id.action_notifications).getActionView();
	    ImageView imgNotifications = (ImageView) badgeLayout.findViewById(R.id.actionbar_notifcation_img);
	    imgNotifications.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), FriendsRequestActivity.class);
				startActivity(i);
				
			}
		});
	    getFriendshipRequest();
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
    						((HomeFragment)fragments[0]).fill(null);
    						invalidateOptionsMenu();
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
    			    	Map<String,String> info = dm.getUser(email);
    			    	info.put("mobile", "1");
    			        return info;   
    			    }
    		};
    		
    		requestQueue.add(request);	
    		
    		
    		
    		
    		}
    		
    	}

    private void getFriendshipRequest() {
    	
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getFriendshipPetUrl() + "/" + cred[0] + "/" + cred[1];
		Log.e("Friendship", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            		JSONObject root = new JSONObject(response);
	            		RelativeLayout badgeLayout = (RelativeLayout) actionBarMenu.findItem(R.id.action_notifications).getActionView();
	            	    TextView txtNotifications = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
	            		txtNotifications.setText(root.getString("numPetitions"));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}	
    
    @Override
    public void onBackPressed()
    {        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("¿Desea salir de Where2Night?")
    	        .setTitle("Salir")
    	        .setCancelable(false)
    	        .setNegativeButton("No, sigo un rato más",
    	                new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog, int id) {
    	                    	//Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    	                        //startActivity(intent);
    	                    }
    	                })
    	        .setPositiveButton("Si, salir",
    	                new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog, int id) {
    	                    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	                        finish();
    	                    }
    	                });
    	AlertDialog alert = builder.create();
    	alert.show();
        
        //super.onBackPressed();  // optional depending on your needs
    }
        
    
}
