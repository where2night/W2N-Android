package com.where2night.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.where2night.utilities.DataManager;
import com.where2night.utilities.Helper;

public class LoginFB {

	RequestQueue requestQueue;
	JSONObject respuesta = null;
	String email;
	Context appContext;
	boolean success = false;	
	boolean value = false;	
	
	public LoginFB(String mail,Context context){
	
	email = mail;
	appContext = context;
	
	}
	
	public boolean connection(){
	requestQueue = Volley.newRequestQueue(appContext); 
	String url = Helper.getLoginFBUrl();
	
	Response.Listener<String> succeedListener = new Response.Listener<String>() 
    {
        @Override
        public void onResponse(String response) {
            // response
            Log.e("Response", response);
            try {
	            respuesta = new JSONObject(response);
				String token = respuesta.getString("Token");
				if (!(token.equals("0")))
				{
					DataManager dm = new DataManager(appContext);
					try{
						dm.login(token,0);
						success = true;
						value = true;
					}catch(SQLException e){
						
					}
					
				}
				else{
					success = false;
					value = true;
				}
            } catch(JSONException e) {}
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
	
	StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener) 
	{     
		    @Override
		    protected Map<String, String> getParams() 
		    {  
		    	HashMap<String, String> params = new HashMap<String, String>();
				params.put("email", email);
		        return params;  
		    }
		};
	
	requestQueue.add(request);	
	
	while (!value){
		int i = 1;
	}
	
	return success;
	
	
	}	
}
