package es.where2night.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.data.LocalListData;

public class MapFragment extends Fragment implements OnMapClickListener {
	
	public static final String LAT = "latitude";
	public static final String LONG = "longitude";
	public static final String NAME = "localName";
	
	LatLng position;
	private GoogleMap mapa;
	
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
		for (LocalListData local: localListData){
			String latitude = local.getLatitude();
			String longitude = local.getLongitude();
			try{
				fillMap(latitude,longitude,local.getName());
			}catch(NumberFormatException e){}
			
		}
	}
	
	public void fillMap(String latitude2, String longitude2, String localName) throws NumberFormatException
	{
		
		float latitude = Float.valueOf(latitude2);
		float longitude = Float.valueOf(longitude2);
		String title = localName;
		
		position = new LatLng(latitude, longitude);
		
		
		
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
	      mapa.setMyLocationEnabled(true);
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
	
}
