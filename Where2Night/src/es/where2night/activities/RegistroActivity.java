package es.where2night.activities;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class RegistroActivity extends Activity {

	
	private EditText etRegisterName;
	private EditText etRegisterSurname;
	private EditText etRegisterDate;
	private EditText etRegisterEmail;
	private EditText etRegisterPass;
	private EditText etRegisterPass2;
	private RadioButton rdFemale;
	private RadioButton rdMale;
	private Button btnRegister;
	private TextView con_cuenta;
	private ProgressBar pgRegister;
	
	private String name;
	private String surnames;
	private String birthdate;
	private String email;
	private String pass;
	private String pass2;
	private String gender;
	
	private RequestQueue requestQueue;
    private JSONObject respuesta = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getActionBar().hide();
		
		
		
		etRegisterName = (EditText) findViewById(R.id.etRegisterName);
		etRegisterSurname = (EditText) findViewById(R.id.etRegisterSurname);
		etRegisterDate = (EditText) findViewById(R.id.etRegisterDate);
		etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
		etRegisterPass = (EditText) findViewById(R.id.etRegisterPass);
		etRegisterPass2 = (EditText) findViewById(R.id.etRegisterPass2);
		rdFemale = (RadioButton) findViewById(R.id.rdFemale);
		rdMale = (RadioButton) findViewById(R.id.rdMale);
		btnRegister = (Button) findViewById(R.id.btnEnviarEstado);
		pgRegister = (ProgressBar) findViewById(R.id.pgRegister);
		con_cuenta = (TextView) findViewById(R.id.link_to_login);
		
		con_cuenta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LoginEmailActivity.class);
                startActivity(intent);
				
			}
		});
		
		btnRegister.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				pgRegister.setVisibility(View.VISIBLE);
				btnRegister.setEnabled(false);
				name = etRegisterName.getText().toString();
				surnames = etRegisterSurname.getText().toString();
				birthdate = etRegisterDate.getText().toString();
				email = etRegisterEmail.getText().toString();
				pass = etRegisterPass.getText().toString();
				pass2 = etRegisterPass2.getText().toString();
				if (rdFemale.isChecked()){
					gender = "female";
				}else if(rdMale.isChecked()){
					gender = "male";
				}
				
				if (checkFields()){
					DataManager dm = new DataManager(getApplicationContext());
					dm.setUser(email,"",name, surnames, birthdate, gender);
					registerCall();
				}else{
					
				}
					
				
			
			}

			

			
			
		});
		
		
	}
	
	private boolean checkFields() {
		
		if(name.isEmpty()){
			setErrorMsg("Rellena tu nombre por favor");
			return false;
		}else if(surnames.isEmpty()){
			setErrorMsg("Rellena tus apellidos por favor");
			return false;	
		}else if(birthdate.isEmpty()){
			setErrorMsg("Rellena tu fecha de nacimiento por favor");
			return false;
		}else if(gender.isEmpty()){
			setErrorMsg("Indica tu sexo por favor");
			return false;
		}else if(email.isEmpty()){
			setErrorMsg("Rellena tu email por favor");
			return false;
		}else if(pass.isEmpty()){
			setErrorMsg("Introduce una contrase�a por favor");
			return false;
		}else if(pass2.isEmpty()){
			setErrorMsg("Repite la contrase�a por favor");
			return false;
		}else if(pass.length() < 6){
			setErrorMsg("Por favor introduce una contrase�a de al menos 6 letras y/o n�meros");
			return false;
		}else if(!pass.equals(pass2)){
			setErrorMsg(getResources().getString(R.string.registerErrorPasswords));
			return false;
		} 
		return true;
	}
	
	
	private void registerCall() {
		
		final DataManager dm = new DataManager(getApplicationContext());
		
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getRegisterUrl();
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	pgRegister.setVisibility(View.GONE);
	            	btnRegister.setEnabled(true);
		            respuesta = new JSONObject(response);
					String token = respuesta.getString("Token");
					String idProfile = respuesta.getString("id");
					if (!(token.equals("0")))
					{
						dm.login(email,idProfile,token,"-1");
						Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
						i.putExtra(MainActivity.EMAIL, email);
						i.putExtra(MainActivity.TYPE, "-1");
						i.putExtra(MainActivity.PARENT, "0");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(i);
					}else{}
	            } catch(JSONException e) {}
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgRegister.setVisibility(View.GONE);
	        	 btnRegister.setEnabled(true);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener) 
		{     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	Map<String, String> info = dm.getUser(email);
			    	info.put("pass", pass);
			    	info.put("mobile", "1");
			        return info;
			    }
		};
		
		requestQueue.add(request);
		
	}
	
	private void setErrorMsg(String string){
		pgRegister.setVisibility(View.GONE);
		btnRegister.setEnabled(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(string)
		        .setTitle("Error en el registro")
		        .setCancelable(false)
		        .setNeutralButton("Aceptar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
		//register_error.setText(string);
	}


}
