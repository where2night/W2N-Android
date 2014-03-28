package es.where2night.fragments.djdetail;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.DjViewActivty;
import es.where2night.activities.LocalViewActivity;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class DJInfoFragment extends Fragment implements OnClickListener{
	
	private NetworkImageView imgDj;
	private TextView txtNameAndSurnameDj;
	private TextView txtDjFollowers;
	private TextView txtBirthdayDj;
	private TextView txtMusicDj;
	private TextView txtDescriptionDj;
	private ProgressBar pgDJView;
	
	
	private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    String djId;
    private ImageLoader imageLoader;
	
	private Button btnFollowMe;
	private int followers = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dj_info, container, false);

		djId = getArguments().getString(DjViewActivty.ID);
		
		imgDj = (NetworkImageView) view.findViewById(R.id.imgDj);
		txtNameAndSurnameDj = (TextView) view.findViewById(R.id.txtNameAndSurnameDj);
		txtDjFollowers = (TextView) view.findViewById(R.id.txtDjFollowers);
		txtBirthdayDj = (TextView) view.findViewById(R.id.txtBirthdayDj);
		txtMusicDj = (TextView) view.findViewById(R.id.txtMusicDj);
		txtDescriptionDj = (TextView) view.findViewById(R.id.txtDescriptionDj);
		pgDJView = (ProgressBar) view.findViewById(R.id.pgDJView);
		btnFollowMe = (Button) view.findViewById(R.id.btnFollowMe);
        btnFollowMe.setOnClickListener(this);
        
        fillData();
        
		return view;
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnFollowMe.getId()){
			 if (btnFollowMe.isSelected()){
			 	follow(true);
			 }
			 else{
				 btnFollowMe.setSelected(true);
				 follow(false);
			 }
		 }
	}
	

private void fillData() {
		
		
		
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getDJUrl() + "/" + cred[0] + "/" + cred[1] + "/" + djId;
		
		imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
	            	pgDJView.setVisibility(View.GONE);
		            respuesta = new JSONObject(response);
		            String DJName = respuesta.getString("nameDJ");
		            getActivity().getActionBar().setTitle(DJName);
		            
		            txtDjFollowers.setText(getResources().getString(R.string.Followers) + 
		            					respuesta.getString("followers"));
		            followers = Integer.valueOf(respuesta.getString("followers"));
		            txtNameAndSurnameDj.setText(getResources().getString(R.string.NameAndSurnameDj) + 
		            					respuesta.getString("name") + " " + respuesta.getString("surname"));
		            String[] date = respuesta.getString("birthdate").split("/");
		            txtBirthdayDj.setText(getResources().getString(R.string.Birthday) + 
		            		date[2] + "/" + date[1] + "/" + date[0]);
		            txtMusicDj.setText(getResources().getString(R.string.MusicDj) + 
		            					respuesta.getString("music"));
		            txtDescriptionDj.setText( respuesta.getString("about"));
		    		
		    		String pictureUrl = respuesta.getString("picture");
		    		
		    		
		    		String following = respuesta.getString("follow");
		    		
		    		if (following.equals("1"))
		    			btnFollowMe.setSelected(true);
		    		
		    		if (pictureUrl.equals(""))
		    			pictureUrl = Helper.getDefaultProfilePictureUrl();
		    			pictureUrl = pictureUrl.replace("\\", "");
		    			imgDj.setImageUrl(pictureUrl, imageLoader);
		    		
		    		
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
	        	 pgDJView.setVisibility(View.GONE);
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
	String url = Helper.getFollowUrl() + "/" + cred[0] + "/" + cred[1] + "/" + djId;
	 
	 
	Response.Listener<String> succeedListener = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            // response
        	Log.e("Response", response);
            try {
            	respuesta = new JSONObject(response);
            	if (respuesta.getString("follow").equals("true")){
            		btnFollowMe.setSelected(true);
            		btnFollowMe.setText(getResources().getString(R.string.Following));
            		followers++;
            	}else if (respuesta.getString("follow").equals("false")){
            		btnFollowMe.setSelected(false);
            		btnFollowMe.setText(getResources().getString(R.string.FollowMe));
            		followers--;
            	}
            	txtDjFollowers.setText(getResources().getString(R.string.Followers) + 
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