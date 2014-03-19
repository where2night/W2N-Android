package es.where2night.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.fragments.localdetail.LocalInfoFragment;

public class LocalsFragment extends Fragment implements  ActionBar.TabListener {
	
	private Fragment[] fragments = new Fragment[]{ new MapFragment(),
			   									   new EventsFragment()};
	private int lastIndex = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locals, container, false);// -- linea original

		final ActionBar actionBar = getActivity().getActionBar();
		// Specify that we will be displaying tabs in the action bar.
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Mapa")
                        .setTabListener(this));
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Lista")
                        .setTabListener(this));
        
        FragmentManager manager = getChildFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.localListTab, fragments[0])
        	    .add(R.id.localListTab, fragments[1])
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
				        		  .commit();
        
        
        setContent(0);
        
		return view;
	}
	

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		setContent(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public void setContent(int index) {
	    Fragment toHide = null;
		Fragment toShow = null;
		
		toHide = fragments[lastIndex];
		toShow =  fragments[index];
		lastIndex = index;
		
		FragmentManager manager = getChildFragmentManager();
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
    }
}
