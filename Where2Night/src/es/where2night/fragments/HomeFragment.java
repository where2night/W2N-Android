package es.where2night.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.activities.MainActivity;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemEventFriend;
import es.where2night.data.ItemFriend;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemFriendState;
import es.where2night.data.ItemLocalNews;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class HomeFragment extends Fragment{
	
	private Spinner spinnerAnimo;
	private TextView txtStatus;
	
	//El minimo de elementos a tener debajo de la posicion actual del scroll antes de cargar mas
    private int visibleThreshold = 3;
    // La pagina actual
    private int currentPage = 0;
    // El total de elementos despues de la ultima carga de datos
    private int previousTotalItemCount = 0;
    //True si sigue esperando que termine de cargar el ultimo grupo de datos solicitados
    private boolean loading = true;
    // Sirve para setear la pagina inicial
    private int startingPageIndex = 0;
	
	private Button btnEnviar;
	private ProgressBar pgEventList;
	private String oldMode;
	private String oldStatus;
	
	private RequestQueue requestQueue;
	AdapterItemNews adapterNews;
	ArrayList<Item> arraydir;
	ListView lista;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		spinnerAnimo = (Spinner)view.findViewById(R.id.spinnerAnimo);
		btnEnviar = (Button)view.findViewById(R.id.btnEnviarEstado);
		pgEventList = (ProgressBar)view.findViewById(R.id.pgEventList);
		txtStatus = (TextView)view.findViewById(R.id.txtEstado);
		lista = (ListView) view.findViewById(R.id.newsList);
		arraydir = new ArrayList<Item>();
		adapterNews = new AdapterItemNews(getActivity(), arraydir);
        lista.setAdapter(adapterNews);
        lista.setOnScrollListener(new OnScrollListener() {
			
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
		        	fillNews(getView());
		            loading = true;
		        }
				
			}
		});
        
		btnEnviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!(oldMode.equals(spinnerAnimo.getSelectedItem().toString()))){
					oldMode = spinnerAnimo.getSelectedItem().toString();
					final DataManager dm = new DataManager(getActivity().getApplicationContext());
					String[] cred = dm.getCred();
					requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
					String url = Helper.getSetModeUrl() + "/" + cred[0] + "/" + cred[1];
					Log.e("url", url);
					
					Response.Listener<String> succeedListener = new Response.Listener<String>(){
				        @Override
				        public void onResponse(String response) {
				            // response
				        	Log.e("Response", response);
				            try {} 
				            catch (Exception e) {
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
				    
				    StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener){
				    	@Override
					    protected Map<String, String> getParams() 
					    {  
					    	Map<String, String> info = new HashMap<String, String>();
					    	String strMode = ((Integer)spinnerAnimo.getSelectedItemPosition()).toString();
					    	info.put("mode", strMode);
					        return info;
					    }
				    }; 
					
					requestQueue.add(request);
				
				}
				
				if(!(oldStatus.equals(txtStatus.getText().toString()))){
					oldStatus = txtStatus.getText().toString();
					final DataManager dm = new DataManager(getActivity().getApplicationContext());
					String[] cred = dm.getCred();
					requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
					String url = Helper.getSetStatusUrl() + "/" + cred[0] + "/" + cred[1];
					Log.e("url", url);
					
					Response.Listener<String> succeedListener = new Response.Listener<String>(){
				        @Override
				        public void onResponse(String response) {
				            // response
				        	Log.e("Response", response);
				            try {} 
				            catch (Exception e) {
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
				    
				    StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener){
				    	@Override
					    protected Map<String, String> getParams() 
					    {  
					    	Map<String, String> info = new HashMap<String, String>();
					    	String strStatus = txtStatus.getText().toString();
					    	info.put("status", strStatus);
					        return info;
					    }
				    }; 
					
					requestQueue.add(request);
					
				}
				
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtStatus.getWindowToken(), 0);
			}
		});
		
		
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
		
		//Pedimos los datos de animo y modo
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String urlMode = Helper.getSetModeUrl() + "/" + cred[0] + "/" + cred[1];
		String urlStatus = Helper.getSetStatusUrl() + "/" + cred[0] + "/" + cred[1];

		Response.Listener<String> succeedListenerMode = new Response.Listener<String>() {
			        
			@Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("ResponseMode", response);
	            try {
	            	JSONArray aux = new JSONArray(response);
	            	JSONObject mo = aux.getJSONObject(0);
	            	String modeString = mo.getString("mode");
	            	//Toast.makeText(getActivity().getApplicationContext(), "Ha entrado", Toast.LENGTH_LONG).show();
	            	spinnerAnimo.setSelection(Integer.parseInt(modeString));
	            	oldMode = modeString;
	            }
	            catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    };
			    
	    Response.ErrorListener errorListenerMode = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	             Log.e("Error.Response", error.toString());
	       }
	    };
	    
	    Response.Listener<String> succeedListenerStatus = new Response.Listener<String>() {
	        
			@Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	JSONArray aux = new JSONArray(response);
	            	JSONObject mo = aux.getJSONObject(0);
	            	String statusString = mo.getString("status");
	            	txtStatus.setText(statusString);
	            	oldStatus = statusString;
	            }
	            catch (Exception e) {
	            	pgEventList.setVisibility(View.GONE);
					e.printStackTrace();
				}
	        }
	    };
			    
	    Response.ErrorListener errorListenerStatus = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEventList.setVisibility(View.GONE);
	             Log.e("Error.Response", error.toString());
	       }
	    };
			    
	    StringRequest request = new StringRequest(Request.Method.GET, urlMode, succeedListenerMode, errorListenerMode); 
		requestQueue.add(request);
		StringRequest request2 = new StringRequest(Request.Method.GET, urlStatus, succeedListenerStatus, errorListenerStatus); 
		requestQueue.add(request2);

	}

	private void fillNews(View view) {
		
        //Toast.makeText(getActivity().getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
        
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				if(adapterNews.getItem(position).getClass() == ItemFriendMode.class){
					Intent intent = new Intent(getActivity(), FriendViewActivity.class);
					intent.putExtra(FriendViewActivity.ID, String.valueOf(((ItemFriendMode)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemFriendState.class){
					Intent intent = new Intent(getActivity(), FriendViewActivity.class);
					intent.putExtra(FriendViewActivity.ID, String.valueOf(((ItemFriendState)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemEvent.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemEvent)adapterNews.getItem(position)).getIdCreator()));
					startActivity(intent);
				}//FIXME
				else if(adapterNews.getItem(position).getClass() == ItemLocalNews.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemLocalNews)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemEventFriend.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemEventFriend)adapterNews.getItem(position)).getIdCreator()));
					startActivity(intent);
				}
			}
		});
		
		
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
										long idAs = Integer.valueOf(aux.getString("idPartierFriend"));
										ItemFriendState iFState = new ItemFriendState("",nameAS,stateAS,idAs);
										arraydir.add(iFState);
										break;
										
									case 3:
										//Modo de amigos
										String nameAM = aux.getString("name") + " " + aux.getString("surnames");
										String pictureAM = aux.getString("picture");
										String modeAM = aux.getString("mode");
										long idAM = Integer.valueOf(aux.getString("idPartierFriend"));
										ItemFriendMode iFMode = new ItemFriendMode("",nameAM,modeAM,idAM);
										arraydir.add(iFMode);									
										break;
										
									case 4:
										//Locales seguidos por amigos
										String nameL = aux.getString("localName");
										String nameF = aux.getString("name") + " " + aux.getString("surnames");
										String pictureLoc = aux.getString("picture");
										long idL = Integer.valueOf(aux.getString("idProfileLocal"));
										ItemLocalNews iLocal= new ItemLocalNews(nameL, "", nameF,idL);
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
			            	pgEventList.setVisibility(View.GONE);
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
