package es.where2night.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.adapters.AdapterItemSearch;
import es.where2night.data.Item;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.data.ItemSearch;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class SearchResultsActivity extends Activity { 
	
	 private ProgressBar pgLocalsList;
	 private ListView list;
	 ArrayList<ItemSearch> arraydir;
	 AdapterItemSearch adapter;
	 private RequestQueue requestQueue;
	 
	   @Override
	   public void onCreate(Bundle savedInstanceState) { 
	      super.onCreate(savedInstanceState); 
	      setContentView(R.layout.fragment_list);
	      list = (ListView) findViewById(R.id.ListFragment);
	      pgLocalsList = (ProgressBar) findViewById(R.id.pgLocalsList);
	      Log.e("Search", "IN");
	      handleIntent(getIntent()); 
	      getActionBar().setDisplayHomeAsUpEnabled(true);
	      getActionBar().setIcon(R.drawable.logo7);
	      
	   } 
	   
	   @Override
		public Intent getParentActivityIntent() {
			Intent intent = new Intent(this, MainActivity.class);
	//		intent.putExtra(MainActivity.OPTION, "5");
			return intent;
		}
	   
	   public void fill(String query){
			
			arraydir = new ArrayList<ItemSearch>();
			
			
		    adapter = new AdapterItemSearch(this, arraydir);
		    list.setAdapter(adapter);
		    
		    list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					int type = ((ItemSearch)adapter.getItem(position)).getType();
					if (type == 0){
						Intent intent = new Intent(SearchResultsActivity.this, FriendViewActivity.class);
		                intent.putExtra(FriendViewActivity.ID, String.valueOf(adapter.getItemId(position)));
		                startActivity(intent);
					} else {
						Intent intent = new Intent(SearchResultsActivity.this, LocalViewActivity.class);
		                intent.putExtra(LocalViewActivity.ID, String.valueOf(adapter.getItemId(position)));
		                startActivity(intent);
					}
	                
				}
				
			});
		    fillData(query);
		}
		
		private void fillData(final String query) {
			final DataManager dm = new DataManager(getApplicationContext());
			String[] cred = dm.getCred();
			requestQueue = Volley.newRequestQueue(getApplicationContext()); 
			String url = Helper.getAllUsersUrl() + "/" + cred[0] + "/" + cred[1];
			
			
			Response.Listener<String> succeedListener = new Response.Listener<String>() 
		    {
		        @Override
		        public void onResponse(String response) {
		            // response
		        	Log.e("Response", response);
		            try {
			            	JSONArray root = new JSONArray(response);
			            	for (int i = 0; i < root.length(); i++){
			            		JSONObject aux = root.getJSONObject(i);
				            	long idProfile = Long.valueOf(aux.getString("idProfile"));
				            	String picture = aux.getString("picture");
				            	picture = picture.replace("\\", "");
				            	String name = aux.getString("name");
				            	int type = Integer.valueOf(aux.getString("type"));
				            	if (name.toUpperCase().contains(query)){
				            		ItemSearch local = new ItemSearch(picture,name,idProfile,type);
				            		arraydir.add(local);
				            	}
			            	}
			            	pgLocalsList.setVisibility(View.GONE);
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
	 
	   public void onNewIntent(Intent intent) { 
	      setIntent(intent); 
	      handleIntent(intent); 
	   } 
	   public void onListItemClick(ListView l, 
	      View v, int position, long id) { 
	   } 
	   private void handleIntent(Intent intent) { 
	      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
	         String query = 
	               intent.getStringExtra(SearchManager.QUERY); 
	         getActionBar().setTitle(query);
	         doSearch(query.toUpperCase()); 
	      } 
	   }    
	   private void doSearch(String query) { 
		   fill(query);
	   } 
	}
