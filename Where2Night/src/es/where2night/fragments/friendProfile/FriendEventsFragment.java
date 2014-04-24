package es.where2night.fragments.friendProfile;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.adapters.AdapterItemEvent;
import es.where2night.data.ItemEvent;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FriendEventsFragment extends Fragment{
	
	private String friendId;
	private RequestQueue requestQueue;
    private ArrayList<ItemEvent> arraydir;
    private AdapterItemEvent adapter;
	private ProgressBar pgEventList;
	private ListView list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		list = (ListView) view.findViewById(R.id.eventList);
		pgEventList = (ProgressBar) view.findViewById(R.id.pgEventList);
		friendId = getArguments().getString(FriendViewActivity.ID);
		
		return view;
	}
	
	public void fill(){
		
		arraydir = new ArrayList<ItemEvent>();
	    adapter = new AdapterItemEvent(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    fillData();
	}
	
	private void fillData() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getEventsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + friendId;
		
		Log.e("url", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
		            
		            	
		          
		    		
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