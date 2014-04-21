package es.where2night.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.where2night.R;

import es.where2night.data.LocalListData;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class MapFragment extends Fragment implements OnMapClickListener {
	
	public static final String LAT = "latitude";
	public static final String LONG = "longitude";
	public static final String NAME = "localName";
	
	LatLng position;
	DataManager dm;
	private GoogleMap mapa;
	private RequestQueue requestQueue;
	private ArrayList<LocalListData> localListData = new ArrayList<LocalListData>();
	
	public MapFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		
		mapa = ((SupportMapFragment) getActivity().getSupportFragmentManager()
	            .findFragmentById(R.id.map)).getMap();
		
		return view;
	}
	
	public void allLocals(ArrayList<LocalListData> localListData){
		mapa.setMyLocationEnabled(true);
		for (LocalListData local: localListData){
			String latitude = local.getLatitude();
			String longitude = local.getLongitude();
			try{
				fillMap(latitude,longitude,local.getName());
			}catch(NumberFormatException e){}
			
		}
		LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
		      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		  // Build the alert dialog
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  builder.setTitle("¿Que hay a tu alrededor?");
		  builder.setMessage("Para saber que hay cerca de ti puedes activar la localización");
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
		}
		 mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
		            new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
		 
	}

private void getAllInfoFromServer() {
		
	try{
		dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getAllLocalsUrl() + "/" + cred[0] + "/" + cred[1];
		
		
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
			            	long idProfile = Long.valueOf(aux.getString("idProfile"));
			            	String picture = aux.getString("picture");
			            	String name = aux.getString("localName");
			            	String latitude = aux.getString("latitude");
			            	String longitude = aux.getString("longitude");
			            	LocalListData local = new LocalListData(idProfile,name,picture,latitude,longitude);
			            	localListData.add(local);
		            	}
		            	allLocals(localListData);
		            
		    		
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
		catch (Exception e){
	}
	}
	
	public void fillMap(String latitude2, String longitude2, String localName) throws NumberFormatException
	{
		
		float latitude = Float.valueOf(latitude2);
		float longitude = Float.valueOf(longitude2);
		String title = localName;
		
		position = new LatLng(latitude, longitude);
		
		
		
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
	     
	      mapa.getUiSettings().setZoomControlsEnabled(false);
	      mapa.getUiSettings().setCompassEnabled(true);
	      mapa.addMarker(new MarkerOptions()
	            .position(position)
	            .title(title)
	            .anchor(0.5f, 0.5f));
	}
	
	 public void moveCamera(View view) {
         mapa.moveCamera(CameraUpdateFactory.newLatLng(position));
   }
 
   public void animateCamera(View view) {
      if (mapa.getMyLocation() != null)
         mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
            new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
   }
 
   public void addMarker(View view) {
      mapa.addMarker(new MarkerOptions().position(
           new LatLng(mapa.getCameraPosition().target.latitude,
      mapa.getCameraPosition().target.longitude)));

   }
 
   @Override
   public void onMapClick(LatLng puntoPulsado) {
      mapa.addMarker(new MarkerOptions().position(puntoPulsado).
         icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
   }

public void fill() {
	getAllInfoFromServer();
	
}
	
}
