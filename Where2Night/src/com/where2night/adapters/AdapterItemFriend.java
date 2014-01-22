package com.where2night.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.where2night.R;
import com.where2night.data.ItemFriend;
 
public class AdapterItemFriend extends BaseAdapter{
 
    protected Activity activity;
    protected ArrayList<ItemFriend> items;
 
    public AdapterItemFriend(Activity activity, ArrayList<ItemFriend> items) {
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
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemfriendslists, null);
        }
 
        // Creamos un objeto FriendsLists
        ItemFriend dir = items.get(position);
        //Rellenamos la picturegrafía
        ImageView picture = (ImageView) v.findViewById(R.id.picture);
        picture.setImageDrawable(dir.getPicture());
        //Rellenamos el name
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(dir.getName());
        //Rellenamos el ubication
        TextView club = (TextView) v.findViewById(R.id.ubication);
        club.setText(dir.getUbication());
        // Retornamos la vista
        return v;
    }
}
