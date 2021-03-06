package es.where2night.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
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

public class FriendsFragment extends Fragment {
	
	 private RequestQueue requestQueue;
	 private ProgressDialog connectionProgressDialog;
	 ArrayList<ItemFriend> arraydir;
	 AdapterItemFriendList adapter;
	 ListView lista;
	// TextView friendshipPets;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
		
		connectionProgressDialog = new ProgressDialog(getActivity());
        connectionProgressDialog.setMessage("Cargando tu perfil...");
        
		lista = (ListView) view.findViewById(R.id.listFriendsList);
        arraydir = new ArrayList<ItemFriend>();
        
        //TODO      
        
	    adapter = new AdapterItemFriendList(getActivity(), arraydir);
        lista.setAdapter(adapter);
        
        lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), FriendViewActivity.class);
                intent.putExtra(FriendViewActivity.ID, String.valueOf(adapter.getItemId(position)));
                startActivity(intent);
			}
			
		});
		
        getFriendList();
		return view;
	}
	
	public void fill(){
		arraydir = new ArrayList<ItemFriend>();
	    adapter = new AdapterItemFriendList(getActivity(), arraydir);
        lista.setAdapter(adapter);
        getFriendList();
		
	}
	
	
	public void getFriendList() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getMyFriendListUrl() + "/" + cred[0] + "/" + cred[1] + "/" + cred[0];
		Log.e("Friends", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            		JSONObject root = new JSONObject(response);
		            	for (int i = 0; i < root.length() - 2 ; i++){
		            		JSONObject aux = new JSONObject(root.getString(String.valueOf(i)));
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
	
	private void getFriendshipRequest() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getFriendshipPetUrl() + "/" + cred[0] + "/" + cred[1];
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            		JSONObject root = new JSONObject(response);
	        //    		friendshipPets.setText(root.getString("numPetitions"));
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
