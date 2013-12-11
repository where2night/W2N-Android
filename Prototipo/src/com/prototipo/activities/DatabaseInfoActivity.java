package com.prototipo.activities;

import java.util.ArrayList;
import java.util.Arrays;

import com.prototipo.R;
import com.prototipo.R.layout;
import com.prototipo.R.menu;
import com.prototipo.fragments.DatabaseInfoFragment;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DatabaseInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_info);
		
		
		ListView list = (ListView)findViewById(R.id.listViewTest);
		String[] array_countries = new String[]{"Brasil", "Mexico", "Colombia", "Argentina",
				"Peru", "Venezuela", "Chile", "Ecuador", 
				"Guatemala", "Cuba"};
		ArrayList<String> countries = new ArrayList<String>(Arrays.asList(array_countries));
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,countries);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_info, menu);
		return true;
	}

}
