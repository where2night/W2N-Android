package es.where2night.fragments.friendProfile;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private Button btnIgnoreFriend;
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
		btnIgnoreFriend = (Button) view.findViewById(R.id.btnIgnoreFriend);
        btnAddAsFriend.setOnClickListener(this);
        btnIgnoreFriend.setOnClickListener(this);
        
       fillData();
        
       return view;
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnAddAsFriend.getId()){
			 friend(true);
			 if (friends == 0){
				 btnAddAsFriend.setEnabled(false);
			 }else {
				 btnAddAsFriend.setVisibility(View.GONE);
				 btnIgnoreFriend.setVisibility(View.GONE); 
			 }
			
		 }else  if (v.getId() == btnIgnoreFriend.getId()){
			 friend(false);
			 btnAddAsFriend.setText(getResources().getString(R.string.AddAsFriend));
			 btnIgnoreFriend.setVisibility(View.GONE);
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
		            friends = Integer.parseInt(respuesta.getString("modefriend"));
		            pictureUrl = respuesta.getString("picture");
		            if (pictureUrl.equals("") || pictureUrl.contains("face"))
		    			pictureUrl = Helper.getDefaultProfilePictureUrl();
		            
		            imgFriend.setImageUrl(pictureUrl, imageLoader);
		            
		            
		            switch (friends) {
					case 0:
						btnIgnoreFriend.setVisibility(View.GONE);
						break;
					case 1:
						btnAddAsFriend.setText(getResources().getString(R.string.AcceptFriendRequest));
						btnIgnoreFriend.setText(getResources().getString(R.string.IgnoreFriendRequest));	
						btnIgnoreFriend.setVisibility(View.VISIBLE);
						break;
					case 3:
						btnAddAsFriend.setEnabled(false);
						btnIgnoreFriend.setVisibility(View.GONE);
						break;
					case 4:
						btnIgnoreFriend.setVisibility(View.GONE);
						btnAddAsFriend.setVisibility(View.GONE);
						break;

					default:
						break;
					}
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

private void friend(boolean accept){
	
	final DataManager dm = new DataManager(getActivity().getApplicationContext());
	String[] cred = dm.getCred();
	requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
	String url = Helper.getFriendshipResponseUrl() + "/" + cred[0] + "/" + cred[1] + "/" + friendId;
	 
	 
	Response.Listener<String> succeedListener = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            // response
        	Log.e("Response", response);
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
	
	if(accept){
		if (friends == 0)
			request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		else
			request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener); 
	}
	else{
		request = new StringRequest(Request.Method.DELETE, url, succeedListener, errorListener); 

	}
	requestQueue.add(request);
}


@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.friend_view_activty, menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.delete_friend) {
		return true;
	}
	return super.onOptionsItemSelected(item);
}


}
