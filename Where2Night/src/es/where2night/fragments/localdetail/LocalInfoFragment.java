package es.where2night.fragments.localdetail;

import org.json.JSONObject;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.fragments.MapFragment;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalInfoFragment extends Fragment implements OnClickListener{

	private NetworkImageView imgLocal;
	private TextView txtLocalFollowers;
	private TextView txtOpening;
	private TextView txtClosing;
	private TextView txtMusicType;
	private TextView txtEntryPrice;
	private TextView txtDrinkPrice;
	private Button btnFollowMe;
	private ProgressBar pgLocalView;
	private String latitude, longitude;
	
	
	private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    String localId;
    private ImageLoader imageLoader;
    
    private MapFragment mapFragment;
	int followers = 0;
	public LocalInfoFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		View view = inflater.inflate(R.layout.fragment_local_info, container, false);
		
		imgLocal = (NetworkImageView) view.findViewById(R.id.imgLocal);
		txtLocalFollowers = (TextView) view.findViewById(R.id.txtLocalFollowers);
		txtOpening = (TextView) view.findViewById(R.id.txtOpening);
		txtClosing = (TextView) view.findViewById(R.id.txtClosing);
		txtMusicType = (TextView) view.findViewById(R.id.txtMusicType);
		txtEntryPrice = (TextView) view.findViewById(R.id.txtEntryPrice);
		txtDrinkPrice = (TextView) view.findViewById(R.id.txtDrinkPrice);
		btnFollowMe = (Button) view.findViewById(R.id.btnFollowMe);
		pgLocalView = (ProgressBar) view.findViewById(R.id.pgLocalView);
		pgLocalView.setVisibility(View.VISIBLE);
		btnFollowMe.setOnClickListener(this);
		
		mapFragment = new MapFragment();
		
		FragmentManager manager = getChildFragmentManager();
        manager.beginTransaction()
        	    				.add(R.id.LocalInfoMap, mapFragment)
        	    				.commit();
		
        fillData();
        
		return view;
		
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnFollowMe.getId()){
			 if (btnFollowMe.isSelected()){
				btnFollowMe.setSelected(false);
         		btnFollowMe.setText(getResources().getString(R.string.FollowMe));
         		followers--;
			 	follow(true);
			 }
			 else{
				 btnFollowMe.setSelected(true);
         		 btnFollowMe.setText(getResources().getString(R.string.Following));
         		 followers++;
				 follow(false);
			 }
		 }
	}
	

private void fillData() {		
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getLocalUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		
		imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
	            	pgLocalView.setVisibility(View.GONE);
		            respuesta = new JSONObject(response);
		            String localName = respuesta.getString("localName");
		            getActivity().getActionBar().setTitle(localName);
		            
		            txtLocalFollowers.setText(getResources().getString(R.string.Followers) + respuesta.getString("followers"));
		            txtOpening.setText(getResources().getString(R.string.OpeningHour) + respuesta.getString("openingHours"));
		    		txtClosing.setText(getResources().getString(R.string.ClosingHour) + respuesta.getString("closeHours"));
		    		txtMusicType.setText(getResources().getString(R.string.MusicLocal) + respuesta.getString("music"));
		    		txtEntryPrice.setText(getResources().getString(R.string.EntryPrice) + respuesta.getString("entryPrice"));
		    		txtDrinkPrice.setText(getResources().getString(R.string.DrinkPrice) + respuesta.getString("drinkPrice"));
		    		followers = Integer.valueOf(respuesta.getString("followers"));
		    		String pictureUrl = respuesta.getString("picture");
		    		
		    		latitude = respuesta.getString("latitude");
		    		longitude = respuesta.getString("longitude");
		    		
		    		String following = respuesta.getString("follow");
		    		
		    		if (following.equals("1"))
		    			btnFollowMe.setSelected(true);
		    		imgLocal.setImageUrl(pictureUrl, imageLoader);
		    		if (respuesta.getString("goto").equals("1")){
		    			LocalViewActivity.btnIGo.setSelected(true);
		    			LocalViewActivity.btnIGo.setText(getResources().getString(R.string.Going));
		    		}
		            mapFragment.fillMap(latitude,longitude,localName,Long.parseLong(localId));
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

	

	private void follow(boolean unfollow){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
		String url = Helper.getFollowUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		 
		 
		Response.Listener<String> succeedListener = new Response.Listener<String>(){
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	respuesta = new JSONObject(response);
            	 if (respuesta.getString("follow").equals("error")){
            		if (btnFollowMe.isSelected()){
            			btnFollowMe.setSelected(false);
                 		btnFollowMe.setText(getResources().getString(R.string.FollowMe));
                 		followers--;
            		}else {
            			btnFollowMe.setSelected(true);
                		btnFollowMe.setText(getResources().getString(R.string.Following));
                		followers++;
            		}
            	}
            	txtLocalFollowers.setText(getResources().getString(R.string.Followers) + 
		            					String.valueOf(followers));
            	}
	            catch (Exception e) {
					e.printStackTrace();
				}
	        
	        }
		};
		Response.ErrorListener errorListener = new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
             // error
				Log.e("Error.Response", error.toString());
			}
		};
		
		StringRequest request;
		
		if(unfollow){
			request = new StringRequest(Request.Method.DELETE, url, succeedListener, errorListener); 
		}
		else{
			request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 

		}
		requestQueue.add(request);
	}
		
}
