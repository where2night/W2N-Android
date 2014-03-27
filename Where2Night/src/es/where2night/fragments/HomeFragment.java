package es.where2night.fragments;


import java.util.ArrayList;

import com.where2night.R;

import es.where2night.activities.MainActivity;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemFriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class HomeFragment extends Fragment implements OnItemSelectedListener{
	
	private Spinner spinnerAnimo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		spinnerAnimo = (Spinner)view.findViewById(R.id.spinnerAnimo);
		fillList(view);
		return view;
	}
	
	public void fillList(View view){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.mode_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAnimo.setAdapter(adapter);
		
		spinnerAnimo.setOnItemSelectedListener(this);
		
		ListView lista = (ListView) view.findViewById(R.id.newsList);
        ArrayList<Item> arraydir = new ArrayList<Item>();

		
        ItemFriend friend = new ItemFriend(getResources().getDrawable(R.drawable.bea), "Beatriz Ortega de Pedro", "Está en Penelope.");
	    arraydir.add(friend);
//	    ItemEvent evento1 = new ItemEvent(getResources().getDrawable(R.drawable.copernico), "Chicas gratis hasta la 1:30", "Copernico", "24/01/214");
//	    arraydir.add(evento1);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.cristina), "Cristina Pedroche", " Va a ir a Pacha.");
	    arraydir.add(friend);
//	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.orangecafe), "2x1 en todas las copas", "Orange Cafe", "25/01/214");
//	    arraydir.add(evento1);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.isma), "Ismael Requena Andreu", "No tiene decidido nada.");
	    arraydir.add(friend);
	//    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.kapital), "Karaoke para todos!", "Kapital", "25/01/214");
//	    arraydir.add(evento1);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.jennifer), "Jennifer Lawrence", "Va a ir a Moma.");
	    arraydir.add(friend); 
//	    evento1  = new ItemEvent(getResources().getDrawable(R.drawable.penelope), "1 copa por 6€ y 2 por 10€", "Penelope", "24/01/214");
//	    arraydir.add(evento1);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.juan), "Juan Brugera Monedero", " Va a ir a Cats.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.sergio), "Sergio Primo Galan", "Va a ir a Moma.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.yuli), "Yuleska Perez", "No tiene decidido nada.");
	    arraydir.add(friend);
	    
	    AdapterItemNews adapter2 = new AdapterItemNews(getActivity(), arraydir);
	    
        lista.setAdapter(adapter2);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
