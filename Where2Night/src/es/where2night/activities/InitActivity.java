package es.where2night.activities;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.where2night.R;

import es.where2night.utilities.DataManager;
import es.where2night.utilities.MomentUtil;

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
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getActionBar().hide();
        
        //Google
        mPlusClient = new PlusClient.Builder(this, this, this)
        					.setActions(MomentUtil.ACTIONS)
        					.build();
        
        if (mPlusClient.isConnected()) {
            mPlusClient.clearDefaultAccount();
            mPlusClient.disconnect();
        }
        
        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton = (SignInButton) findViewById(R.id.login_gplus_button);
        mSignInButton.setOnClickListener(this);
        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);
        mRevokeAccessButton = findViewById(R.id.revoke_access_button);
        mRevokeAccessButton.setOnClickListener(this);
        
        //FaceBook
        
        LoginButton authButton = (LoginButton) findViewById(R.id.login_fb_button);
          authButton.setOnErrorListener(new OnErrorListener() {
           
           @Override
           public void onError(FacebookException error) {
                   
           }
        });
          
        authButton.setReadPermissions(Arrays.asList("basic_info","email","user_birthday"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {
                   
	    @SuppressWarnings("deprecation")
		@Override
           public void call(Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {                                      
	             Request.executeMeRequestAsync(session,
	                     new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user, Response response) {
								if (user != null) {
									String email = user.getProperty("email").toString();
									String name = user.getFirstName();
									String surnames = user.getLastName();
									String gender = user.getProperty("gender").toString();
									String birthdate = user.getBirthday();
									String fbId = user.getId();
									String picture = "http://graph.facebook.com/" + fbId + "/picture?type=large";
									DataManager dm = new DataManager(getApplicationContext());
									dm.setUser(email,picture,name,surnames, birthdate, gender);
									Intent i = new Intent(getApplicationContext(), MainActivity.class);
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									i.putExtra(MainActivity.EMAIL, email);
									i.putExtra(MainActivity.TYPE, "0");
									i.putExtra(MainActivity.PARENT, "1");
									startActivity(i);
					              }
								
							}
	                     });
	   }else if(session.isClosed()) {
	   }
		}                 
          });
        
        //Login Email
        btnLoginEmail = (Button) findViewById(R.id.btnLoginEmail);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itemIntent = new Intent(InitActivity.this, LoginEmailActivity.class);
				InitActivity.this.startActivity(itemIntent);
				
			}
		});
        
        //Registro
        btnRegistro = (Button) findViewById(R.id.btnRegister);
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
            //	mPlusClient.connect();
                int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (available != ConnectionResult.SUCCESS) {
                    showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                    return;
                }

                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
                } catch (Exception e) {
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
                    updateButtons(false /* isSignedIn*/);
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
        if (mPlusClient.getCurrentPerson() != null){
	    	Person person = mPlusClient.getCurrentPerson();
	    	String email = mPlusClient.getAccountName();
			String name = person.getName().getGivenName();
			String surnames = person.getName().getFamilyName();
			String gender = "";
			if (person.getGender() == 0)
				gender = "male";
			else if (person.getGender() == 1)
				gender = "female";
			String birthdate = person.getBirthday();
			String picture = person.getImage().getUrl();
			DataManager dm = new DataManager(getApplicationContext());
			dm.setUser(email,picture,name,surnames, birthdate, gender);
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.putExtra(MainActivity.EMAIL, email);
			i.putExtra(MainActivity.TYPE, "1");
			i.putExtra(MainActivity.PARENT, "2");
			startActivity(i);
        }
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
