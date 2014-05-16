package es.where2night.fragments.localdetail;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.where2night.R;
import com.where2night.R.id;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import es.where2night.activities.LocalViewActivity;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LocalStatisticsFragment extends Fragment{

	private String localId;
	private TextView people;
	private WebView chartGender;
	private WebView chartAge;
	private WebView chartCivilState;
	private RequestQueue requestQueue;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		localId = getArguments().getString(LocalViewActivity.ID);
		
		View view = inflater.inflate(R.layout.fragment_local_statistics, container, false);
		people = (TextView) view.findViewById(R.id.textPeople);
		chartGender = (WebView) view.findViewById(R.id.chartGender);
		chartAge = (WebView) view.findViewById(R.id.chartAge);
		chartCivilState = (WebView) view.findViewById(R.id.chartCivilState);
        
		return view;
		
	}
		   
	public void fill() {
		final DataManager dm = new DataManager(getActivity().getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); 
		String url = Helper.getStatisticsUrl() + "/" + cred[0] + "/" + cred[1] + "/" + localId;
		Log.e("url", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	
	            	JSONObject root = new JSONObject(response);
	            	int inicial = (Integer.parseInt(root.getString("rows")))-1;
	            	
	            	people.setText("Van a ir " + root.getString("numGo") + " personas");
	            	
	            	String urlGender = "http://chart.apis.google.com/chart?cht=p3&chs=250x125&chd=t:" + 
	            						root.getString("mens") + "," +
	            						root.getString("womens") + 
	            						"&chl=" + root.getString("mens") + "|" + root.getString("womens") + 
	            						"&chts=000000,16&chtt=Distribucion+por+genero&chco=0000FF,FF0000&chdl=Hombres|Mujeres";
	            	chartGender.loadUrl(urlGender);
	            	
	            	String urlAge = "http://chart.apis.google.com/chart?cht=p3&chs=250x125&chd=t:" +
	            					root.getString("a18-20") + "," +
	            					root.getString("a21-23") + "," +
	            					root.getString("a24-30") + "," +
	            					root.getString("am31") +
	            					"&chl=" + root.getString("a18-20") + "|" +
	            					root.getString("a21-23") + "|" +
	            					root.getString("a24-30") + "|" +
	            					root.getString("am31") + 
	            					"&chts=000000,16&chtt=Distribucion+por+edad&chco=0000FF,FF0000,FFFF00,00FF00&chdl=18-20|21-23|24-30|31+o+mas";
	            	chartAge.loadUrl(urlAge);
	            	
	            	String urlCivilState = "http://chart.apis.google.com/chart?cht=p3&chs=250x170&chd=t:";
	            	for(int i=inicial+71; i<inicial+76; i++){
	            		urlCivilState = urlCivilState + root.getString(((Integer)i).toString()) + ",";
	            	}
	            	urlCivilState = urlCivilState + root.getString(((Integer)(inicial+76)).toString()) + "&chl=";
	            	for(int i=inicial+71; i<inicial+76; i++){
	            		urlCivilState = urlCivilState + root.getString(((Integer)i).toString()) + "|";
	            	}
	            	urlCivilState = urlCivilState + root.getString(((Integer)(inicial+76)).toString());						
	            	urlCivilState = urlCivilState + "&chts=000000,16&chtt=Distribucion+por+estado+'civil'&chco=00FF00,FF0000,0000FF,FFFF00,FF00FF,00FFFF,660099&chdl=Sin+compromiso|Ennoviad@|Con+novi@,+pero...|Buscando+rollete|Casad@|Divorciad@|Viud@";		
	            	chartCivilState.loadUrl(urlCivilState);
	            	
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
		
	
}
