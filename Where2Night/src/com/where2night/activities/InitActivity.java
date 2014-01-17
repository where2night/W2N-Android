package com.where2night.activities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.where2night.R;
import com.where2night.util.LoginFB;
import com.where2night.utilities.DataManager;
import com.where2night.utilities.Helper;
import com.where2night.utilities.MomentUtil;

public class InitActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener{
	
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

    private TextView mSignInStatus;
    private PlusClient mPlusClient;
    private SignInButton mSignInButton;
    private View mSignOutButton;
    private View mRevokeAccessButton;
    private ConnectionResult mConnectionResult;
    private Button btnLoginEmail;
    private Button btnRegistro;
    private String email;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getActionBar().hide();
        
        /* Check if user is already loged in */
       
        
        
        //Google
        mPlusClient = new PlusClient.Builder(this, this, this)
                .setActions(MomentUtil.ACTIONS)
                .build();

        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton = (SignInButton) findViewById(R.id.login_gplus_button);
        mSignInButton.setOnClickListener(this);
        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);
        mRevokeAccessButton = findViewById(R.id.revoke_access_button);
        mRevokeAccessButton.setOnClickListener(this);
        
        //FaceBook
        final TextView txtSaludo = (TextView)findViewById(R.id.txtSaludo);
        
        LoginButton authButton = (LoginButton) findViewById(R.id.login_fb_button);
          authButton.setOnErrorListener(new OnErrorListener() {
           
           @Override
           public void onError(FacebookException error) {
                   
           }
        });
          
        authButton.setReadPermissions(Arrays.asList("basic_info","email"));

        authButton.setSessionStatusCallback(new Session.StatusCallback() {
                   
                @SuppressWarnings("deprecation")
				@Override
                   public void call(Session session, SessionState state, Exception exception) {
                    
                    if (session.isOpened()) {                                      
                              Request.executeMeRequestAsync(session,
                                      new Request.GraphUserCallback() {
                                          @Override
                                          public void onCompleted(GraphUser user,Response response) {
                                              if (user != null) {
                                            	      email  = (String) user.getProperty("email");
                                            	      LoginFB lfb = new LoginFB(email, getApplicationContext());
                                            	      boolean success = lfb.connection();
                                            	      if (success){
                                            	    	  Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            	    	  startActivity(i);
                                            	      }
                                              }
                                          }
                                      });
                    }else if(session.isClosed()) {
                txtSaludo.setText("¡Hola!");
                    }
                   }
                  });
        
        //Login Email
        btnLoginEmail = (Button) findViewById(R.id.login_email_button);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itemIntent = new Intent(InitActivity.this, LoginEmailActivity.class);
				InitActivity.this.startActivity(itemIntent);
				
			}
		});
        
        //Registro
        btnRegistro = (Button) findViewById(R.id.registro_button);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itemIntent = new Intent(InitActivity.this, RegistroActivity.class);
				InitActivity.this.startActivity(itemIntent);
				
			}
		});
    }


    @Override
    public void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    public void onStop() {
        mPlusClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login_gplus_button:
                int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (available != ConnectionResult.SUCCESS) {
                    showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                    return;
                }

                try {
                    mSignInStatus.setText(getString(R.string.signing_in_status));
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    // Fetch a new result to start.
                    mPlusClient.connect();
                }
                break;
            case R.id.sign_out_button:
                if (mPlusClient.isConnected()) {
                    mPlusClient.clearDefaultAccount();
                    mPlusClient.disconnect();
                    mPlusClient.connect();
                }
                break;
            case R.id.revoke_access_button:
                if (mPlusClient.isConnected()) {
                    //mPlusClient.revokeAccessAndDisconnect(this);
                    updateButtons(false /* isSignedIn */);
                }
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
            return super.onCreateDialog(id);
        }

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return null;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
        }
       return new AlertDialog.Builder(this)
                .setMessage("Error")
                .setCancelable(true)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        //Google
        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (resultCode == RESULT_OK && !mPlusClient.isConnected()
                    && !mPlusClient.isConnecting()) {
                // This time, connect should succeed.
                mPlusClient.connect();
            }
        }
        //Facebook
        else{
        	super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
    }
/*
    @Override
    public void onAccessRevoked(ConnectionResult status) {
        if (status.isSuccess()) {
            mSignInStatus.setText(R.string.revoke_access_status);
        } else {
            mSignInStatus.setText(R.string.revoke_access_error_status);
            mPlusClient.disconnect();
        }
        mPlusClient.connect();
    }*/

    @Override
    public void onConnected(Bundle connectionHint) {
        String currentPersonName = mPlusClient.getCurrentPerson() != null
                ? mPlusClient.getCurrentPerson().getDisplayName()
                : getString(R.string.unknown_person);
        mSignInStatus.setText(getString(R.string.signed_in_status, currentPersonName));
        updateButtons(true /* isSignedIn */);
        Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT);
    }

    @Override
    public void onDisconnected() {
        mSignInStatus.setText(R.string.loading_status);
        mPlusClient.connect();
        updateButtons(false /* isSignedIn */);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mConnectionResult = result;
        updateButtons(false /* isSignedIn */);
    }

    private void updateButtons(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.INVISIBLE);
            mSignOutButton.setEnabled(true);
            mRevokeAccessButton.setEnabled(true);
        } else {
            if (mConnectionResult == null) {
                // Disable the sign-in button until onConnectionFailed is called with result.
                mSignInButton.setVisibility(View.INVISIBLE);
                mSignInStatus.setText(getString(R.string.loading_status));
            } else {
                // Enable the sign-in button since a connection result is available.
                mSignInButton.setVisibility(View.VISIBLE);
                mSignInStatus.setText(getString(R.string.signed_out_status));
            }

            mSignOutButton.setEnabled(false);
            mRevokeAccessButton.setEnabled(false);
        }
    }
    
    
	
    
}
