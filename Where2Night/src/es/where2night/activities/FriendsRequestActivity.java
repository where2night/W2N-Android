package es.where2night.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.adapters.AdapterItemFriendList;
import es.where2night.data.ItemFriend;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class FriendsRequestActivity extends Activity {
	
	 private RequestQueue requestQueue;
	 private ProgressDialog connectionProgressDialog;
	 ArrayList<ItemFriend> arraydir;
	 AdapterItemFriendList adapter;
	// TextView friendshipPets;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friends);
		
		connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Cargando tu perfil...");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(R.drawable.logo7);
        
		ListView lista = (ListView) findViewById(R.id.friendList);
	//	friendshipPets = (TextView) view.findViewById(R.id.textNumberFriendRequest);
        arraydir = new ArrayList<ItemFriend>();
        
        //TODO      
        
	    adapter = new AdapterItemFriendList(this, arraydir);
        lista.setAdapter(adapter);
        
        lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(FriendsRequestActivity.this, LocalViewActivity.class);
                intent.putExtra(LocalViewActivity.ID, String.valueOf(adapter.getItemId(position)));
                startActivity(intent);
			}
			
		});
		
        getFriendshipRequest();
	}
	
	
	private void getFriendshipRequest() {
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getFriendshipPetUrl() + "/" + cred[0] + "/" + cred[1];
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            		JSONObject root = new JSONObject(response);
	            		int requests = Integer.parseInt(root.getString("numPetitions"));
		            	for (int i = 0; i < requests; i++){
		            		JSONObject aux = root.getJSONObject(String.valueOf(i));
			            	long idProfile = Long.valueOf(aux.getString("idProfile"));
			            	String picture = aux.getString("picture");
			            	picture = picture.replace("\\", "");
			            	String name = aux.getString("name") + " " +  aux.getString("surnames");
			            	ItemFriend friend = new ItemFriend(picture,name,"0","0","0",idProfile);
			            	arraydir.add(friend);
		            	}
		            	connectionProgressDialog.dismiss();
		            	adapter.notifyDataSetChanged();
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
	public Intent getParentActivityIntent() {
		Intent intent = new Intent(this, MainActivity.class);
		return intent;
	}
	
}