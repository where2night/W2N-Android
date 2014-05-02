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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.fragments.localdetail.JukeboxViewFragment;
import es.where2night.fragments.localdetail.LocalDiscountListFragment;
import es.where2night.fragments.localdetail.LocalEventsFragment;
import es.where2night.fragments.localdetail.LocalInfoFragment;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalViewActivity extends FragmentActivity implements OnClickListener, ActionBar.TabListener  {
   
    public static final String ID = "id";
    public static Button btnIGo;
    private int lastIndex = 0;
    private Bundle bundle;
    private String localId = "";
    
    private Fragment[] fragments = new Fragment[]{ new LocalInfoFragment(),
    											   new LocalEventsFragment(),
    											   new LocalDiscountListFragment(),
    											   new JukeboxViewFragment()};
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
        
        btnIGo.setOnClickListener(this);
        
        String[] tabs = getResources().getStringArray(R.array.local_tabs);
       
        bundle = new Bundle();
		bundle.putString(ID, localId);
        for (int i = 0; i</*tabs.length*/4; i++){
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
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
        						  .hide(fragments[2])
        						  .hide(fragments[3])
				        		  .commit();
        
        setContent(0);
        
    }
    
    
    public void setContent(int index) {
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
		if (index == 2) Toast.makeText(getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
		if (index == 3) ((JukeboxViewFragment) toShow).fill();
    }
    

    @Override
	public void onClick(View v) {
		 if (v.getId() == btnIGo.getId()){
			 if (btnIGo.isSelected()){
				 btnIGo.setSelected(false);
				 btnIGo.setText(getResources().getString(R.string.IGo));
				 goingTo(true);
			 }else{
				 btnIGo.setSelected(true);
				 btnIGo.setText(getResources().getString(R.string.Going));
				 goingTo(false);
			 }
		 }
	}
	
	private void goingTo(boolean notGoing){
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		String url = Helper.getGoToLocalUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("GoToPub", url);
		 
		Response.Listener<String> succeedListener = new Response.Listener<String>(){
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	respuesta = new JSONObject(response);
	            	if (respuesta.getString("goToPub").equals("error")){
	            		if (btnIGo.isSelected()){
	            			btnIGo.setSelected(false);
	       				 	btnIGo.setText(getResources().getString(R.string.IGo));
	            		}else{
	            			btnIGo.setSelected(true);
	        				btnIGo.setText(getResources().getString(R.string.Going));
	            		}
	            	}
            	}
	            catch (Exception e) {
					e.printStackTrace();
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
		
		if(notGoing){
			request = new StringRequest(Request.Method.DELETE, url, succeedListener, errorListener); 
		}
		else{
			request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 

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
}
