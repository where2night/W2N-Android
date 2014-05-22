package es.where2night.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.FriendViewActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemNews;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemEventFriend;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemFriendState;
import es.where2night.data.ItemListFriend;
import es.where2night.data.ItemLocalCheck;
import es.where2night.data.ItemLocalGoes;
import es.where2night.data.ItemLocalNews;
import es.where2night.data.ItemNewList;
import es.where2night.fragments.editprofile.BasicInfoFragment;
import es.where2night.fragments.editprofile.DetailedInfoFragment;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;


public class HomeFragment extends Fragment{
	
	private static int RESULT_START_GPS = 1;
	private Spinner spinnerAnimo;
	private TextView txtStatus;
	private TextView txtLocalGoing;
	private LinearLayout layoutCheckIn;
	
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
    
    private static int lastVisibleItem = 0;
	
	private Button btnEnviar;
	private Button btnCheckIn;
	private ProgressBar pgEventList;
	public static String oldMode;
	public static String oldStatus;
	
	private RequestQueue requestQueue;
	AdapterItemNews adapterNews;
	ArrayList<Item> arraydir;
	ListView lista;
	
	boolean goingSomewhere = false;
	
	double latLocal = 0;
	double lngLocal = 0;
	int idLocal = 0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		spinnerAnimo = (Spinner)view.findViewById(R.id.spinnerAnimo);
		btnEnviar = (Button)view.findViewById(R.id.btnEnviarEstado);
		btnCheckIn = (Button)view.findViewById(R.id.btnCheckIn);
		pgEventList = (ProgressBar)view.findViewById(R.id.pgEventList);
		txtStatus = (TextView)view.findViewById(R.id.txtEstado);
		txtLocalGoing = (TextView)view.findViewById(R.id.txtLocalGoing);
		lista = (ListView) view.findViewById(R.id.newsList);
		layoutCheckIn = (LinearLayout) view.findViewById(R.id.layoutCheckIn);
		arraydir = new ArrayList<Item>();
		adapterNews = new AdapterItemNews(getActivity(), arraydir);
        lista.setAdapter(adapterNews);
        
        checkCheckIn();
        
    //    checkGoingSomeWhere();
        
        btnCheckIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				hacerCheckIn();
			}
		});
        
        lista.setOnScrollListener(new OnScrollListener() {
        	
        	int mLastFirstVisibleItem = 0;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
								
			}
			
			
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {	
				
				
				if (goingSomewhere && view.getId() == lista.getId()) 
		        {
		            final int currentFirstVisibleItem = lista.getFirstVisiblePosition();

		            if (currentFirstVisibleItem > mLastFirstVisibleItem) 
		            {
		            	layoutCheckIn.setVisibility(View.GONE);
		            } 
		            else if (currentFirstVisibleItem < mLastFirstVisibleItem) 
		            {
		            	layoutCheckIn.setVisibility(View.VISIBLE);
		            }

		            mLastFirstVisibleItem = currentFirstVisibleItem;
		        }   
				
				
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
				if(oldMode == null || !(oldMode.equals(((Integer)spinnerAnimo.getSelectedItemPosition()).toString()))){
					oldMode = ((Integer)spinnerAnimo.getSelectedItemPosition()).toString();
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
				
				if(oldStatus == null || !(oldStatus.equals(txtStatus.getText().toString()))){
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
				Toast.makeText(getActivity().getApplicationContext(), "¡Hecho!", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		fill(view);
		return view;
	}
	
	private void checkGoingSomeWhere() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getWereUserGoesTodayUrl() + "/" + cred[0] + "/" + cred[1];
		Log.e("CheckIn", url);
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	String localName = respuesta.getString("localName");
		            	txtLocalGoing.setText("Hoy vas a " + localName);
		            	latLocal = Double.valueOf(respuesta.getString("latitude"));
		            	lngLocal = Double.valueOf(respuesta.getString("longitude"));
		            	idLocal = Integer.valueOf(respuesta.getString("idProfile"));
		            	layoutCheckIn.setVisibility(View.VISIBLE);
		            	goingSomewhere = true;
		            	
		            	
					} catch (JSONException e) {
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

	public void fill(View view){
		fillData(view);
        //Toast.makeText(getActivity().getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
	    fillNews(view);
	    
	    
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(getActivity().getApplicationContext(), "Pantalla Estática", Toast.LENGTH_LONG).show();
        if (requestCode == RESULT_START_GPS && resultCode == Activity.RESULT_CANCELED ) {
        	LocationManager lm = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        	doCheckIn(lm);        	
        }
	}
	
	public void hacerCheckIn(){
		
		LocationManager lm = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
		      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		  // Build the alert dialog
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  builder.setTitle("Check In");
		  builder.setMessage("Para hacer Check In debes habilitar el GPS");
		  builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialogInterface, int i) {
		    // Show location settings when the user acknowledges the alert dialog
		    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    startActivityForResult(intent, RESULT_START_GPS);
		    }
		  });
		  builder.setNegativeButton("Cancelar",null);
		  Dialog alertDialog = builder.create();
		  alertDialog.setCanceledOnTouchOutside(false);
		  alertDialog.show();
		}else{
			doCheckIn(lm);
		}
	}
	
	public boolean doCheckIn (LocationManager lm){
		Location locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    Location best;
	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }
	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	       best = locationGPS;
	    	
	    }
	    else {
	        best = locationNet;
	    }
	    double lat = best.getLatitude();
	    double lng = best.getLongitude();
	    
	    if ((lat < latLocal + 0.0002) &&
	    	(lat > latLocal - 0.0002) &&	
	    	(lng < lngLocal + 0.0002) &&	
	    	(lng > lngLocal - 0.0002) ){
	    	
	    	registerCheckIn();
	    	
	    	Toast.makeText(getActivity(), "CheckIn", Toast.LENGTH_SHORT).show();
	    	return true;
	    }
	    return false;
	}
	
	private void registerCheckIn(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getCheckInUrl() + "/" + cred[0] + "/" + cred[1] + "/" + idLocal;
		Log.e("CheckIn", url);
		
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	String error = respuesta.getString("error");
		            	if (error.equals("0")){
		            		layoutCheckIn.setVisibility(View.GONE);
		            		goingSomewhere = false;
		            	}
					} catch (JSONException e) {
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
		
		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	
	private void checkCheckIn(){
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getCheckInUrl() + "/" + cred[0] + "/" + cred[1];

		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
		            try {
		            	JSONObject respuesta = new JSONObject(response);
		            	String id = respuesta.getString("id");
		            	if (id.equals("null"))
		            		checkGoingSomeWhere();
		            	else
		            		idLocal = Integer.valueOf(id);
		            		
					} catch (JSONException e) {
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
				}
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
				else if(adapterNews.getItem(position).getClass() == ItemLocalGoes.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemLocalGoes)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemLocalCheck.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemLocalCheck)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemListFriend.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemListFriend)adapterNews.getItem(position)).getId()));
					startActivity(intent);
				}
				else if(adapterNews.getItem(position).getClass() == ItemNewList.class){
					Intent intent = new Intent(getActivity(), LocalViewActivity.class);
					intent.putExtra(LocalViewActivity.ID, String.valueOf(((ItemNewList)adapterNews.getItem(position)).getId()));
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
			            		String pictureAux = "0";
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
						            	String name = aux.getString("localName");
						            	String picture = aux.getString("picture");
						            	picture = picture.replace("\\", "");
						            	if(picture.equals(pictureAux) || picture.equals("")){
											picture = Helper.getDefaultPubPictureUrl();
										}
						            	String goes = aux.getString("GOES");
						            	boolean going = false;
						            	if (!goes.equals("null"))
						            		going = true;
						            	ItemEvent event = new ItemEvent(picture,name,title,text,date,start,close,idCreator,id,going);
						            	arraydir.add(event);
										break;
									
									case 2:
										//Estado de amigos
										String nameAS = aux.getString("name") + " " + aux.getString("surnames");
										String pictureAS = aux.getString("picture");
										pictureAS = pictureAS.replace("\\", "");
										if(pictureAS.equals(pictureAux) || pictureAS.equals("")){
											pictureAS = Helper.getDefaultProfilePictureUrl();
										}
										String stateAS = aux.getString("status");
										long idAs = Integer.valueOf(aux.getString("idPartierFriend"));
										ItemFriendState iFState = new ItemFriendState(pictureAS,nameAS,stateAS,idAs);
										arraydir.add(iFState);
										break;
										
									case 3:
										//Modo de amigos
										String nameAM = aux.getString("name") + " " + aux.getString("surnames");
										String pictureAM = aux.getString("picture");
										pictureAM = pictureAM.replace("\\", "");
										if(pictureAM.equals(pictureAux) || pictureAM.equals("")){
											pictureAM = Helper.getDefaultProfilePictureUrl();
										}
										String modeAM = aux.getString("mode");
										long idAM = Integer.valueOf(aux.getString("idPartierFriend"));
										ItemFriendMode iFMode = new ItemFriendMode(pictureAM,nameAM,modeAM,idAM);
										arraydir.add(iFMode);									
										break;
										
									case 4:
										//Locales seguidos por amigos
										String nameL = aux.getString("localName");
										String nameF = aux.getString("name") + " " + aux.getString("surnames");
										String pictureLoc = aux.getString("picture");
										pictureLoc = pictureLoc.replace("\\", "");
										if(pictureLoc.equals(pictureAux) || pictureLoc.equals("")){
											picture = Helper.getDefaultPubPictureUrl();
										}
										long idL = Integer.valueOf(aux.getString("idProfileLocal"));
										ItemLocalNews iLocal= new ItemLocalNews(nameL, pictureLoc, nameF,idL);
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
						            	pictureF = pictureF.replace("\\", "");
						            	if(pictureF.equals(pictureAux) || pictureF.equals("")){
						            		pictureF = Helper.getDefaultPubPictureUrl();
										}
						            	String nameFriend = aux.getString("name");
						            	String goesF = aux.getString("GOES");
						            	boolean goingF = false;
						            	if (!goesF.equals("null"))
						            		goingF = true;
						            	ItemEventFriend eventFriend = new ItemEventFriend(pictureF,nameLoc,titleF,textF,dateF,startF,closeF,idCreatorF,nameFriend,goingF,idF);
						            	arraydir.add(eventFriend);
										break;
										
									case 6:
										//Locales a los que van mis amigos
										String nameLG = aux.getString("localName");
										String nameFG = aux.getString("name") + " " + aux.getString("surnames");
										String pictureLocG = aux.getString("picture");
										String[] dateArrG = aux.getString("assistdate").split("-");
						            	String dateG = dateArrG[2] + "/" + dateArrG[1] + "/" + dateArrG[0];
										pictureLocG = pictureLocG.replace("\\", "");
										if(pictureLocG.equals(pictureAux) || pictureLocG.equals("")){
											picture = Helper.getDefaultPubPictureUrl();
										}
										long idLG = Integer.valueOf(aux.getString("idProfileLocal"));
										ItemLocalGoes iLocalG= new ItemLocalGoes(nameLG, pictureLocG, nameFG, dateG, idLG);
										arraydir.add(iLocalG);
										break;
										
									case 7:
										//Locales a los han hecho checkin mis amigos
										String valido = "1";
										String isC = aux.getString("inside");
										if (isC.equals(valido)){
											String nameLC = aux.getString("localName");
											String nameFC = aux.getString("name") + " " + aux.getString("surnames");
											String pictureLocC = aux.getString("picture");
											pictureLocC = pictureLocC.replace("\\", "");
											if(pictureLocC.equals(pictureAux) || pictureLocC.equals("")){
												picture = Helper.getDefaultPubPictureUrl();
											}
											long idLC = Integer.valueOf(aux.getString("idProfileLocal"));
											ItemLocalCheck iLocalC= new ItemLocalCheck(nameLC, pictureLocC, nameFC, idLC);
											arraydir.add(iLocalC);
										}
										break;
										
									case 8:
										//Amigos se apuntan a lista
										String pictureL = aux.getString("picture");
						            	pictureL = pictureL.replace("\\", "");
						            	if(pictureL.equals(pictureAux) || pictureL.equals("")){
						            		pictureL = Helper.getDefaultPubPictureUrl();
										}
						            	String nameLocL = aux.getString("localName");
										String textoL = aux.getString("name") + " " + aux.getString("surnames") + " se ha apuntado a esta lista";
										String titleL = aux.getString("title");
										String descripcionL = aux.getString("text");
										String[] dateArrL = aux.getString("date").split("-");
						            	String dateL = dateArrL[2] + "/" + dateArrL[1] + "/" + dateArrL[0];
						            	String startL = aux.getString("startHour");
						            	String closeL = aux.getString("closeHour");
						            	long idLo = Long.valueOf(aux.getString("idEvent"));
						            	String goesL = aux.getString("GOES");
						            	boolean goingL = false;
						            	if (!goesL.equals("null"))
						            		goingL = true;
						            	String[] dateArrLC = aux.getString("dateClose").split("-");
						            	String expireL = dateArrLC[2] + "/" + dateArrLC[1] + "/" + dateArrLC[0];
						            	ItemListFriend listF = new ItemListFriend(pictureL,textoL,nameLocL,titleL,descripcionL,dateL,startL,closeL,expireL,goingL,idLo);
						            	arraydir.add(listF);
										break;
										
									case 9:
										//Locales a los que sigo crean nueva lista
										String pictureN = aux.getString("picture");
						            	pictureN = pictureN.replace("\\", "");
						            	if(pictureN.equals(pictureAux) || pictureN.equals("")){
						            		pictureN = Helper.getDefaultPubPictureUrl();
										}
						            	String nameLocN = aux.getString("localName");
										String textoN = "Acaba de crear esta lista";
										String titleN = aux.getString("title");
										String descripcionN = aux.getString("text");
										String[] dateArrN = aux.getString("date").split("-");
						            	String dateN = dateArrN[2] + "/" + dateArrN[1] + "/" + dateArrN[0];
						            	String startN = aux.getString("startHour");
						            	String closeN = aux.getString("closeHour");
						            	long idN = Long.valueOf(aux.getString("idEvent"));
						            	String goesN = aux.getString("GOES");
						            	boolean goingN = false;
						            	if (!goesN.equals("null"))
						            		goingN = true;
						            	String[] dateArrNC = aux.getString("dateClose").split("-");
						            	String expireN = dateArrNC[2] + "/" + dateArrNC[1] + "/" + dateArrNC[0];
						            	ItemNewList list = new ItemNewList(pictureN,textoN,nameLocN,titleN,descripcionN,dateN,startN,closeN,expireN,goingN,idN);
						            	arraydir.add(list);
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
