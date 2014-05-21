package es.where2night.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.activities.MessagesViewActivity;
import es.where2night.adapters.AdapterItemFriend;
import es.where2night.adapters.AdapterItemFriendList;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemFriend;
import es.where2night.utilities.BitmapLRUCache;
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

public class MessagesFragment extends Fragment{

	private ListView listFriends;
	private RequestQueue requestQueue;
	AdapterItemFriendList adapter;
	ArrayList<ItemFriend> arraydir;
	private ImageLoader imageLoader;
	private String idProfile;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_messages, container, false);
		listFriends = (ListView) view.findViewById(R.id.listFriendsMessages);
		arraydir = new ArrayList<ItemFriend>();
		adapter = new AdapterItemFriendList(getActivity(), arraydir);
		listFriends.setAdapter(adapter);
		
		listFriends.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), MessagesViewActivity.class);
                intent.putExtra(MessagesViewActivity.ID, String.valueOf(adapter.getItemId(position)));
                startActivity(intent);
			}
			
		});
		
		return view;
	}
	
	public void fill(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		idProfile = cred[0];
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getFriendsMessagesUrl() + "/" + cred[0] + "/" + cred[1];
		Log.e("Edit", url);
		imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
		
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
			            		String name = aux.getString("name") + " " + aux.getString("surnames");
			            		String picture = aux.getString("picture");
				            	picture = picture.replace("\\", "");
								long id = Integer.valueOf(aux.getString("idProfile"));
								ItemFriend friend = new ItemFriend(picture,name,"0","0","0",id);
								arraydir.add(friend);
			            	}
			            	adapter.notifyDataSetChanged();
			            }
			            catch(Exception e) {
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
