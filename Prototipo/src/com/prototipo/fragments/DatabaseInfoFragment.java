package com.prototipo.fragments;


import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prototipo.Helper;
import com.prototipo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DatabaseInfoFragment extends Fragment {

	public ListView list;
	
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
		
		/* ArrayList<String> people = new ArrayList<String>(Arrays.asList(array_people));
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
				R.layout.custom_list_item,people);
		list.setAdapter(adapter); */
		
	}
	
	
	
}
