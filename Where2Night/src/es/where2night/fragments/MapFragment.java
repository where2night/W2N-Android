package es.where2night.fragments;

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

public class MapFragment extends Fragment implements OnMapClickListener {
	
	private final LatLng UPV = new LatLng(39.481106, -0.340987);
	private GoogleMap mapa;
	
	public MapFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		
		mapa = ((SupportMapFragment) getActivity().getSupportFragmentManager()
	            .findFragmentById(R.id.map)).getMap();
		
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));
	      mapa.setMyLocationEnabled(true);
	      mapa.getUiSettings().setZoomControlsEnabled(false);
	      mapa.getUiSettings().setCompassEnabled(true);
	      mapa.addMarker(new MarkerOptions()
	            .position(UPV)
	            .title("UPV")
	            .snippet("Universidad Politécnica de Valencia")
	            .anchor(0.5f, 0.5f));
		
		return view;
	}
	
	 public void moveCamera(View view) {
         mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
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
