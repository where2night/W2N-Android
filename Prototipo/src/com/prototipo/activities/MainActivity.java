package com.prototipo.activities;

import com.prototipo.R;
import com.prototipo.fragments.DatabaseInfoFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{

	
	private String email = "";
	private String password  = "";
	private EditText editEmail;
	private EditText editPassword;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnSend = (Button)findViewById(R.id.btnSend);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        
        
        btnSend.setOnClickListener(this);
        
        FragmentManager manager = getSupportFragmentManager();
        DatabaseInfoFragment fragment = (DatabaseInfoFragment) manager.findFragmentById(R.id.fragmentDatabaseInfo);
        fragment.loadContent();
        
        
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		
		email = editEmail.getText().toString();
		password = editPassword.getText().toString();
		
		Toast.makeText(this, "Email: " + email + " - Password: " + password, Toast.LENGTH_SHORT).show();
		
		editEmail.setText("");
		editPassword.setText("");
	}
    
}
