package es.where2night.fragments.djdetail;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemEvent;
import es.where2night.data.ItemEvent;

public class DJEventsFragment extends Fragment {
	
	private String localId;
	
	private ListView list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		list = (ListView) view.findViewById(R.id.eventList);
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		return view;
	}
	
	public void fill(){
		
	    ArrayList<ItemEvent> arraydir = new ArrayList<ItemEvent>();
	   	        
	    AdapterItemEvent adapter = new AdapterItemEvent(getActivity(), arraydir);
	    list.setAdapter(adapter);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_events_fragment, menu);
	}
}
