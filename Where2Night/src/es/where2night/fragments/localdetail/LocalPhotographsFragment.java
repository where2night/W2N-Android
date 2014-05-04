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

import es.where2night.activities.FriendViewActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.activities.PhotoViewActivity;
import es.where2night.adapters.AdapterItemPhoto;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemPhoto;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class LocalPhotographsFragment extends Fragment{
	
	private String localId;
	private GridView grid;
	private RequestQueue requestQueue;
    private ArrayList<ItemPhoto> arraydir;
    private AdapterItemPhoto adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		View view = inflater.inflate(R.layout.fragment_photos, container, false);
		grid = (GridView) view.findViewById(R.id.gridViewPhotographs);
		
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String direccion = ((ItemPhoto)adapter.getItem(position)).getPhoto();
				Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
				intent.putExtra(PhotoViewActivity.URL, direccion);
				startActivity(intent);
				
			}
		});
		
		return view;
	}
	
	public void fill(){
		arraydir = new ArrayList<ItemPhoto>();
	    adapter = new AdapterItemPhoto(getActivity(), arraydir);
	    grid.setAdapter(adapter);
	    
	    final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getPhotosUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
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
		            	String direccion = aux.getString("direccion");
		            	ItemPhoto photo = new ItemPhoto(direccion);
		            	arraydir.add(photo);
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
