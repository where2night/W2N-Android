package es.where2night.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.fragments.localdetail.JukeboxViewFragment;
import es.where2night.fragments.localdetail.LocalAsistantsFragment;
import es.where2night.fragments.localdetail.LocalDiscountListFragment;
import es.where2night.fragments.localdetail.LocalEventsFragment;
import es.where2night.fragments.localdetail.LocalInfoFragment;
import es.where2night.fragments.localdetail.LocalPhotographsFragment;
import es.where2night.fragments.localdetail.LocalStatisticsFragment;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalViewActivity extends FragmentActivity implements  ActionBar.TabListener  {
   
    public static final String ID = "id";
    public static Button btnIGo;
    public static Button btnIDontGo;
    private String msg = "";
    private String toastMsg = "";
    FrameLayout f;
    private int lastIndex = 0;
    private Bundle bundle;
    Long date;
    DatePicker datePicker;
    private String localId = "";
    public Menu actionBarMenu;
    
    private Button accept;
    private Button cancel;
    String dateString = "";
    
    private Fragment[] fragments = new Fragment[]{ new LocalInfoFragment(),
    											   new LocalEventsFragment(),
    											   new LocalDiscountListFragment(),
    											   new JukeboxViewFragment(),
    											   new LocalPhotographsFragment(),
    											   new LocalStatisticsFragment(),
    											   new LocalAsistantsFragment()};
    private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.activity_local_view);
        
        // Getting localId from the caller object
        localId = getIntent().getStringExtra(ID);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.logo7);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        btnIGo = (Button) findViewById(R.id.btnIGo);
        btnIDontGo = (Button) findViewById(R.id.btnIDontGo);
        
         
	//	 dialog.show();
        
        btnIGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(LocalViewActivity.this);
				dialog.setContentView(R.layout.selectdate_dialog);
				dialog.setTitle("Elige fecha");
				datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
				accept = (Button) dialog.findViewById(R.id.btnSetDT);
				cancel = (Button) dialog.findViewById(R.id.btnCancelDT);
				
				accept.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dateString = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth()+1) + "/" + datePicker.getYear();
						goingTo(false);
						dialog.dismiss();
					}
				});
					
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			
				dialog.show();
				
			}
		});
        
        btnIDontGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(LocalViewActivity.this);
				dialog.setContentView(R.layout.selectdate_dialog);
				dialog.setTitle("Elige fecha");
				datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
				accept = (Button) dialog.findViewById(R.id.btnSetDT);
				cancel = (Button) dialog.findViewById(R.id.btnCancelDT);
				
				accept.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dateString = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth()+1) + "/" + datePicker.getYear();
						goingTo(true);	
						dialog.dismiss();
					}
				});
					
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
        
        String[] tabs = getResources().getStringArray(R.array.local_tabs);
       
        bundle = new Bundle();
		bundle.putString(ID, localId);
        for (int i = 0; i</*tabs.length*/7; i++){
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
        	    .add(R.id.selectedTabFragment, fragments[2])
        	    .add(R.id.selectedTabFragment, fragments[3])
        	    .add(R.id.selectedTabFragment, fragments[4])
        	    .add(R.id.selectedTabFragment, fragments[5])
        	    .add(R.id.selectedTabFragment, fragments[6])
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
    
    
    public void setContent(int index) {
    	if(lastIndex == 3 ){
    		for(int i=0; i<actionBarMenu.size(); i++){
		    	actionBarMenu.getItem(i).setVisible(false);
		    }
    	}
    	Fragment toHide = null;
		Fragment toShow = null;
		
		toHide = fragments[lastIndex];
		toShow =  fragments[index];
		lastIndex = index;
		
		FragmentManager manager = getSupportFragmentManager();
		
			/* We send local Id to the fragments */
			
		
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
		
		
		if (index == 1) ((LocalEventsFragment) toShow).fill();
		if (index == 3) {
			for(int i=0; i<actionBarMenu.size(); i++){
		    	actionBarMenu.getItem(i).setVisible(true);
		    }
			((JukeboxViewFragment) toShow).fill();
		}
		if (index == 4) ((LocalPhotographsFragment) toShow).fill();
		if (index == 5) ((LocalStatisticsFragment) toShow).fill();
	//	if (index == 6) ((LocalAsistantsFragment) toShow).fill();
    }
    

	
	private void goingTo(boolean notGoing){
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		String urlPost = Helper.getGoToLocalUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("GoToPub", urlPost);
		
			
		Response.Listener<String> succeedListener = new Response.Listener<String>(){
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	JSONObject respuesta2 = new JSONObject(response);
	            	String error = "error";
	            	if (respuesta2.getString("goToPub").equals(error)){
	            		muestraFallo(msg);
	            	}
	            	Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
	            	
            	}
	            catch (Exception e) {
					e.printStackTrace();
					muestraError();
				}
	        
	        }

		};
		Response.ErrorListener errorListener = new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
             // error
				Log.e("Error.Response", error.toString());
			}
		};
		
		StringRequest request;
		

		if (notGoing){
			msg = "No has dicho que fueras a ir el día " + dateString;
			toastMsg = "Desapuntado!";
		}else{
			msg = "Ya has dicho que vas a ir el día " + dateString;
			toastMsg = "Apuntado!";
		}
    	
		String urlDelete = Helper.getGoToLocalUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId + "/" + dateString;
		Log.e("NotGoing", urlDelete);
		
		if(notGoing){
			request = new StringRequest(Request.Method.DELETE, urlDelete, succeedListener, errorListener); 
		}
		else{
			request = new StringRequest(Request.Method.POST, urlPost, succeedListener, errorListener){
				@Override
			    protected Map<String, String> getParams() 
			    {  
			    	Map<String, String> info = new HashMap<String, String>();
			    	info.put("date", dateString);
			        return info;
			    }
			}; 

		}
		requestQueue.add(request);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		setContent(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	 @Override
	 public Intent getParentActivityIntent() {
			Intent intent = new Intent(this, MainActivity.class);
		//	intent.putExtra(MainActivity.OPTION, "5");
			return intent;
	 }
	 
	 @Override
	    public void onBackPressed()
	    {
	    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	        startActivity(intent);
	        
	        //super.onBackPressed();  // optional depending on your needs
	    }
	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.jukebox_view, menu);
	        
	       /* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		    // Assumes current activity is the searchable activity
		    searchView.setSearchableInfo(searchManager.getSearchableInfo( getComponentName()));
		    searchView.setIconifiedByDefault(true);*/
		    actionBarMenu = menu;

		    for(int i=0; i<actionBarMenu.size(); i++){
		    	actionBarMenu.getItem(i).setVisible(false);
		    }
		    
	        return true;
	    }
	 
	 @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case R.id.action_refresh:
	    	//fragments[3] = new JukeboxViewFragment();
	    	((JukeboxViewFragment)fragments[3]).fill();
	    	break;
	    case 16908332:
	    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	        startActivity(intent);
	    default:
	      break;
	    }

	    return true;
	  }
	 
	 private void muestraError() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
 		builder.setMessage("Estás apuntado a un evento o una lista ese día. Debes desapuntarte antes de decir que vas (no vas) a ir.")
 		        .setTitle("Error")
 		        .setCancelable(false)
 		        .setNeutralButton("Aceptar",
 		                new DialogInterface.OnClickListener() {
 		                    public void onClick(DialogInterface dialog, int id) {
 		                        dialog.cancel();
 		                    }
 		                });
 		AlertDialog alert = builder.create();
 		alert.show();
			
		}
	 
	 public void muestraFallo(String msg){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
 		builder.setMessage(msg)
 		        .setTitle("Error")
 		        .setCancelable(false)
 		        .setNeutralButton("Aceptar",
 		                new DialogInterface.OnClickListener() {
 		                    public void onClick(DialogInterface dialog, int id) {
 		                        dialog.cancel();
 		                    }
 		                });
 		AlertDialog alert = builder.create();
 		alert.show();
	 }

}
