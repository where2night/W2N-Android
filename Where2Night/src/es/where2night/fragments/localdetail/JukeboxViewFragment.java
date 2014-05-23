package es.where2night.fragments.localdetail;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemSong;
import es.where2night.data.ItemSong;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class JukeboxViewFragment extends Fragment {
	
	private String localId = "";
	private boolean checkIn;
	private RequestQueue requestQueue;
    private ArrayList<ItemSong> arraydir;
    private AdapterItemSong adapter;
	private ProgressBar pgEventList;
	private ListView list;
	
	private LinearLayout layoutNowPlaying;
	private TextView txtSongName;
	private TextView txtArtistName;
	double latLocal = 0;
	double lngLocal = 0;
	String localName;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		try{
			localId = getArguments().getString(LocalViewActivity.ID);
		}catch(Exception e){
			checkGoingSomeWhere();
		}
		
		checkIn = false;
		
		
		View view = inflater.inflate(R.layout.fragment_jukebox_view, container, false);
		list = (ListView) view.findViewById(R.id.listSongs);
		layoutNowPlaying = (LinearLayout) view.findViewById(R.id.layoutNowPlaying);
		txtSongName = (TextView) view.findViewById(R.id.txtSongName);
		txtArtistName = (TextView) view.findViewById(R.id.txtArtistName);
		pgEventList = (ProgressBar) view.findViewById(R.id.pgEventList);
		layoutNowPlaying.setVisibility(View.GONE);
		return view;
	}
	
	public void fill(){
		
		arraydir = new ArrayList<ItemSong>();
		adapter = new AdapterItemSong(getActivity(), arraydir);
		list.setAdapter(adapter);
		if (checkIn)
			fillData();
		else
			checkCheckIn();
		
	
	   
	}
	
	private void checkGoingSomeWhere() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getWereUserGoesTodayUrl() + "/" + cred[0] + "/" + cred[1];
		Log.e("CheckIn", url);
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	localName = respuesta.getString("localName");
		            	latLocal = Double.valueOf(respuesta.getString("latitude"));
		            	lngLocal = Double.valueOf(respuesta.getString("longitude"));
		            	localId = respuesta.getString("idProfile");
		            	
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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

	public void fillData() {
		pgEventList.setVisibility(View.VISIBLE);
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getSongsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("url", url);
		layoutNowPlaying.setVisibility(View.GONE);
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
			            	String title = aux.getString("trackName");
			            	String artist = aux.getString("trackArtist");
			            	String votes = aux.getString("votes");
			            	String idTrack = aux.getString("idTrack");
			            	String voted = aux.getString("VOTE");
			            	String playing = aux.getString("playing");
			            	boolean vot;
			            	if (voted.equals("null")) vot = false;
			            	else vot = true;
			            	if(playing.equals("0")){
			            		ItemSong song = new ItemSong(title,artist,Integer.parseInt(votes),Long.parseLong(idTrack),vot,checkIn,Long.parseLong(localId));
			            		arraydir.add(song);
			            	}else{
			            		txtSongName.setText(title);
			            		txtArtistName.setText(artist);
			            		layoutNowPlaying.setVisibility(View.VISIBLE);
			            	}
		            	}
		            adapter.notifyDataSetChanged();
		            pgEventList.setVisibility(View.GONE);
		    		
				} catch (Exception e) {
					pgEventList.setVisibility(View.GONE);
					e.printStackTrace();
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			 		builder.setMessage("No vas a ir a ningun sitio hoy ni has hecho checkIn, con lo cual no puedes ver la gramola.")
			 		        .setTitle("Error")
			 		        .setCancelable(false)
			 		        .setNeutralButton("Aceptar",
			 		                new DialogInterface.OnClickListener() {
			 		                    public void onClick(DialogInterface dialog, int id) {
			 		                        dialog.cancel();
			 		                    }
			 		                });
			 		AlertDialog alert = builder.create();
			 		alert.show();
				}
	        }
	    };
	    
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEventList.setVisibility(View.GONE);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	
	private void checkCheckIn(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getCheckInUrl() + "/" + cred[0] + "/" + cred[1];

		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	String id = respuesta.getString("id");
		            	if (!id.equals("null")){
		            		if (localId.equals("") || id.equals(localId)){
		            			checkIn = true;
		            			fillData();
		            			localId = id;
		            		}else{
		            			fillData();
		            			checkIn = false;
		            		}
		            		
		            	}else{
		            		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        			builder.setMessage("Necesitas hacer CheckIn en " + localName + " para votar las canciones")
		        			        .setTitle("No puedes votar aún")
		        			        .setCancelable(false)
		        			        .setNegativeButton("No deseo hacerlo",
		            	                new DialogInterface.OnClickListener() {
		            	                    public void onClick(DialogInterface dialog, int id) {
		            	                    	pgEventList.setVisibility(View.GONE);
		            	                    	fillData();
		            	                    }
		            	                })
		            	        .setPositiveButton("Hacer CheckIn",
		            	                new DialogInterface.OnClickListener() {
		            	                    public void onClick(DialogInterface dialog, int id) {
		            	                    	hacerCheckIn();
		            	                    }
		            	                });
		        			AlertDialog alert = builder.create();
		        			alert.show();
		            	}
		            		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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

	public void hacerCheckIn(){
		
		LocationManager lm = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		  // Build the alert dialog
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  builder.setTitle("Check In");
		  builder.setMessage("Para hacer Check In debes habilitar el GPS");
		  builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialogInterface, int i) {
		    // Show location settings when the user acknowledges the alert dialog
		    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    startActivity(intent);
		    }
		  });
		  builder.setNegativeButton("Cancelar",null);
		  Dialog alertDialog = builder.create();
		  alertDialog.setCanceledOnTouchOutside(false);
		  alertDialog.show();
		}else{
			doCheckIn(lm);
		}
	}
	
	public boolean doCheckIn (LocationManager lm){
		Location locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    Location best;
	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }
	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	       best = locationGPS;
	    	
	    }
	    else {
	        best = locationNet;
	    }
	    double lat = best.getLatitude();
	    double lng = best.getLongitude();
	    
	    if ((lat < latLocal + 0.0002) &&
	    	(lat > latLocal - 0.0002) &&	
	    	(lng < lngLocal + 0.0002) &&	
	    	(lng > lngLocal - 0.0002) ){
	    	
	    	registerCheckIn();
	    	checkIn = true;
	    	fillData();
	    	Toast.makeText(getActivity(), "CheckIn", Toast.LENGTH_SHORT).show();
	    	return true;
	    }
	    return false;
	}
	
	private void registerCheckIn(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getCheckInUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("CheckIn", url);
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	String error = respuesta.getString("error");
		            	if (error.equals("0")){
		            	}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
		
		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
}
