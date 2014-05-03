package es.where2night.activities;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.fragments.editprofile.BasicInfoFragment;
import es.where2night.fragments.editprofile.DetailedInfoFragment;
import es.where2night.fragments.editprofile.PasswordChangeFragment;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class EditProfileActivity extends FragmentActivity implements ActionBar.TabListener  {

	
	private Button btnEditAccept;
	private Button btnEditCancel;
	private ProgressBar pgEdit;
	

	private String idProfile;
	
	
	private int lastIndex = 0;
	
	public static RequestQueue requestQueue;
    private JSONObject respuesta = null;
    
    private Fragment[] fragments = new Fragment[] {new BasicInfoFragment(),
			   										new DetailedInfoFragment(),
			   										new PasswordChangeFragment()};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		 final ActionBar actionBar = getActionBar();
        actionBar.removeAllTabs();
        
        actionBar.setIcon(R.drawable.logo7);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        
        String[] tabs = getResources().getStringArray(R.array.editProfile_tabs);
        
		
        for (int i = 0; i<tabs.length; i++){
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText(tabs[i])
	                        .setTabListener(this));
        }
        
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.selectedTabFragment, fragments[0])
        	    .add(R.id.selectedTabFragment, fragments[1])
        	    .add(R.id.selectedTabFragment, fragments[2])
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
        						  .hide(fragments[2])
				        		  .commit();
        
        setContent(0);
		
		btnEditAccept = (Button) findViewById(R.id.btnEditAccept);
		btnEditCancel = (Button) findViewById(R.id.btnEditCancel);
		pgEdit = (ProgressBar) findViewById(R.id.pgEdit);
		pgEdit.setVisibility(View.VISIBLE);
		fillData();
		
		btnEditAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pgEdit.setVisibility(View.VISIBLE);
				btnEditAccept.setEnabled(false);
				sendData();
			}
		});
		
		btnEditCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra(MainActivity.PARENT, "0");
				startActivity(i);
				
			}
		}); 
		
	
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
		
    }

	private void fillData() {
		
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		idProfile = cred[0];
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1] + "/" + cred[0];
		Log.e("Edit", url);
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            	pgEdit.setVisibility(View.GONE);
	            	btnEditAccept.setEnabled(true);
		            try {
						respuesta = new JSONObject(response);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            ((BasicInfoFragment) fragments[0]).setData(respuesta);
		            ((DetailedInfoFragment) fragments[1]).setData(respuesta);
		           
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEdit.setVisibility(View.GONE);
	        	 btnEditAccept.setEnabled(true);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	
	private void sendData() {
		
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1];
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            pgEdit.setVisibility(View.GONE);
				btnEditAccept.setEnabled(true);
				Toast.makeText(getApplicationContext(), "Perfil Guardado", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra(MainActivity.PARENT, "0");
				startActivity(i);
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEdit.setVisibility(View.GONE);
	        	 btnEditAccept.setEnabled(true);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener) 
		{     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	HashMap<String, String> info = new HashMap<String, String>();
			    	String[] dataBasic = ((BasicInfoFragment) fragments[0]).getData();
			    	String[] dataDetailed = ((DetailedInfoFragment) fragments[1]).getData();
			    	
			    	info.put("idProfile", idProfile);
			    	if (dataBasic[1].equals("false"))
			    		info.put("picture", dataBasic[0]);
			    	else{
			    		info.put("picture", dataBasic[0]);
			    		info.put("uploading", "true");
			    	}
			    	info.put("name", dataBasic[2]);
			    	info.put("surnames", dataBasic[3]);
			    	
			    	info.put("birthdate", dataBasic[4]);
			    	info.put("city", dataDetailed[0]);
			    	info.put("music", dataDetailed[1]);
			    	info.put("civil_state",dataDetailed[2]);
			    	info.put("drink",dataDetailed[3]);
			    	info.put("about", dataDetailed[4]);

			    	info.put("gender", dataBasic[5]);
			    	
			    	return info;
			    }
		};
		
		requestQueue.add(request);
	}
	


	@Override
	public Intent getParentActivityIntent() {
		Intent intent = new Intent(this, MainActivity.class);
		return intent;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		setContent(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
	
	
	
}
