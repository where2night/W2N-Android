package es.where2night.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.where2night.R;

import es.where2night.fragments.myProfile.FriendActivityFragment;
import es.where2night.fragments.myProfile.FriendInfoFragment;
import es.where2night.utilities.DataManager;

public class ProfileFragment extends Fragment implements ActionBar.TabListener{
	
	public static final String ID = "id";
	private Bundle bundle;
    private String friendId = "";
    private int lastIndex = 0;
    FragmentManager manager;
	
    private Fragment[] fragments = new Fragment[] {new FriendActivityFragment(),
			   new FriendInfoFragment()};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_friend_view, container, false);
		
		manager = getChildFragmentManager();
	
		
		manager.beginTransaction().add(R.id.selectedTabFragment, fragments[0])
			.add(R.id.selectedTabFragment, fragments[1])
			.commit();	

		manager.beginTransaction().hide(fragments[1])
		.commit();
    
		return view;
	}

	public void setTabs(){
		try{
		final ActionBar actionBar = getActivity().getActionBar();
	    
	    actionBar.removeAllTabs();
	    
	    
	    String[] tabs = getResources().getStringArray(R.array.friend_tabs);
	    
		
	    for (int i = 0; i<tabs.length; i++){
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText(tabs[i])
	                        .setTabListener(this));
	    }
	    

	    setContent(0);
		}catch (Exception e){}
	}


@Override
public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
	setContent(tab.getPosition());
}

public void setContent(int index) {
    Fragment toHide = null;
	Fragment toShow = null;
	
	toHide = fragments[lastIndex];
	toShow =  fragments[index];
	lastIndex = index;
	
	manager.beginTransaction()
			.hide(toHide)
			.show(toShow)
			.commit();
	
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}
}
