package es.where2night.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.data.ItemEvent;
import es.where2night.utilities.BitmapLRUCache;
 
public class AdapterItemEvent extends BaseAdapter{
 
    protected Activity activity;
    protected ArrayList<ItemEvent> items;
    TextView text;
    TextView time;
    ViewHolder holder;
    private ImageLoader imageLoader;
 
    public AdapterItemEvent(Activity activity, ArrayList<ItemEvent> items) {
        this.activity = activity;
        this.items = items;
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
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
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolder();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemeventslists, null);
            holder.picture = (NetworkImageView) v.findViewById(R.id.Eventpicture);
            holder.txtTitle = (TextView) v.findViewById(R.id.txtEventTitle);
            holder.txtClub = (TextView) v.findViewById(R.id.txtEventClub);
            holder.txtDate = (TextView) v.findViewById(R.id.txtEventDate);
            holder.txtText = (TextView) v.findViewById(R.id.txtEventText);
            holder.txtTime = (TextView) v.findViewById(R.id.txtEventTime);
            v.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
 
        // Creamos un objeto ItemEvent
        ItemEvent dir = items.get(position);
        //Rellenamos la picturegrafía
        holder.picture.setImageUrl(dir.getPicture(), imageLoader);
        //TODO Cambiar imageview por volleyimageview 
        //picture.setImageDrawable(dir.getPicture());
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
    
    public void onClickedOnce(ViewHolder holder){
    }
    
    static class ViewHolder {
		public NetworkImageView picture;
		public TextView txtTitle;
		public TextView txtClub;
		public TextView txtDate;
		public TextView txtText;
		public TextView txtTime;
	}

	

}
