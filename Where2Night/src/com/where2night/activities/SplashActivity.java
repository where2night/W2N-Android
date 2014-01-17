package com.where2night.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Toast;

import com.where2night.R;
import com.where2night.R.layout;
import com.where2night.utilities.DataManager;

public class SplashActivity extends Activity
{
    private static final long DELAY = 3000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActionBar().hide();
        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	
            	 DataManager dm = new DataManager(getApplicationContext());
                 try{
                 	if (dm.checkLogin()){
                 		Intent i = new Intent(SplashActivity.this, MainActivity.class);
         				startActivity(i);
                 	}
                 	else{
                 		SplashActivity.this.finish();
                        startActivity(new Intent(SplashActivity.this, InitActivity.class));
                 	}
                 } catch(SQLException e){
                 	Toast.makeText(getApplicationContext(), "Se ha producido un error al hacer Login", Toast.LENGTH_SHORT).show();
                 }
                
            }
         }, DELAY);
       scheduled = true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
    }
}
