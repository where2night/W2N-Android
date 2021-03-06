package es.where2night.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemLocal;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.data.LocalListData;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalsListFragment  extends Fragment {
	ArrayList<ItemLocalAndDJ> arraydir;
	AdapterItemLocal adapter;
	private RequestQueue requestQueue;
    private ProgressBar pgLocalsList;
    private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		list = (ListView) view.findViewById(R.id.ListFragment);
		pgLocalsList = (ProgressBar) view.findViewById(R.id.pgLocalsList);
		
		return view;
	}
	
	public void fill(){
		
		arraydir = new ArrayList<ItemLocalAndDJ>();
		
		
	    adapter = new AdapterItemLocal(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    
	    list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), LocalViewActivity.class);
                intent.putExtra(LocalViewActivity.ID, String.valueOf(adapter.getItemId(position)));
                startActivity(intent);
			}
			
		});
	    fillData();
	}
	
	private void fillData() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getFollowedLocalsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + cred[0];
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	JSONObject root = new JSONObject(response);
		            	for (int i = 0; i < root.length()- 2; i++){
		            		JSONObject aux = root.getJSONObject(String.valueOf(i));
			            	long idProfile = Long.valueOf(aux.getString("idProfile"));
			            	String picture = aux.getString("picture");
			            	picture = picture.replace("\\", "");
			            	String name = aux.getString("localName");
			            	ItemLocalAndDJ local = new ItemLocalAndDJ(picture,name,idProfile);
			            	arraydir.add(local);
		            	}
		            	pgLocalsList.setVisibility(View.GONE);
		            	adapter.notifyDataSetChanged();
		            
		    		
				} catch (Exception e) {
					pgLocalsList.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
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

}
