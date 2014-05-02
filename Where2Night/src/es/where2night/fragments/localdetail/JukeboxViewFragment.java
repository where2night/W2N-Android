package es.where2night.fragments.localdetail;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;
import com.where2night.R.id;
import com.where2night.R.layout;
import com.where2night.R.menu;

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemEvent;
import es.where2night.adapters.AdapterItemSong;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemSong;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Build;

public class JukeboxViewFragment extends Fragment {
	
	private String localId;
	private RequestQueue requestQueue;
    private ArrayList<ItemSong> arraydir;
    private AdapterItemSong adapter;
	private ProgressBar pgEventList;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		View view = inflater.inflate(R.layout.fragment_jukebox_view, container, false);
		list = (ListView) view.findViewById(R.id.listSongs);
		pgEventList = (ProgressBar) view.findViewById(R.id.pgEventList);
		
		return view;
	}
	
	public void fill(){
		
	    arraydir = new ArrayList<ItemSong>();
	    adapter = new AdapterItemSong(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    fillData();
	   
	}
	
	
	private void fillData() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getSongsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("url", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
		            	JSONObject root = new JSONObject(response);
		            	
		            	for (int i = 0; i < root.length(); i++){
			            	JSONObject aux = root.getJSONObject(String.valueOf(i));
			            	String title = aux.getString("title");
			            	String artist = aux.getString("artist");
			            	String votes = aux.getString("votes");
			            	String voted = aux.getString("voted");
			            	boolean vot = true;
			            	if (voted.equals("0")) vot = false;
			            	ItemSong song = new ItemSong(title,artist,Integer.parseInt(votes),vot);
			            	arraydir.add(song);
		            	}
		            adapter.notifyDataSetChanged();
		            pgEventList.setVisibility(View.GONE);
		    		
				} catch (Exception e) {
					pgEventList.setVisibility(View.GONE);
					e.printStackTrace();
				}
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEventList.setVisibility(View.GONE);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	

}
