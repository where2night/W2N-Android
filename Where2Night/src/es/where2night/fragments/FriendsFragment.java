package es.where2night.fragments;

import java.util.ArrayList;

import com.where2night.R;

import es.where2night.adapters.AdapterItemFriend;
import es.where2night.data.ItemFriend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FriendsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		ListView lista = (ListView) view.findViewById(R.id.ListFragment);
        ArrayList<ItemFriend> arraydir = new ArrayList<ItemFriend>();
        
        //TODO      
        
	    AdapterItemFriend adapter = new AdapterItemFriend(getActivity(), arraydir);
        lista.setAdapter(adapter);
		
		
		return view;
	}
}
