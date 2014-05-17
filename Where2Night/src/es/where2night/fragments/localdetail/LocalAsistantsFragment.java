package es.where2night.fragments.localdetail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemFriendList;
import es.where2night.data.ItemFriend;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

public class LocalAsistantsFragment extends Fragment{
	
	private String localId;
	ListView lista;
	ArrayList<ItemFriend> arraydir;
	AdapterItemFriendList adapter;
	private RequestQueue requestQueue;
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		localId = getArguments().getString(LocalViewActivity.ID);
		View view = inflater.inflate(R.layout.fragment_local_asistants, container, false);
		lista = (ListView) view.findViewById(R.id.listAsistants);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		arraydir = new ArrayList<ItemFriend>();
		
	    adapter = new AdapterItemFriendList(getActivity(), arraydir);
        lista.setAdapter(adapter);
        
        lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), FriendViewActivity.class);
                intent.putExtra(LocalViewActivity.ID, String.valueOf(adapter.getItemId(position)));
                startActivity(intent);
			}
			
		});
		
		return view;
	}
	
	public void fill(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getStatisticsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("Asistants", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
		            	JSONObject root = new JSONObject(response);
		            	int maximo = Integer.parseInt(root.getString("rows"))-1;
		            	for (int i = 0; i < maximo; i++){
		            		JSONObject aux = root.getJSONObject(((Integer)(i)).toString());
			            	long idProfile = Long.valueOf(aux.getString("idProfile"));
			            	String picture = aux.getString("picture");
			            	picture = picture.replace("\\", "");
			            	String name = aux.getString("name") + " " +  aux.getString("surnames");
			            	ItemFriend friend = new ItemFriend(picture,name,"0","0","0",idProfile);
			            	arraydir.add(friend);
		            	}
		            	progressBar.setVisibility(View.GONE);
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
	             progressBar.setVisibility(View.GONE);
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
}
