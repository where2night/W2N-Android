package es.where2night.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.where2night.R;


public class LocalsSearchResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 setContentView(R.layout.fragment_list);
		 
		 Log.e("Search actvity", "entra");
		 // Get the intent, verify the action and get the query
		    Intent intent = getIntent();
		    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		      String query = intent.getStringExtra(SearchManager.QUERY);
		   // doMySearch(query);
		    }
	}
 
 
    
 
}