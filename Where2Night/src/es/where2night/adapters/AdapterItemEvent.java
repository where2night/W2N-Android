package es.where2night.adapters;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.data.ItemEvent;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
 
public class AdapterItemEvent extends BaseAdapter implements OnClickListener{
 
    protected Activity activity;
    protected ArrayList<ItemEvent> items;
    ViewHolderEvent holder;
    private ImageLoader imageLoader;
    RequestQueue requestQueue;
 
    public AdapterItemEvent(Activity activity, ArrayList<ItemEvent> items) {
        this.activity = activity;
        this.items = items;
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        this.imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
      }
 
    @Override
    public int getCount() {
        return items.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }
 
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;
        // Creamos un objeto ItemEvent
        final ItemEvent dir = items.get(position);
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderEvent();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemeventslists, null);
            holder.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
            holder.txtTitle = (TextView) v.findViewById(R.id.txtEventTitle);
            holder.txtClub = (TextView) v.findViewById(R.id.txtEventClub);
            holder.txtDate = (TextView) v.findViewById(R.id.txtEventDate);
            holder.txtText = (TextView) v.findViewById(R.id.txtEventText);
            holder.txtTime = (TextView) v.findViewById(R.id.txtEventTime);
            holder.btnSignMe = (Button) v.findViewById(R.id.btnSignMe);
            
            v.setTag(holder);
        } else {
        	holder = (ViewHolderEvent) convertView.getTag();
        }
        
       
        
        holder.btnSignMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long eventId = dir.getId();
				if (v.isSelected()){
					holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holder.btnSignMe.setSelected(false);
		        	dir.setGoes(false);
		        	
					goToEvent(eventId,true);
				}else{
					holder.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holder.btnSignMe.setSelected(true);
		        	dir.setGoes(true);
					goToEvent(eventId,false);
				}
				notifyDataSetChanged(); // tells the adapter that the data changed
			}
		});
        
        if (dir.isGoes()){
        	holder.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
        	holder.btnSignMe.setSelected(true);
        }else {
        	holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
        	holder.btnSignMe.setSelected(false);
        }
        
        //Rellenamos la picturegrafía
        if (!dir.getPicture().equals("")){
        	holder.picture.setImageUrl(dir.getPicture(), imageLoader);
        }
        //Rellenamos el name
        holder.txtTitle.setText(dir.getTitle());
        //Rellenamos el club
        holder.txtClub.setText(dir.getName());
      //Rellenamos el date
        holder.txtDate.setText(dir.getDate());
        holder.txtText.setText(dir.getText());
        holder.txtTime.setText(dir.getStart() + " - " + dir.getClose());
        // Retornamos la vista
        return v;
    }
    
    
    static class ViewHolderEvent {
		public NetworkImageView picture;
		public TextView txtTitle;
		public TextView txtClub;
		public TextView txtDate;
		public TextView txtText;
		public TextView txtTime;
		public Button btnSignMe;
	}
    

	private void goToEvent(long eventId,boolean notGoing){
		final DataManager dm = new DataManager(activity.getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = Helper.getGoToEventUrl() + "/" + cred[0] + "/" + cred[1] + "/" + eventId;
		Log.e("AdapterURl", url); 
		 
		Response.Listener<String> succeedListener = new Response.Listener<String>(){
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	/*JSONObject respuesta = new JSONObject(response);
	            	if (respuesta.getString("goto").equals("true")){
	            		
	            	}else if (respuesta.getString("goto").equals("false")){
	            		
	            	}*/
            	}
	            catch (Exception e) {
					e.printStackTrace();
				}
	        
	        }
		};
		Response.ErrorListener errorListener = new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
             // error
				Log.e("Error.Response", error.toString());
			}
		};
		
		StringRequest request;
		
		if(notGoing){
			request = new StringRequest(Request.Method.DELETE, url, succeedListener, errorListener); 
		}
		else{
			request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 

		}
		requestQueue.add(request);
	}

	@Override
	public void onClick(View v) {
		
		
	}

	

}
