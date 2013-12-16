package com.where2night.activities;

import com.where2night.R;
import com.where2night.R.layout;
import com.where2night.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LoginEmailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_email);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_email, menu);
		return true;
	}

}
