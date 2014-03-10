package es.where2night.fragments.localdetail;

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

import es.where2night.adapters.AdapterItemDiscountList;
import es.where2night.data.ItemDiscountList;
import es.where2night.data.ItemEvent;

public class LocalDiscountListFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		
		ListView list = (ListView) view.findViewById(R.id.eventList);
	    ArrayList<ItemDiscountList> arraydir = new ArrayList<ItemDiscountList>();
	        
	    
	    ItemDiscountList descuento = new ItemDiscountList("Lunes", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Martes", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Miercoles", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Jueves", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Viernes", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Sabado", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    descuento = new ItemDiscountList("Domingo", "15/03/2014 18:00");
	    arraydir.add(descuento);
	    
	    AdapterItemDiscountList adapter = new AdapterItemDiscountList(getActivity(), arraydir);
	    list.setAdapter(adapter);
		return view;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_events_fragment, menu);
	}
}
