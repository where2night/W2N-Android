package es.where2night.fragments.friendProfile;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.adapters.AdapterItemEvent;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemEventFriend;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemFriendState;
import es.where2night.data.ItemLocalNews;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class FriendEventsFragment extends Fragment{
	
	private String friendId;
	private RequestQueue requestQueue;
    private ArrayList<Item> arraydir;
    private AdapterItemNews adapterNews;
	private ProgressBar pgEventList;
	private ListView list;
	
	//El minimo de elementos a tener debajo de la posicion actual del scroll antes de cargar mas
    private int visibleThreshold = 1;
    // La pagina actual
    private int currentPage = 0;
    // El total de elementos despues de la ultima carga de datos
    private int previousTotalItemCount = 0;
    //True si sigue esperando que termine de cargar el ultimo grupo de datos solicitados
    private boolean loading = true;
    // Sirve para setear la pagina inicial
    private int startingPageIndex = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		list = (ListView) view.findViewById(R.id.eventList);
		pgEventList = (ProgressBar) view.findViewById(R.id.pgEventList);
		friendId = getArguments().getString(FriendViewActivity.ID);
		
		list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
				if (!loading && (totalItemCount < previousTotalItemCount)) {
		            currentPage = startingPageIndex;
		            previousTotalItemCount = totalItemCount;
		            if (totalItemCount == 0) {
		                loading = true;
		            }
		        }

		        if (loading) {
		            if (totalItemCount > previousTotalItemCount) {
		                loading = false;
		                previousTotalItemCount = totalItemCount;
		                currentPage++;
		            }
		        }

		        //Si hemos llegado al final de la lista y una carga de datos no esta en proceso
		        //invoco el metodo onLoadMore para poder cargar mas datos.
		        if (!loading
		                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
		        	currentPage++;
		        	//Toast.makeText(getActivity().getApplicationContext(), "Entra Refill", Toast.LENGTH_LONG).show();
		        	fillData();
		            loading = true;
		        }
				
			}
		});
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}
	
	public void fill(){
		
		arraydir = new ArrayList<Item>();
	    adapterNews = new AdapterItemNews(getActivity(), arraydir);
	    list.setAdapter(adapterNews);
	    fillData();
	}
	
	private void fillData() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getNewsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + currentPage;
		
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
					            	String goes = aux.getString("GOES");
					            	boolean going = false;
					            	if (!goes.equals("null"))
					            		going = true;
					            	ItemEvent event = new ItemEvent("",name,title,text,date,start,close,idCreator,id,going); //FIXME cargar imagen
					            	arraydir.add(event);
									break;
								
								case 2:
									//Estado de amigos
									String nameAS = aux.getString("name") + " " + aux.getString("surnames");
									String pictureAS = aux.getString("picture");
									String stateAS = aux.getString("status");
									ItemFriendState iFState = new ItemFriendState("",nameAS,stateAS);
									arraydir.add(iFState);
									break;
									
								case 3:
									//Modo de amigos
									String nameAM = aux.getString("name") + " " + aux.getString("surnames");
									String pictureAM = aux.getString("picture");
									String modeAM = aux.getString("mode");
									ItemFriendMode iFMode = new ItemFriendMode("",nameAM,modeAM);
									arraydir.add(iFMode);									
									break;
									
								case 4:
									//Locales seguidos por amigos
									String nameL = aux.getString("localName");
									String nameF = aux.getString("name") + " " + aux.getString("surnames");
									String pictureLoc = aux.getString("picture");
									ItemLocalNews iLocal= new ItemLocalNews(nameL, "", nameF);
									arraydir.add(iLocal);
									break;
									
								case 5:
									//Eventos que asisten mis amigos
									String titleF = aux.getString("title");
					            	String textF = aux.getString("text");
					            	String[] dateArrF = aux.getString("date").split("-");
					            	String dateF = dateArrF[2] + "/" + dateArrF[1] + "/" + dateArrF[0];
					            	String startF = aux.getString("startHour");
					            	String closeF = aux.getString("closeHour");
					            	String idCreatorF = aux.getString("idProfileLocal");
					            	long idF = Long.valueOf(aux.getString("idEvent"));
					            	String nameLoc = aux.getString("localName");
					            	String pictureF = aux.getString("picture");
					            	String nameFriend = aux.getString("name");
					            	String goesF = aux.getString("GOES");
					            	boolean goingF = false;
					            	if (!goesF.equals("null"))
					            		goingF = true;
					            	ItemEventFriend eventFriend = new ItemEventFriend("",nameLoc,titleF,textF,dateF,startF,closeF,idCreatorF,nameFriend,goingF,idF); //FIXME cargar imagen
					            	arraydir.add(eventFriend);
									break;

								default:
									break;
							}
		            	}
		            adapterNews.notifyDataSetChanged();
		            	
				} catch (Exception e) {
					pgEventList.setVisibility(View.GONE);
					e.printStackTrace();
				}
	        }   
	    };
	    
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEventList.setVisibility(View.GONE);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
	    StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	
	
}