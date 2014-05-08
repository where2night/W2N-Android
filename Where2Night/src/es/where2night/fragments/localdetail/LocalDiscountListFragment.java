package es.where2night.fragments.localdetail;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemDiscountList;
import es.where2night.data.ItemDiscountList;
import es.where2night.data.ItemEvent;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalDiscountListFragment extends Fragment {
	
	private String localId;
	AdapterItemDiscountList adapter;
	ListView list;
	private RequestQueue requestQueue;
	ArrayList<ItemDiscountList> arraydir;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		list = (ListView) view.findViewById(R.id.eventList);
	    arraydir = new ArrayList<ItemDiscountList>();
	        
	    
	    adapter = new AdapterItemDiscountList(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    
	    fill();
		return view;
	}
	
	private void fill() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getDiscountListUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
		            	JSONObject root = new JSONObject(response);
		            	
		            	for (int i = 0; i < root.length() - 3; i++){
			            	JSONObject aux = root.getJSONObject(String.valueOf(i));
			            	String title = aux.getString("title");
			            	String text = aux.getString("about");
			            	String[] dateArr = aux.getString("date").split("-");
			            	String date = dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0];
			            	String start = aux.getString("startHour");
			            	String close = aux.getString("closeHour");
			            	long id = Long.valueOf(aux.getString("idLists"));
			            	
			            	//String goes = aux.getString("GOES");
			            	boolean going = false;
			            //	if (!goes.equals("null"))
			            //		going = true;
			            	
			            	ItemDiscountList event = new ItemDiscountList(id,title,date,going);
			            	arraydir.add(event);
		            	}
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
	
}
