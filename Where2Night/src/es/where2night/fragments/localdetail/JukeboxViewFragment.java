package es.where2night.fragments.localdetail;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class JukeboxViewFragment extends Fragment {
	
	private String localId = "";
	private boolean checkIn;
	private RequestQueue requestQueue;
    private ArrayList<ItemSong> arraydir;
    private AdapterItemSong adapter;
	private ProgressBar pgEventList;
	private ListView list;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try{
			localId = getArguments().getString(LocalViewActivity.ID);
		}catch(Exception e){}
		
		checkIn = false;
		if (localId.equals("")){
			checkCheckIn();
		}
		//checkIn = getArguments().getString(CHECKIN);
		
		
		View view = inflater.inflate(R.layout.fragment_jukebox_view, container, false);
		list = (ListView) view.findViewById(R.id.listSongs);
		pgEventList = (ProgressBar) view.findViewById(R.id.pgEventList);
		arraydir = new ArrayList<ItemSong>();
		adapter = new AdapterItemSong(getActivity(), arraydir);
		list.setAdapter(adapter);
		return view;
	}
	
	public void fill(){
		
		if(checkIn && localId != ""){
		    arraydir = new ArrayList<ItemSong>();
		    adapter = new AdapterItemSong(getActivity(), arraydir);
		    list.setAdapter(adapter);
		    fillData();
		}
		else{
			pgEventList.setVisibility(View.GONE);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("No has hecho CheckIn")
			        .setTitle("Error en el registro")
			        .setCancelable(false)
			        .setNeutralButton("Aceptar",
			                new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int id) {
			                        dialog.cancel();
			                    }
			                });
			AlertDialog alert = builder.create();
			alert.show();
			pgEventList.setVisibility(View.VISIBLE);
			
			fillData();
		}
	   
	}
	
	
	private void fillData() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getSongsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("url", url);
		
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
			            	boolean vot;
			            	if (voted.equals("null")) vot = false;
			            	else vot = true;
			            	ItemSong song = new ItemSong(title,artist,Integer.parseInt(votes),Long.parseLong(idTrack),vot,checkIn,Long.parseLong(localId));
			            	arraydir.add(song);
		            	}
		            adapter.notifyDataSetChanged();
		            pgEventList.setVisibility(View.GONE);
		    		
				} catch (Exception e) {
					pgEventList.setVisibility(View.GONE);
					e.printStackTrace();
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
		            		localId = id;
		            		checkIn = true;
		            		fillData();
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
}
