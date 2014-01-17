package com.prototipo.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prototipo.Helper;
import com.prototipo.R;
import com.prototipo.fragments.DatabaseInfoFragment;

public class MainActivity extends FragmentActivity implements OnClickListener{

	
	private String email = "";
	private String password  = "";
	private EditText editEmail;
	private EditText editPassword;
	Button btnSend;
	Button btnConexion;
	DatabaseInfoFragment fragment;
	RequestQueue requestQueue;
	JSONObject respuesta = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnSend = (Button)findViewById(R.id.btnSend);
        btnConexion = (Button)findViewById(R.id.btnConexion);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        
        
        btnSend.setOnClickListener(this);
        btnConexion.setOnClickListener(this);  
        
        FragmentManager manager = getSupportFragmentManager();
        fragment = (DatabaseInfoFragment) manager.findFragmentById(R.id.fragmentDatabaseInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    void apiCall()
    {
    	requestQueue = Volley.newRequestQueue(this); 
		String url = Helper.getUrl();
		
		
		/*
		Response.Listener<JSONObject> listener = 
	    		new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {
						Log.e("RESPONSE",response.toString());
		            }
	    };
	    
	    
	    Response.ErrorListener errorListener = 
	    		new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError e) {
						
						Log.e("RESPONSE", "Error Response code: " + e.getMessage());
					}
	    };
	    JSONObject objeto = new JSONObject(params);
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, objeto, listener, errorListener);
		*/
		
		StringRequest request = new StringRequest(Request.Method.POST, url, 
			    new Response.Listener<String>() 
			    {
			        @Override
			        public void onResponse(String response) {
			            // response
			            Log.d("Response", response);
			        }
			    }, 
			    new Response.ErrorListener() 
			    {
			         @Override
			         public void onErrorResponse(VolleyError error) {
			             // error
			             Log.d("Error.Response", error.getMessage());
			       }
			    }
			) {     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	HashMap<String, String> params = new HashMap<String, String>();
					params.put("email",  "isma@gmail.com");
					params.put("password",  "123456");
			             
			            return params;  
			    }
			};
		
		requestQueue.add(request);	
		
		
    }
    
	@Override
	public void onClick(View v) {
		
		if(v.getId() == btnSend.getId()){
			email = editEmail.getText().toString();
			password = editPassword.getText().toString();		
			Toast.makeText(this, "Email: " + email + " - Password: " + password, Toast.LENGTH_SHORT).show();		
			editEmail.setText("");
			editPassword.setText("");
		}
		else if (v.getId() == btnConexion.getId()){
			apiCall();
			fragment.loadContent();			
		}
	}
    
}
