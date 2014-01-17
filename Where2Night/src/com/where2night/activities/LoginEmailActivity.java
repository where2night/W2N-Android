package com.where2night.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;
import com.where2night.utilities.DataManager;
import com.where2night.utilities.Helper;

public class LoginEmailActivity extends Activity {

	private EditText txtEmail;
	private EditText txtPass;
	String email, pass;
	RequestQueue requestQueue;
	JSONObject respuesta = null;
	Button btnLoginEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_login_email);
		 getActionBar().hide();
		  
		 btnLoginEmail = (Button) findViewById(R.id.btnLogin);
		 txtEmail = (EditText) findViewById(R.id.txtEmail);
		 txtPass = (EditText) findViewById(R.id.txtPass);
	     btnLoginEmail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					email = txtEmail.getText().toString();
					pass = txtPass.getText().toString();
					
					
					btnLoginEmail.setEnabled(false);
					requestQueue = Volley.newRequestQueue(getApplicationContext()); 
					String url = Helper.getLoginUrl();
					
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
									DataManager dm = new DataManager(getApplicationContext());
									try{
										dm.login(token,-1);
									}catch(SQLException e){
										
									}
									Intent i = new Intent(getApplicationContext(), MainActivity.class);
									startActivity(i);
								}
								else{
									btnLoginEmail.setEnabled(true);									
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
								params.put("password", pass);
						        return params;  
						    }
						};
					
					requestQueue.add(request);	
					
				}
			});
	     
	     
	        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_email, menu);
		return true;
	}

}
