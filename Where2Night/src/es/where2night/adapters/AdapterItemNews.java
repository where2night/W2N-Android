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
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemEventFriend;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemFriendState;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.data.ItemLocalNews;
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
    ViewHolderFriendMode holderFriendMode;
    ViewHolderFriendState holderFriendState;
    ViewHolderEvent holderEvent;
    ViewHolderLocalFollow holderLocal;
    ViewHolderEventFriend holderEventFriend;
    
	
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
        ItemFriendMode dir = null;
        ItemFriendState fri = null;
        ItemEvent eve = null;
        ItemLocalNews loc = null;
        ItemEventFriend eFri = null;
        Item i = items.get(position);
 
        //Asociamos el layout de la lista que hemos creado
        //if(convertView == null){
        	
        	if(i.getClass() == ItemFriendMode.class){
        		dir = (ItemFriendMode)i;
        		holderFriendMode = new ViewHolderFriendMode();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendnews, null);
                holderFriendMode.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderFriendMode.txtName = (TextView) v.findViewById(R.id.txtName);
                holderFriendMode.txtMode = (TextView) v.findViewById(R.id.txtNews);
                v.setTag(holderFriendMode);
        	}
        	else if (i.getClass() == ItemFriendState.class){
        		fri = (ItemFriendState)i;
        		holderFriendState = new ViewHolderFriendState();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendnews, null);
                holderFriendState.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderFriendState.txtName = (TextView) v.findViewById(R.id.txtName);
                holderFriendState.txtState = (TextView) v.findViewById(R.id.txtNews);
                v.setTag(holderFriendState);
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
    				//		holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
    						v.setSelected(false);
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.SignMe));
    						goToEvent(eventId,true);
    					}else{
    					//	holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.Signed));
    						v.setSelected(true);
    						goToEvent(eventId,false);
    					}
    				}
    			});*/ //FIXME
                
                v.setTag(holderEvent);
        	}
        	else if(i.getClass() == ItemLocalNews.class){
        		loc = (ItemLocalNews)i;
        		holderLocal = new ViewHolderLocalFollow();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemlocalnews, null);
                holderLocal.picture = (NetworkImageView) v.findViewById(R.id.LocalPicture);
                holderLocal.txtNameLocal = (TextView) v.findViewById(R.id.txtLocalName);
                holderLocal.txtNameFriend = (TextView) v.findViewById(R.id.txtNameFriend);
                v.setTag(holderLocal);
        	}
        	else if(i.getClass() == ItemEventFriend.class){
        		eFri = (ItemEventFriend)i;
        		holderEventFriend = new ViewHolderEventFriend();
        		LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemeventfriend, null);
                holderEventFriend.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderEventFriend.txtTitle = (TextView) v.findViewById(R.id.txtEventTitle);
                holderEventFriend.txtClub = (TextView) v.findViewById(R.id.txtEventClub);
                holderEventFriend.txtDate = (TextView) v.findViewById(R.id.txtEventDate);
                holderEventFriend.txtText = (TextView) v.findViewById(R.id.txtEventText);
                holderEventFriend.txtTime = (TextView) v.findViewById(R.id.txtEventTime);
                holderEventFriend.btnSignMe = (Button) v.findViewById(R.id.btnSignMe);
        		holderEventFriend.txtFriend = (TextView) v.findViewById(R.id.txtFriend);
        		v.setTag(holderEventFriend);
        		
        		/*         
                holderEventFriend.btnSignMe.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					long eventId = eFri.getId();
    					if (v.isSelected()){
    				//		holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
    						v.setSelected(false);
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.SignMe));
    						goToEvent(eventId,true);
    					}else{
    					//	holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
    						Button b = (Button) v.findViewById(v.getId());
    						b.setText(activity.getResources().getString(R.string.Signed));
    						v.setSelected(true);
    						goToEvent(eventId,false);
    					}
    				}
    			});*/ //FIXME
        	}
       /* }
        else{
        	if(i.getClass() == ItemFriendMode.class){
        		holderFriendMode = (ViewHolderFriendMode) convertView.getTag();
        	}
        	else if(i.getClass() == ItemLocalNews.class){
        		holderLocal = (ViewHolderLocalFollow) convertView.getTag();
        	}
        	else if(i.getClass() == ItemFriendState.class){
        		holderFriendState = (ViewHolderFriendState) convertView.getTag();
        	}
        	else if(i.getClass() == ItemEvent.class){
        		holderEvent = (ViewHolderEvent) convertView.getTag();
        	}
        	else if(i.getClass() == ItemEventFriend.class){
        		holderEventFriend = (ViewHolderEventFriend) convertView.getTag();
        	}
        }*/
        
        
        //RELLENAMOS
        if(i.getClass() == ItemFriendMode.class){
        	dir = (ItemFriendMode)i;
        	//Rellenamos la picturegrafía
            if (!dir.getPicture().equals("")){
            	holderFriendMode.picture.setImageUrl(dir.getPicture(), imageLoader);
            }
            //Rellenamos el name
            holderFriendMode.txtName.setText(dir.getName());
            //Rellenamos el mode
            int modeInt = Integer.parseInt(dir.getMode());
            String modeString = "";
            switch (modeInt){
            	case 0:
            		modeString = "'De tranquis'";
            		break;
            	case 1:
            		modeString = "'Hoy no me lio'";
            		break;
            	case 2:
            		modeString = "'Lo que surja'";
            		break;
            	case 3:
            		modeString = "'Lo daré todo'";
            		break;
            	case 4:
            		modeString = "'Destroyer'";
            		break;
            	case 5:
            		modeString = "'Yo me llamo Ralph'";
            		break;
            }
            holderFriendMode.txtMode.setText("Ha cambiado su modo de fiesta a " + modeString);
        }
        else if(i.getClass() == ItemFriendState.class){
        	fri = (ItemFriendState)i;
        	//Rellenamos la picturegrafía
            if (!fri.getPicture().equals("")){
            	holderFriendState.picture.setImageUrl(fri.getPicture(), imageLoader);
            }
            //Rellenamos el name
            holderFriendState.txtName.setText(fri.getName());
            //Rellenamos el mode
            String state = "'" + fri.getState() + "'";
            holderFriendMode.txtMode.setText("Ha cambiado su estado " + state);
        }
        else if(i.getClass() == ItemEvent.class){
        	eve = (ItemEvent)i;
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
        else if(i.getClass() == ItemLocalNews.class){
        	loc = (ItemLocalNews)i;
        	//Rellenamos la picturegrafía
            if (!loc.getPicture().equals("") && !loc.getPicture().equals("null")){
            	holderLocal.picture.setImageUrl(loc.getPicture(), imageLoader); //FIXME
            }
            //Rellenamos el nameLocal
            holderLocal.txtNameLocal.setText(loc.getNameLocal());
          //Rellenamos el nameFriend
            holderLocal.txtNameFriend.setText(loc.getNameFriend() + " está siguiendo a:");
        }
        else if(i.getClass() == ItemEventFriend.class){
        	eFri = (ItemEventFriend)i;
        	//Rellenamos la picturegrafía
            if (!eFri.getPicture().equals("")){
            	holderEventFriend.picture.setImageUrl(eFri.getPicture(), imageLoader);
            }
            //Rellenamos el name
            holderEventFriend.txtTitle.setText(eFri.getTitle());
            //Rellenamos el club
            holderEventFriend.txtClub.setText(eFri.getNameEvent());
          //Rellenamos el date
            holderEventFriend.txtDate.setText(eFri.getDate());
            holderEventFriend.txtText.setText(eFri.getText());
            holderEventFriend.txtTime.setText(eFri.getStart() + " - " + eFri.getClose());
            holderEventFriend.txtFriend.setText(eFri.getNameFriend() + " va a ir a este evento");
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
	
	static class ViewHolderFriendMode {
		public NetworkImageView picture;
		public TextView txtName;
		public TextView txtMode;
	}
	
	static class ViewHolderFriendState {
		public NetworkImageView picture;
		public TextView txtName;
		public TextView txtState;
	}
	
	static class ViewHolderLocalFollow {
		public NetworkImageView picture;
		public TextView txtNameLocal;
		public TextView txtNameFriend;
	}
	
	static class ViewHolderEventFriend {
			public NetworkImageView picture;
			public TextView txtTitle;
			public TextView txtClub;
			public TextView txtDate;
			public TextView txtText;
			public TextView txtTime;
			public Button btnSignMe;
			public TextView txtFriend;
	}

}
