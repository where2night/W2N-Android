package es.where2night.adapters;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.adapters.AdapterItemEvent.ViewHolderEvent;
import es.where2night.adapters.AdapterItemFriend.ViewHolderFriend;
import es.where2night.adapters.AdapterItemLocal.ViewHolderLocal;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemFriend;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterItemNews extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<Item> items;
    private ImageLoader imageLoader;
    RequestQueue requestQueue;
    ViewHolderFriend holderFriend;
    ViewHolderEvent holderEvent;
    ViewHolderLocal holderLocal;
    
    
	
	public AdapterItemNews(Activity activity, ArrayList<Item> items){
		this.activity = activity;
        this.items = items;
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
        ItemFriend dir = null;
        ItemEvent eve = null;
        final ItemLocalAndDJ loc = null;
        Item i = new Item();
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	i = items.get(position);
        	if(i.getClass() == ItemFriend.class){
        		dir = (ItemFriend)i;
        		holderFriend = new ViewHolderFriend();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendslists, null);
                holderFriend.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderFriend.txtName = (TextView) v.findViewById(R.id.txtName);
                holderFriend.txtUbication = (TextView) v.findViewById(R.id.txtUbicationFriend);
                holderFriend.txtEstado = (TextView) v.findViewById(R.id.txtEstado);
                v.setTag(holderFriend);
        	}
        	else if(i.getClass() == ItemEvent.class){
        		eve = (ItemEvent)i;
        		holderEvent = new ViewHolderEvent();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemeventslists, null);
                holderEvent.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderEvent.txtTitle = (TextView) v.findViewById(R.id.txtEventTitle);
                holderEvent.txtClub = (TextView) v.findViewById(R.id.txtEventClub);
                holderEvent.txtDate = (TextView) v.findViewById(R.id.txtEventDate);
                holderEvent.txtText = (TextView) v.findViewById(R.id.txtEventText);
                holderEvent.txtTime = (TextView) v.findViewById(R.id.txtEventTime);
                holderEvent.btnSignMe = (Button) v.findViewById(R.id.btnSignMe);
                
                /*         
                holderEvent.btnSignMe.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					long eventId = eve.getId();
    					if (v.isSelected()){
    				//		holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
    						v.setSelected(false);
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.SignMe));
    						goToEvent(eventId,true);
    					}else{
    					//	holder.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.Signed));
    						v.setSelected(true);
    						goToEvent(eventId,false);
    					}
    				}
    			});*/ //FIXME
                
                v.setTag(holderEvent);
        	}
        	else if(i.getClass() == ItemLocalAndDJ.class){
        		holderLocal = new ViewHolderLocal();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemlocallist, null);
                holderLocal.picture = (NetworkImageView) v.findViewById(R.id.LocalPicture);
                holderLocal.txtname = (TextView) v.findViewById(R.id.txtLocalName);
                v.setTag(holderLocal);
        	}
        }
        else{
        	if(i.getClass() == ItemFriend.class){
        		holderFriend = (ViewHolderFriend) convertView.getTag();
        	}
        	else if(i.getClass() == ItemEvent.class){
        		holderEvent = (ViewHolderEvent) convertView.getTag();
        	}
        	else if(i.getClass() == ItemLocalAndDJ.class){
        		holderLocal = (ViewHolderLocal) convertView.getTag();
        	}
        }
        
        
        //RELLENAMOS
        if(i.getClass() == ItemFriend.class){
        	//Rellenamos la picturegrafía
            if (!dir.getPicture().equals("")){
            	holderFriend.picture.setImageUrl(dir.getPicture(), imageLoader);
            }
            //Rellenamos el name
            holderFriend.txtName.setText(dir.getTitle());
            //Rellenamos el ubication
            holderFriend.txtUbication.setText(dir.getUbication() + " en plan " + dir.getMode());
            //Rellenamos el estado
            holderFriend.txtEstado.setText(dir.getEstado());
        }
        else if(i.getClass() == ItemEvent.class){
        	//Rellenamos la picturegrafía
            if (!eve.getPicture().equals("")){
            	holderEvent.picture.setImageUrl(eve.getPicture(), imageLoader);
            }
            //Rellenamos el name
            holderEvent.txtTitle.setText(eve.getTitle());
            //Rellenamos el club
            holderEvent.txtClub.setText(eve.getName());
          //Rellenamos el date
            holderEvent.txtDate.setText(eve.getDate());
            holderEvent.txtText.setText(eve.getText());
            holderEvent.txtTime.setText(eve.getStart() + " - " + eve.getClose());
        }
        else if(i.getClass() == ItemLocalAndDJ.class){
        	//Rellenamos la picturegrafía
            if (!loc.getPicture().equals("") && !loc.getPicture().equals("null")){
            	holderLocal.picture.setImageUrl(loc.getPicture(), imageLoader); //FIXME
            }
            //Rellenamos el name
            holderLocal.txtname.setText(loc.getName());
        }

        return v;
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

}
