package es.where2night.fragments.localdetail;

import com.where2night.R;

import es.where2night.fragments.MapFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocalInfoFragment extends Fragment{

	public LocalInfoFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_local_info, container, false);
		
		FragmentManager manager = getChildFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.LocalInfoMap, new MapFragment())
        	    .commit();	
        
		return view;
		
	}

	
	
	
	
}
