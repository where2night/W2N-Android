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
        
        ItemFriend friend = new ItemFriend(getResources().getDrawable(R.drawable.bea), "Beatriz Ortega de Pedro", "Está en Penelope.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.cristina), "Cristina Pedroche", " Va a ir a Pacha.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.isma), "Ismael Requena Andreu", "No tiene decidido nada.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.jennifer), "Jennifer Lawrence", "Va a ir a Moma.");
	    arraydir.add(friend);        
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.juan), "Juan Brugera Monedero", " Va a ir a Cats.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.sergio), "Sergio Primo Galan", "Va a ir a Moma.");
	    arraydir.add(friend);
	    friend  = new ItemFriend(getResources().getDrawable(R.drawable.yuli), "Yuleska Perez", "No tiene decidido nada.");
	    arraydir.add(friend);      
        
	    AdapterItemFriend adapter = new AdapterItemFriend(getActivity(), arraydir);
        lista.setAdapter(adapter);
		
		
		return view;
	}
}
