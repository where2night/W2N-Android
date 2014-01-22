package com.where2night.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
	private TextView txtLoginError;
	private ProgressBar pgLoginEmail;
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
		 txtEmail = (EditText) findViewById(R.id.txtLoginEmail);
		 txtPass = (EditText) findViewById(R.id.txtLoginPass);
		 pgLoginEmail = (ProgressBar) findViewById(R.id.pgLoginEmail);
		 txtLoginError = (TextView) findViewById(R.id.login_error);
		 
	     btnLoginEmail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					email = txtEmail.getText().toString();
					pass = txtPass.getText().toString();
					pgLoginEmail.setVisibility(View.VISIBLE);
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
				            	pgLoginEmail.setVisibility(View.GONE);
					            respuesta = new JSONObject(response);
								String token = respuesta.getString("Token");
								if (!(token.equals("0")))
								{
									DataManager dm = new DataManager(getApplicationContext());
									dm.login(email,token,-1);
									Intent i = new Intent(getApplicationContext(), MainActivity.class);
									i.putExtra(MainActivity.EMAIL, email);
									i.putExtra(MainActivity.TYPE, "-1");
									i.putExtra(MainActivity.PARENT, "0");
									btnLoginEmail.setEnabled(true);
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(i);
								}
								else{
									txtLoginError.setText(getResources().getString(R.string.login_error));
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
				             txtLoginError.setText(getResources().getString(R.string.login_error_connection));
				             btnLoginEmail.setEnabled(true);
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
