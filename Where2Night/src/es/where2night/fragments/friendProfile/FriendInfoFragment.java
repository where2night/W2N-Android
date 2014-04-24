package es.where2night.fragments.friendProfile;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

import es.where2night.activities.FriendViewActivity;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class FriendInfoFragment extends Fragment implements OnClickListener {
	
	private NetworkImageView imgFriend;
	private TextView txtNameAndSurnameFriend;
	private TextView txtNumberOfFriends;
	private TextView txtBirthdayFriend;
	private TextView txtMusicFriendLike;
	private TextView txtDescriptionFriend;
	private ProgressBar pgFriendView;
	
	private Button btnAddAsFriend;
	private int friends = 0;
	
		private ImageLoader imageLoader;
		
	    private String pictureUrl;	
		private String idProfile;
		private String picturePath;
		
		String encodedImage = "";
		
		private RequestQueue requestQueue;
	
	String friendId;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_info, container, false);

		friendId = getArguments().getString(FriendViewActivity.ID);
		
		imgFriend = (NetworkImageView) view.findViewById(R.id.imgFriend);
		txtNameAndSurnameFriend = (TextView) view.findViewById(R.id.txtNameAndSurnameFriend);
		txtNumberOfFriends = (TextView) view.findViewById(R.id.txtNumberOfFriends);
		txtBirthdayFriend = (TextView) view.findViewById(R.id.txtBirthdayFriend);
		txtMusicFriendLike = (TextView) view.findViewById(R.id.txtMusicFriendLike);
		txtDescriptionFriend = (TextView) view.findViewById(R.id.txtDescriptionFriend);
		pgFriendView = (ProgressBar) view.findViewById(R.id.pgFriendView);
		btnAddAsFriend = (Button) view.findViewById(R.id.btnAddAsFriend);
        btnAddAsFriend.setOnClickListener(this);
        
       fillData();
        
		return view;
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnAddAsFriend.getId()){
			 if (btnAddAsFriend.isSelected()){
			 	friends--;
			 }
			 else{
				 btnAddAsFriend.setSelected(true);
				 friends++;
			 }
		 }
	}
	
	
private void fillData() {
		
		
		
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		idProfile = cred[0];
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1] + "/" + friendId;
		Log.e("Edit", url);
		imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	pgFriendView.setVisibility(View.GONE);
	            	JSONObject respuesta = new JSONObject(response);
	            	txtNameAndSurnameFriend.setText(respuesta.getString("name") + " " + respuesta.getString("surnames"));
		            String[] date = respuesta.getString("birthdate").split("/");
		            txtBirthdayFriend.setText(date[2] + "/" + date[1] + "/" + date[0]);
		            txtMusicFriendLike.setText(respuesta.getString("music"));
		         /*   etEditDrink.setText(respuesta.getString("drink"));
		            etEditCivilState.setText(respuesta.getString("civil_state"));
		            etEditCity.setText(respuesta.getString("city"));*/
		            txtDescriptionFriend.setText(respuesta.getString("about"));
		            
		            pictureUrl = respuesta.getString("picture");
		            if (pictureUrl.equals("") || pictureUrl.contains("face"))
		    			pictureUrl = Helper.getDefaultProfilePictureUrl();
		            
		            imgFriend.setImageUrl(pictureUrl, imageLoader);
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
	        	 pgFriendView.setVisibility(View.GONE);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}

}
