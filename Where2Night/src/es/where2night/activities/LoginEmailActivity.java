package es.where2night.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class LoginEmailActivity extends Activity {

	private EditText txtEmail;
	private EditText txtPass;
	private TextView txtLoginError;
	private TextView sin_cuenta;
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
		  
		 btnLoginEmail = (Button) findViewById(R.id.btnLoginLogin);
		 txtEmail = (EditText) findViewById(R.id.txtLoginEmail);
		 txtPass = (EditText) findViewById(R.id.txtLoginPass);
		 pgLoginEmail = (ProgressBar) findViewById(R.id.pgLoginEmail);
		 txtLoginError = (TextView) findViewById(R.id.login_error);
		 sin_cuenta = (TextView) findViewById(R.id.link_to_register);
		 
		 sin_cuenta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
				
			}
		});
		 
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
								String idProfile = respuesta.getString("id");
								if (!(token.equals("0")))
								{
									DataManager dm = new DataManager(getApplicationContext());
									dm.login(email,idProfile,token,"-1");
									Intent i = new Intent(getApplicationContext(), MainActivity.class);
									i.putExtra(MainActivity.EMAIL, email);
									i.putExtra(MainActivity.TYPE, "-1");
									i.putExtra(MainActivity.PARENT, "0");
									btnLoginEmail.setEnabled(true);
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(i);
								}
								else{
									muestraError("El email o la contraseña no es correcto");
									//txtLoginError.setText(getResources().getString(R.string.login_error));
									btnLoginEmail.setEnabled(true);									
								}
				            } catch(JSONException e) {
				            	muestraError("El email o la contraseña no es correcto");
				            }
				        }
				    };
				    Response.ErrorListener errorListener = new Response.ErrorListener() 
				    {
				         @Override
				         public void onErrorResponse(VolleyError error) {
				             // error
				             muestraError("Error de conexión");
				             //txtLoginError.setText(getResources().getString(R.string.login_error_connection));
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
								params.put("mobile", "1");
						        return params;  
						    }
						};
					
					requestQueue.add(request);	
					
				}
			});
	     
	     
	        
	}
	
	private void muestraError(String error){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(error)
		        .setTitle("Error en el login")
		        .setCancelable(false)
		        .setNeutralButton("Aceptar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
	}


}
