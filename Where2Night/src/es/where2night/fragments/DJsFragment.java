package es.where2night.fragments;

import java.util.ArrayList;

import com.where2night.R;

import es.where2night.activities.DjViewActivty;
import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemLocal;
import es.where2night.data.ItemLocal;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DJsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		ListView list = (ListView) view.findViewById(R.id.ListFragment);
		list.clearDisappearingChildren();
	    ArrayList<ItemLocal> arraydir = new ArrayList<ItemLocal>();
	    ItemLocal evento1 = new ItemLocal(getResources().getDrawable(R.drawable.copernico),"Copernico", 0 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.orangecafe), "Orange Cafe", 1 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    evento1  = new ItemLocal(getResources().getDrawable(R.drawable.kapital), "Kapital", 2 );
	    arraydir.add(evento1);
	    final AdapterItemLocal adapter = new AdapterItemLocal(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    
	    list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), DjViewActivty.class);
                intent.putExtra(DjViewActivty.ID, adapter.getItemId(position));
                startActivity(intent);
			}
			
		});
	    
		return view;
	}
}