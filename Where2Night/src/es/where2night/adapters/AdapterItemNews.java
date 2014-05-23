package es.where2night.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import es.where2night.adapters.AdapterItemEvent.ViewHolderEvent;
import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemEventFriend;
import es.where2night.data.ItemFriendMode;
import es.where2night.data.ItemFriendState;
import es.where2night.data.ItemListFriend;
import es.where2night.data.ItemLocalCheck;
import es.where2night.data.ItemLocalGoes;
import es.where2night.data.ItemLocalNews;
import es.where2night.data.ItemNewList;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class AdapterItemNews extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<Item> items;
    private ImageLoader imageLoader;
    RequestQueue requestQueue;
    ViewHolderFriendMode holderFriendMode;
    ViewHolderFriendState holderFriendState;
    ViewHolderEvent holderEvent;
    ViewHolderLocalFollow holderLocal;
    ViewHolderLocalGoes holderLocalGoes;
    ViewHolderLocalCheck holderLocalCheck;
    ViewHolderEventFriend holderEventFriend;
    ViewHolderListFriend holderListFriend;
    ViewHolderListNew holderList;
    
	
	public AdapterItemNews(Activity activity, ArrayList<Item> items){
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
        ItemFriendMode dir = null;
        ItemFriendState fri = null;
        ItemEvent eve = null;
        ItemLocalNews loc = null;
        ItemEventFriend eFri = null;
        ItemLocalGoes locg = null;
        ItemLocalCheck locC = null;
        ItemNewList list = null;
        ItemListFriend listFriend = null;
        Item i = items.get(position);
 
        //Asociamos el layout de la lista que hemos creado
        //if(convertView == null){
        	
        	if(i.getClass() == ItemFriendMode.class){
        		dir = (ItemFriendMode)i;
        		holderFriendMode = new ViewHolderFriendMode();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendnews, null);
                holderFriendMode.picture = (NetworkImageView) v.findViewById(R.id.Friendpicture);
                holderFriendMode.txtName = (TextView) v.findViewById(R.id.txtName);
                holderFriendMode.txtMode = (TextView) v.findViewById(R.id.txtNews);
                v.setTag(holderFriendMode);
        	}
        	else if (i.getClass() == ItemFriendState.class){
        		fri = (ItemFriendState)i;
        		holderFriendState = new ViewHolderFriendState();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendnews, null);
                holderFriendState.picture = (NetworkImageView) v.findViewById(R.id.Friendpicture);
                holderFriendState.txtName = (TextView) v.findViewById(R.id.txtName);
                holderFriendState.txtState = (TextView) v.findViewById(R.id.txtNews);
                v.setTag(holderFriendState);
        	}
        	else if(i.getClass() == ItemEvent.class){
        		eve = (ItemEvent)i;
        		holderEvent = new ViewHolderEvent();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemevent, null);
                holderEvent.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
                holderEvent.txtTitle = (TextView) v.findViewById(R.id.txtEventTitle);
                holderEvent.txtClub = (TextView) v.findViewById(R.id.txtEventClub);
                holderEvent.txtDate = (TextView) v.findViewById(R.id.txtEventDate);
                holderEvent.txtText = (TextView) v.findViewById(R.id.txtEventText);
                holderEvent.txtTime = (TextView) v.findViewById(R.id.txtEventTime);
                holderEvent.btnSignMe = (Button) v.findViewById(R.id.btnSignMe);
                if (eve.isGoes()){
					holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holderEvent.btnSignMe.setSelected(true);
                }
                else{
                	holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holderEvent.btnSignMe.setSelected(false);
                }
                final ItemEvent eveFinal = eve;
                
                         
                holderEvent.btnSignMe.setOnClickListener(new OnClickListener() {
			
					@Override
					public void onClick(View v) {
						long eventId = eveFinal.getId();
						if (v.isSelected()){
							holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
				        	holderEvent.btnSignMe.setSelected(false);
				        	eveFinal.setGoes(false);
				        	
							goToEvent(eventId,true);
						}else{
							holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
				        	holderEvent.btnSignMe.setSelected(true);
				        	eveFinal.setGoes(true);
							goToEvent(eventId,false);
						}
						notifyDataSetChanged(); // tells the adapter that the data changed
					}
				});
		        
		        if (eve.isGoes()){
		        	holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holderEvent.btnSignMe.setSelected(true);
		        }else {
		        	holderEvent.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holderEvent.btnSignMe.setSelected(false);
		        } 
                
                v.setTag(holderEvent);
        	}
        	else if(i.getClass() == ItemLocalNews.class){
        		loc = (ItemLocalNews)i;
        		holderLocal = new ViewHolderLocalFollow();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemlocalnews, null);
                holderLocal.picture = (NetworkImageView) v.findViewById(R.id.LocalPhotoBig);
                holderLocal.txtNameLocal = (TextView) v.findViewById(R.id.txtLocalName);
                holderLocal.txtNameFriend = (TextView) v.findViewById(R.id.txtNameFriend);
                v.setTag(holderLocal);
        	}
        	else if(i.getClass() == ItemLocalCheck.class){
        		locC = (ItemLocalCheck)i;
        		holderLocalCheck = new ViewHolderLocalCheck();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemlocalnews, null);
                holderLocalCheck.picture = (NetworkImageView) v.findViewById(R.id.LocalPhotoBig);
                holderLocalCheck.txtNameLocal = (TextView) v.findViewById(R.id.txtLocalName);
                holderLocalCheck.txtNameFriend = (TextView) v.findViewById(R.id.txtNameFriend);
                v.setTag(holderLocalCheck);
        	}
        	else if(i.getClass() == ItemLocalGoes.class){
        		locg = (ItemLocalGoes)i;
        		holderLocalGoes = new ViewHolderLocalGoes();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemlocalgoes, null);
                holderLocalGoes.picture = (NetworkImageView) v.findViewById(R.id.LocalPhotoBig);
                holderLocalGoes.txtNameLocal = (TextView) v.findViewById(R.id.txtLocalName);
                holderLocalGoes.txtNameFriend = (TextView) v.findViewById(R.id.txtNameFriend);
                holderLocalGoes.txtDate = (TextView) v.findViewById(R.id.textDate);
                v.setTag(holderLocalGoes);
        	}
        	else if(i.getClass() == ItemNewList.class){
        		list = (ItemNewList)i;
        		holderList = new ViewHolderListNew();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemnewlist, null);
                holderList.picture = (NetworkImageView) v.findViewById(R.id.localListPicture);//FIXME
                holderList.nombre = (TextView) v.findViewById(R.id.textNameLocal);
                holderList.texto = (TextView) v.findViewById(R.id.textDiscount);
                holderList.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                holderList.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
                holderList.txtDate = (TextView) v.findViewById(R.id.txtDate);
                holderList.txtHour = (TextView) v.findViewById(R.id.txtHour);
                holderList.txtExpireDate = (TextView) v.findViewById(R.id.txtExpireDate);
                holderList.txtInvited = (TextView) v.findViewById(R.id.txtInvited);
                holderList.btnSignMe = (Button) v.findViewById(R.id.btnSignMeIn);
                holderList.spinnerInvited = (Spinner) v.findViewById(R.id.spinnerInvited);
                
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
        		        R.array.invited_array, android.R.layout.simple_spinner_item);
        		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		holderList.spinnerInvited.setAdapter(adapter);
                v.setTag(holderList);
                
                final ItemNewList listFinal = list;
                holderList.btnSignMe.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				long listId = listFinal.getId();
        				if (v.isSelected()){
        					holderList.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
        		        	holderList.btnSignMe.setSelected(false);
        		        	listFinal.setGoes(false);
        		        	holderList.txtInvited.setVisibility(View.VISIBLE);
        		        	holderList.spinnerInvited.setVisibility(View.VISIBLE);
        					singIntoList(listId,true);
        				}else{
        					holderList.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
        		        	holderList.btnSignMe.setSelected(true);
        		        	listFinal.setGoes(true);
        		        	singIntoList(listId,false);
        				}
        				notifyDataSetChanged(); // tells the adapter that the data changed
        			}
        		});
                
                if (list.isGoes()){
                	holderList.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
                	holderList.btnSignMe.setSelected(true);
                	holderList.txtInvited.setVisibility(View.GONE);
                	holderList.spinnerInvited.setVisibility(View.GONE);
                }else {
                	holderList.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
                	holderList.btnSignMe.setSelected(false);
                	holderList.txtInvited.setVisibility(View.VISIBLE);
                	holderList.spinnerInvited.setVisibility(View.VISIBLE);
                }
        	}
        	else if(i.getClass() == ItemListFriend.class){
        		listFriend = (ItemListFriend)i;
        		holderListFriend = new ViewHolderListFriend();
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemnewlist, null);
                holderListFriend.picture = (NetworkImageView) v.findViewById(R.id.localListPicture);//FIXME
                holderListFriend.nombre = (TextView) v.findViewById(R.id.textNameLocal);
                holderListFriend.texto = (TextView) v.findViewById(R.id.textDiscount);
                holderListFriend.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                holderListFriend.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
                holderListFriend.txtDate = (TextView) v.findViewById(R.id.txtDate);
                holderListFriend.txtHour = (TextView) v.findViewById(R.id.txtHour);
                holderListFriend.txtExpireDate = (TextView) v.findViewById(R.id.txtExpireDate);
                holderListFriend.txtInvited = (TextView) v.findViewById(R.id.txtInvited);
                holderListFriend.btnSignMe = (Button) v.findViewById(R.id.btnSignMeIn);
                holderListFriend.spinnerInvited = (Spinner) v.findViewById(R.id.spinnerInvited);
                
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
        		        R.array.invited_array, android.R.layout.simple_spinner_item);
        		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		holderListFriend.spinnerInvited.setAdapter(adapter);
                v.setTag(holderListFriend);
                
                final ItemListFriend listFriendFinal = listFriend;
                holderListFriend.btnSignMe.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				long listId = listFriendFinal.getId();
        				if (v.isSelected()){
        					holderListFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
        		        	holderListFriend.btnSignMe.setSelected(false);
        		        	listFriendFinal.setGoes(false);
        		        	holderListFriend.txtInvited.setVisibility(View.VISIBLE);
        		        	holderListFriend.spinnerInvited.setVisibility(View.VISIBLE);
        					singIntoList(listId,true);
        				}else{
        					holderListFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
        		        	holderListFriend.btnSignMe.setSelected(true);
        		        	listFriendFinal.setGoes(true);
        		        	singIntoList(listId,false);
        				}
        				notifyDataSetChanged(); // tells the adapter that the data changed
        			}
        		});
                
                if (listFriend.isGoes()){
                	holderListFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
                	holderListFriend.btnSignMe.setSelected(true);
                	holderListFriend.txtInvited.setVisibility(View.GONE);
                	holderListFriend.spinnerInvited.setVisibility(View.GONE);
                }else {
                	holderListFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
                	holderListFriend.btnSignMe.setSelected(false);
                	holderListFriend.txtInvited.setVisibility(View.VISIBLE);
                	holderListFriend.spinnerInvited.setVisibility(View.VISIBLE);
                }
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
                if (eFri.isGoes()){
					holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holderEventFriend.btnSignMe.setSelected(true);
                }
                else{
                	holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holderEventFriend.btnSignMe.setSelected(false);
                }
        		holderEventFriend.txtFriend = (TextView) v.findViewById(R.id.txtFriend);
        		v.setTag(holderEventFriend);
        		final ItemEventFriend eFriFinal = eFri;
        		         
                holderEventFriend.btnSignMe.setOnClickListener(new OnClickListener() {
			
					@Override
					public void onClick(View v) {
						long eventId = eFriFinal.getId();
						if (v.isSelected()){
							holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
				        	holderEventFriend.btnSignMe.setSelected(false);
				        	eFriFinal.setGoes(false);
				        	
							goToEvent(eventId,true);
						}else{
							holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
				        	holderEventFriend.btnSignMe.setSelected(true);
				        	eFriFinal.setGoes(true);
							goToEvent(eventId,false);
						}
						notifyDataSetChanged(); // tells the adapter that the data changed
					}
				});
		        
		        if (eFri.isGoes()){
		        	holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holderEventFriend.btnSignMe.setSelected(true);
		        }else {
		        	holderEventFriend.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holderEventFriend.btnSignMe.setSelected(false);
		        }
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
            holderFriendState.txtState.setText("Ha cambiado su estado " + state);
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
            	holderLocal.picture.setImageUrl(loc.getPicture(), imageLoader); 
            }
            //Rellenamos el nameLocal
            holderLocal.txtNameLocal.setText(loc.getNameLocal());
          //Rellenamos el nameFriend
            holderLocal.txtNameFriend.setText(loc.getNameFriend() + " está siguiendo a:");
        }
        else if(i.getClass() == ItemLocalCheck.class){
        	locC = (ItemLocalCheck)i;
        	//Rellenamos la picturegrafía
            if (!locC.getPicture().equals("") && !locC.getPicture().equals("null")){
            	holderLocalCheck.picture.setImageUrl(locC.getPicture(), imageLoader); 
            }
            //Rellenamos el nameLocal
            holderLocalCheck.txtNameLocal.setText(locC.getNameLocal());
          //Rellenamos el nameFriend
            holderLocalCheck.txtNameFriend.setText(locC.getNameFriend() + " ha hecho checkIn en:");
        }
        else if(i.getClass() == ItemLocalGoes.class){
        	locg = (ItemLocalGoes)i;
        	//Rellenamos la picturegrafía
            if (!locg.getPicture().equals("") && !locg.getPicture().equals("null")){
            	holderLocalGoes.picture.setImageUrl(locg.getPicture(), imageLoader); 
            }
            //Rellenamos el nameLocal
            holderLocalGoes.txtNameLocal.setText(locg.getNameLocal());
          //Rellenamos el nameFriend
            holderLocalGoes.txtNameFriend.setText(locg.getNameFriend() + " va a ir a:");
          //Rellenamos el date
            holderLocalGoes.txtDate.setText("el dia " + locg.getDate());
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
        else if(i.getClass() == ItemNewList.class){
        	list = (ItemNewList)i;
        	//Rellenamos
        	holderList.picture.setImageUrl(list.getPicture(),imageLoader);
        	holderList.nombre.setText(list.getNombre());
        	holderList.texto.setText(list.getText());
        	 holderList.txtTitle.setText(list.getTitle());
             holderList.txtDescription.setText(list.getDescription());
             holderList.txtDate.setText(list.getDate());
             String start = list.getStart().substring(0, 5);
             String end = list.getEnd().substring(0, 5);
             holderList.txtHour.setText(start + " - " + end);
             holderList.txtExpireDate.setText(list.getExpireDate());
             // Retornamos la vista
             return v;
        }
        else if(i.getClass() == ItemListFriend.class){
        	listFriend = (ItemListFriend)i;
        	//Rellenamos
        	holderListFriend.picture.setImageUrl(listFriend.getPicture(),imageLoader);
        	holderListFriend.nombre.setText(listFriend.getNombre());
        	holderListFriend.texto.setText(listFriend.getText());
        	 holderListFriend.txtTitle.setText(listFriend.getTitle());
             holderListFriend.txtDescription.setText(listFriend.getDescription());
             holderListFriend.txtDate.setText(listFriend.getDate());
             String start = listFriend.getStart().substring(0, 5);
             String end = listFriend.getEnd().substring(0, 5);
             holderListFriend.txtHour.setText(start + " - " + end);
             holderListFriend.txtExpireDate.setText(listFriend.getExpireDate());
             // Retornamos la vista
             return v;
        }

        return v;
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
	static class ViewHolderListNew{
		public NetworkImageView picture;
		public TextView texto;
		public TextView nombre;
		public TextView txtTitle;
		public TextView txtDescription;
		public TextView txtDate;
		public TextView txtHour;
		public TextView txtExpireDate;
		public TextView txtInvited;
		public Spinner spinnerInvited;
		public Button btnSignMe;
	}
	static class ViewHolderListFriend{
		public NetworkImageView picture;
		public TextView texto;
		public TextView nombre;
		public TextView txtTitle;
		public TextView txtDescription;
		public TextView txtDate;
		public TextView txtHour;
		public TextView txtExpireDate;
		public TextView txtInvited;
		public Spinner spinnerInvited;
		public Button btnSignMe;
	}
	static class ViewHolderLocalCheck {
		public NetworkImageView picture;
		public TextView txtNameLocal;
		public TextView txtNameFriend;
	}
	
	static class ViewHolderLocalGoes {
		public NetworkImageView picture;
		public TextView txtNameLocal;
		public TextView txtNameFriend;
		public TextView txtDate;
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
	
	private void singIntoList(long listtId,boolean notGoing){
		final DataManager dm = new DataManager(activity.getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = Helper.joinDiscountListUrl() + "/" + cred[0] + "/" + cred[1] + "/" + listtId;
		 
		Response.Listener<String> succeedListener = new Response.Listener<String>(){
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	/*JSONObject respuesta = new JSONObject(response);
	            	if (respuesta.getString("join").equals("true")){
	            		
	            	}else if (respuesta.getString("join").equals("false")){
	            		
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
			request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener){     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	HashMap<String, String> info = new HashMap<String, String>();
			    	info.put("numGuest", ((Integer)holderList.spinnerInvited.getSelectedItemPosition()).toString());
			    				    	
			    	return info;
			    }
		};

		}
		requestQueue.add(request);
	}

}
