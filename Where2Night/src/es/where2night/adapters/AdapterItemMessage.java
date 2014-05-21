package es.where2night.adapters;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.where2night.R;

import es.where2night.adapters.AdapterItemSong.ViewHolderSong;
import es.where2night.data.ItemMessage;
import es.where2night.data.ItemSong;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterItemMessage extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<ItemMessage> items;
    ViewHolderMessage holder;
    RequestQueue requestQueue;
    
    public AdapterItemMessage(Activity activity, ArrayList<ItemMessage> items) {
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Generamos una convertView por motivos de eficiencia
        View v = convertView;
        // Creamos un objeto ItemFriend
        final ItemMessage dir = items.get(position); 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderMessage();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemmessage, null);
            holder.message = (TextView) v.findViewById(R.id.textMessage);
            holder.mode = (TextView) v.findViewById(R.id.textMessageRead);
            holder.date = (TextView) v.findViewById(R.id.textMessageDate);
            v.setTag(holder);
        }
        else{
        	holder = (ViewHolderMessage) convertView.getTag();
        }
        
        
        //Rellenamos
        if(dir.getMode() == 0){
        	holder.mode.setText("Enviado");
        }
        else if(dir.getMode() == 1){
        	holder.mode.setText("Recibido");
        }
        else if(dir.getMode() == 2){
        	holder.mode.setText("Leido");
        }
        holder.message.setText(dir.getMessage());
        holder.date.setText(dir.getDate());
        
        // Retornamos la vista
        return v;
        
	}
	
	static class ViewHolderMessage {
		public TextView message;
		public TextView mode;
		public TextView date;
	}

}
