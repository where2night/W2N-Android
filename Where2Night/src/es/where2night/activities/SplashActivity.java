package es.where2night.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Toast;

import com.where2night.R;

import es.where2night.utilities.DataManager;

public class SplashActivity extends Activity {
    private static final long DELAY = 2000;
    private boolean scheduled = false;
    private Timer splashTimer;
    
    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActionBar().hide();
        act = this;
        splashTimer = new Timer();
 		
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	 DataManager dm = new DataManager(getApplicationContext());
                 try{
                	 int logedin = dm.checkLogin();
                 	if (logedin != -2){
                 		Intent i = new Intent(SplashActivity.this, MainActivity.class);
                 		i.putExtra(MainActivity.PARENT, "3");
                 		if (logedin == 1){
                 			i.putExtra(MainActivity.PARENT, "4");
                 		}
                 		
                 		
                 		
                 		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         				startActivity(i);
                 	}else{
                 		SplashActivity.this.finish();
                 		Intent i = new Intent(SplashActivity.this, InitActivity.class);
                 		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         				startActivity(i);
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
