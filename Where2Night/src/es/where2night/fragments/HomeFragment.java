package es.where2night.fragments;


import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.MainActivity;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemFriend;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class HomeFragment extends Fragment implements OnItemSelectedListener{
	
	private Spinner spinnerAnimo;
	int numPagina;
	
	private RequestQueue requestQueue;
	AdapterItemNews adapterNews;
	ArrayList<Item> arraydir;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		spinnerAnimo = (Spinner)view.findViewById(R.id.spinnerAnimo);
		numPagina = 0;
		fill(view);
		return view;
	}
	
	public void fill(View view){
		fillData(view);
        //Toast.makeText(getActivity().getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
	    fillNews(view);
	    
	    
	}

	private void fillData(View view) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.mode_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAnimo.setAdapter(adapter);
		spinnerAnimo.setOnItemSelectedListener(this);
		
		//Pedimos los datos de animo y modo
		/*final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getLocalUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
*/
	}

	private void fillNews(View view) {
		ListView lista = (ListView) view.findViewById(R.id.newsList);
		arraydir = new ArrayList<Item>();
		adapterNews = new AdapterItemNews(getActivity(), arraydir);
        lista.setAdapter(adapterNews);
		
        Toast.makeText(getActivity().getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
        
        //FIXME PEDIR NOVEDADES
        final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getNewsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + numPagina;
		Log.e("url", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
			    {
			        @Override
			        public void onResponse(String response) {
			            // response
			        	Log.e("Response", response);
			            try {
			            	JSONObject root = new JSONObject(response);
			            	
			            	for (int i = 0; i < root.length() - 1; i++){
			            		JSONObject aux = root.getJSONObject(String.valueOf(i));
			            		String type = aux.getString("TYPE");
			            		int tipo = Integer.parseInt(type);
			            		switch (tipo) {
									case 1:
										//Eventos de locales que seguimos
										String title = aux.getString("title");
						            	String text = aux.getString("text");
						            	String[] dateArr = aux.getString("date").split("-");
						            	String date = dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0];
						            	String start = aux.getString("startHour");
						            	String close = aux.getString("closeHour");
						            	String idCreator = aux.getString("idProfileLocal");
						            	long id = Long.valueOf(aux.getString("idEvent"));
						            	String name = aux.getString("name");
						            	String picture = aux.getString("picture");
						            	ItemEvent event = new ItemEvent("",name,title,text,date,start,close,idCreator,id); //FIXME cargar imagen
						            	arraydir.add(event);
										break;
									
									case 2:
										//Estado de amigos
										
										break;
										
									case 3:
										//Modo de amigos
										
										break;
										
									case 4:
										//Locales favoritos
										
										break;
										
									case 5:
										//Eventos que asisten mis amigos
										
										break;
	
									default:
										break;
								}
			            	}
			            	adapterNews.notifyDataSetChanged();
			            	
			            } catch (Exception e) {
							e.printStackTrace();
						}
			        }

					
			    };
			    
			    Response.ErrorListener errorListener = new Response.ErrorListener() 
			    {
			         @Override
			         public void onErrorResponse(VolleyError error) {
			             // error
			             Log.e("Error.Response", error.toString());
			       }
			    };
			    
			    StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
				
				requestQueue.add(request);
        
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
