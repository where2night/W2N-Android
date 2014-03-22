package es.where2night.fragments;

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

import es.where2night.adapters.AdapterItemEvent;
import es.where2night.data.ItemEvent;

public class EventsFragment extends Fragment {
	
	private ListView list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		list = (ListView) view.findViewById(R.id.eventList);
		return view;
	}
	
	public void fill(){
		
	    ArrayList<ItemEvent> arraydir = new ArrayList<ItemEvent>();
	    ItemEvent evento1 = new ItemEvent(getResources().getDrawable(R.drawable.copernico), "Chicas gratis hasta la 1:30", "Copernico", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.orangecafe), "2x1 en todas las copas", "Orange Cafe", "25/01/2014");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.kapital), "Karaoke para todos!", "Kapital", "25/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
	    arraydir.add(evento1);
	        
	    AdapterItemEvent adapter = new AdapterItemEvent(getActivity(), arraydir);
	    list.setAdapter(adapter);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_events_fragment, menu);
	}
}
