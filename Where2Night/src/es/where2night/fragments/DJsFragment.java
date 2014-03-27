package es.where2night.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.DjViewActivty;
import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemEvent;
import es.where2night.adapters.AdapterItemLocal;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DJsFragment extends Fragment {
	private ArrayList<ItemLocalAndDJ> arraydir;
	private AdapterItemLocal adapter;
	
	private RequestQueue requestQueue;
    private JSONObject respuesta = null;
    private ImageLoader imageLoader;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		ListView list = (ListView) view.findViewById(R.id.ListFragment);
	    list.setAdapter(adapter);
	   
	    arraydir = new ArrayList<ItemLocalAndDJ>();
	    adapter = new AdapterItemLocal(getActivity(), arraydir);
	    list.setAdapter(adapter);
	    fillData();
	    
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

	private void fillData() {
		// TODO Auto-generated method stub
		
	}

}