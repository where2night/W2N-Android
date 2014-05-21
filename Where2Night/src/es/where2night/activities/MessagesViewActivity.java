package es.where2night.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;
import com.where2night.R.id;
import com.where2night.R.layout;
import com.where2night.R.menu;

import es.where2night.adapters.AdapterItemFriendList;
import es.where2night.adapters.AdapterItemMessage;
import es.where2night.data.ItemFriend;
import es.where2night.data.ItemMessage;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Build;

public class MessagesViewActivity extends Activity implements OnClickListener{

	private ListView messagesList;
	public static final String ID = "id";
	private String friendId = "";
	private RequestQueue requestQueue;
	ArrayList<ItemMessage> arraydir;
	AdapterItemMessage adapter;
	private EditText messageToSent;
	private Button btnEnviar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_view);
		
		friendId = getIntent().getStringExtra(ID);
		messagesList = (ListView) findViewById(R.id.listMessages);
		messageToSent = (EditText) findViewById(R.id.editMessage);
		btnEnviar = (Button) findViewById(R.id.buttonSendMessage);
		messageToSent.setText("");
		btnEnviar.setOnClickListener(this); 

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		fillMessages();
		hideKeyBoard();
	}

	private void fillMessages() {
		arraydir = new ArrayList<ItemMessage>();
		adapter = new AdapterItemMessage(this, arraydir);
        messagesList.setAdapter(adapter);
		final DataManager dm = new DataManager(this.getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(this.getApplicationContext()); 
		String url = Helper.getMessage() + "/" + cred[0] + "/" + cred[1] + "/" + friendId;
		Log.e("Messages", url);
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	JSONObject root = new JSONObject(response);
	            	for (int i = root.length() - 8; i >= 0; i--){
	            		JSONObject aux = new JSONObject(root.getString(String.valueOf(i)));
	            		String message = aux.getString("message");
	            		int mode = Integer.parseInt(aux.getString("mode"));
	            		String date = aux.getString("createdTime");
	            		ItemMessage mes = new ItemMessage(message,mode,date);
	            		arraydir.add(mes);
	            	}
            	adapter.notifyDataSetChanged();

	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	    	}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener() 
		{
		     @Override
		     public void onErrorResponse(VolleyError error) {
		         // error
		    	 Log.e("Error.Response", error.toString());
		     }
		};
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
		
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_messages_view,
					container, false);
			return rootView;
		}
	}*/

	@Override
	public void onClick(View v) {
		String vacio ="";
		if (!messageToSent.getText().toString().equals(vacio)){
			final DataManager dm = new DataManager(getApplicationContext());
			String[] cred = dm.getCred();
			requestQueue = Volley.newRequestQueue(getApplicationContext()); 
			String url = Helper.getSendMessageUrl() + "/" + cred[0] + "/" + cred[1] + "/" + friendId;
			Log.e("url", url);
			
			Response.Listener<String> succeedListener = new Response.Listener<String>(){
		        @Override
		        public void onResponse(String response) {
		            // response
		        	Log.e("Response", response);
		            try {} 
		            catch (Exception e) {
		            	e.printStackTrace();
		            }
		        }		
			};
			    
		    Response.ErrorListener errorListener = new Response.ErrorListener() 
		    {
		         @Override
		         public void onErrorResponse(VolleyError error) {
		             // error
		             Log.e("Error.Response", error.toString());
		       }
		    };
		    
		    StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener){
		    	@Override
			    protected Map<String, String> getParams() 
			    {  
			    	Map<String, String> info = new HashMap<String, String>();
			    	String strMessage = messageToSent.getText().toString();
			    	info.put("message", strMessage);
			    	runOnUiThread(new Runnable() {
			    	    @Override
			    	    public void run() {
			    	    	messageToSent.setText("");
			    	    	fillMessages();
			    	    }
			    	});
			        return info;
			    }
		    }; 
			
			requestQueue.add(request);
			hideKeyBoard();
			
		}
		else{
			hideKeyBoard();
		}
		
	}

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(messageToSent.getWindowToken(), 0);
		
	}

}
