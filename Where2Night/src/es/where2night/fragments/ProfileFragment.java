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

import es.where2night.activities.InitActivity;
import es.where2night.activities.ProfileViewActivity;
import es.where2night.fragments.myProfile.FriendActivityFragment;
import es.where2night.fragments.myProfile.FriendInfoFragment;
import es.where2night.utilities.DataManager;

public class ProfileFragment extends Fragment {
	
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
		
		
    
		return view;
	}

	public void fill(){
		
	}

}
