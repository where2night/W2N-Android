package es.where2night.adapters;

import java.util.ArrayList;

import com.where2night.R;

import es.where2night.data.Item;
import es.where2night.data.ItemEvent;
import es.where2night.data.ItemFriend;
import es.where2night.data.ItemLocalAndDJ;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterItemNews extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<Item> items;
	
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
        ItemLocalAndDJ loc = null;
        Item i = new Item();
 
        //Asociamos el layout de la lista que hemos creado
        //if(convertView == null){
        	i = items.get(position);
        	if(i.getClass() == ItemFriend.class){
        		dir = (ItemFriend)i;
        		LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemfriendslists, null);
        	}
        	else if(i.getClass() == ItemEvent.class){
        		eve = (ItemEvent)i;
        		LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemeventslists, null);
        	}
            
        //}
        if(i.getClass() == ItemFriend.class){
        	//Rellenamos la picturegrafía
 	        ImageView picture = (ImageView) v.findViewById(R.id.picture);
 	        picture.setImageDrawable(dir.getPicture());
 	        //Rellenamos el name
 	        TextView name = (TextView) v.findViewById(R.id.name);
 	        name.setText(dir.getTitle());
 	        //Rellenamos el ubication
 	        TextView club = (TextView) v.findViewById(R.id.ubication);
 	        club.setText(dir.getUbication());
        }
        else if(i.getClass() == ItemEvent.class){
        	//Rellenamos la picturegrafía
            ImageView picture = (ImageView) v.findViewById(R.id.Eventpicture);
            //TODO Cambiar imageview por volleyimageview 
            //  picture.setImageDrawable(eve.getPicture());
            //Rellenamos el name
            TextView name = (TextView) v.findViewById(R.id.txtEventTitle);
            name.setText(dir.getTitle());
            //Rellenamos el club
            TextView club = (TextView) v.findViewById(R.id.txtEventClub);
            club.setText(dir.getTitle());
            //Rellenamos el date
            TextView date = (TextView) v.findViewById(R.id.txtEventDate);
            date.setText(eve.getDate());
        }

        return v;
	}

}
