package com.prototipo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.prototipo.R;
import com.prototipo.fragments.DatabaseInfoFragment;

public class DatabaseInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_info);
		
		FragmentManager manager = getSupportFragmentManager();
        DatabaseInfoFragment fragment = (DatabaseInfoFragment) manager.findFragmentById(R.id.fragmentDatabaseInfo);
		fragment.loadContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_info, menu);
		return true;
	}

}
