package es.where2night.fragments.myProfile;

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

public class FriendInfoFragment extends Fragment {
	
	private NetworkImageView imgFriend;
	private TextView txtNameAndSurnameFriend;
	private TextView txtBirthdayFriend;
	private TextView txtMusicFriend;
	private TextView txtCivilStateFriend;
	private TextView txtCityFriend;
	private TextView txtDrinkFriend;
	private TextView txtAboutFriend;
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
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_info, container, false);

		
		imgFriend = (NetworkImageView) view.findViewById(R.id.imgFriend);
		txtNameAndSurnameFriend = (TextView) view.findViewById(R.id.txtNameAndSurnameFriend);
		txtBirthdayFriend = (TextView) view.findViewById(R.id.txtBirthdayFriend);
		txtCivilStateFriend = (TextView) view.findViewById(R.id.txtCivilStateFriend);
		txtMusicFriend = (TextView) view.findViewById(R.id.txtMusicFriend);
		txtCityFriend = (TextView) view.findViewById(R.id.txtCityFriend);
		txtDrinkFriend = (TextView) view.findViewById(R.id.txtDrinkFriend);
		txtAboutFriend = (TextView) view.findViewById(R.id.txtAboutFriend);
		pgFriendView = (ProgressBar) view.findViewById(R.id.pgFriendView);
	
        
      fillData();
        
       return view;
	}
	
	
	
	
private void fillData() {
		
		
		
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		idProfile = cred[0];
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1] + "/" + cred[0];
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
	            	String name = respuesta.getString("name") + " " + respuesta.getString("surnames");
	            	txtNameAndSurnameFriend.setText(name);
		            String[] date = respuesta.getString("birthdate").split("/");
		            txtBirthdayFriend.setText("Fecha de nacimiento: " + date[2] + "/" + date[1] + "/" + date[0]);
		            txtMusicFriend.setText("Mi música favorita es: " + respuesta.getString("music"));
		        	txtCivilStateFriend.setText("Estado 'civil' actual: " + respuesta.getString("civil_state"));
		        	txtCityFriend.setText("Ciudad actual: " + respuesta.getString("city"));
		        	txtDrinkFriend.setText("Mi bebida favorita es: " + respuesta.getString("drink"));
		        	txtAboutFriend.setText("Algo más sobre mi: " + respuesta.getString("about"));
		        	
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



}
