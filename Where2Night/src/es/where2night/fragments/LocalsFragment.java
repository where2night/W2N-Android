package es.where2night.fragments;

import com.where2night.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocalsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view = inflater.inflate(R.layout.fragment_locals, container, false); -- linea original
		// En este fragmento va una lista con los locales que se siguen
		// Para demostracion se muestra una vista de un local 
		//---**** HAY QUE CAMBIARLO ****---
		View view = inflater.inflate(R.layout.activity_local_view, container, false);
		return view;
	}
}
