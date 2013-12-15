package com.where2night.activities;

import java.util.Arrays;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;

import com.where2night.R;

public class InitActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener{

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    
    private SignInButton btnSignIn;
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_init);
                
                //Google+
                btnSignIn = (SignInButton)findViewById(R.id.login_gplus_button);
                
                mPlusClient = new PlusClient.Builder(this, this, this)
                	.setActions("http://schemas.google.com/AddActivity","http://schemas.google.org/BuyActivity")
                    .build();
                
                mConnectionProgressDialog = new ProgressDialog(this);
                mConnectionProgressDialog.setMessage("Conectando...");
         
                btnSignIn.setOnClickListener(new OnClickListener() {
                	 
                    @Override
                    public void onClick(View view)
                    {
                    	if (view.getId() == R.id.login_gplus_button && !mPlusClient.isConnected()) {
                            if (mConnectionResult == null) {
                                mConnectionProgressDialog.show();
                            } else {
                                try {
                                    mConnectionResult.startResolutionForResult(InitActivity.this, REQUEST_CODE_RESOLVE_ERR);
                                } catch (SendIntentException e) {
                                    // Try connecting again.
                                    mConnectionResult = null;
                                    mPlusClient.connect();
                                }
                            }
                        }
                    }
                });
            
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
                                                              txtSaludo.setText(R.id.txtSaludo + " " + user.getName());                                                              
                                                      }
                                                  }
                                              });
                            }else if(session.isClosed()) {
                        txtSaludo.setText("¡Hola!");
                            }
                           }
                          });
                
        }
        
        @Override
        public void onConnected(Bundle connectionHint) {
            mConnectionProgressDialog.dismiss();
            Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        }
        
        @Override
         public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                
         }

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}
}
