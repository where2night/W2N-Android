package com.prototipo.fragments;


import java.util.ArrayList;
import java.util.Arrays;

import com.prototipo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DatabaseInfoFragment extends Fragment {

	ListView list;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		loadContent();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_database_info, null);
		list = (ListView)view.findViewById(R.id.listView);
		return view;
	}

	public void loadContent() {
	
		String[] array_countries = new String[]{"Brasil", "Mexico", "Colombia", "Argentina",
				"Peru", "Venezuela", "Chile", "Ecuador", 
				"Guatemala", "Cuba"};
		ArrayList<String> countries = new ArrayList<String>(Arrays.asList(array_countries));
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
				android.R.layout.simple_list_item_1,countries);
		list.setAdapter(adapter);
		
	}
	
	
	
}
