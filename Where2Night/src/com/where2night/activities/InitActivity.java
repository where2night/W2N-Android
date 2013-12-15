package com.where2night.activities;

import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.where2night.R;

public class InitActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_init);
                
                final TextView txtSaludo = (TextView)findViewById(R.id.txtSaludo);
                
                LoginButton authButton = (LoginButton) findViewById(R.id.login_button);
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
                                                              txtSaludo.setText(R.id.txtSaludo + " " + user.getUsername());                                                              
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
         public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
             Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
         }
}
