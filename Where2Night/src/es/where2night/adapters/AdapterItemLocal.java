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

import es.where2night.data.ItemLocalAndDJ;
import es.where2night.utilities.BitmapLRUCache;

public class AdapterItemLocal extends BaseAdapter{
 
    protected Activity activity;
    protected ArrayList<ItemLocalAndDJ> items;
    private ViewHolderLocal holder;
    private ImageLoader imageLoader;
    RequestQueue requestQueue;
 
    public AdapterItemLocal(Activity activity, ArrayList<ItemLocalAndDJ> items) {
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
    	return ((ItemLocalAndDJ) getItem(position)).getId();
    }

    
    
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		 // Generamos una convertView por motivos de eficiencia
        View v = convertView;
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderLocal();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlocallist, null);
            holder.picture = (NetworkImageView) v.findViewById(R.id.LocalPicture);
            holder.txtname = (TextView) v.findViewById(R.id.txtLocalName);
            v.setTag(holder);
        } else {
        	holder = (ViewHolderLocal) convertView.getTag();
        }
 
        // Creamos un objeto ItemEvent
        ItemLocalAndDJ dir = items.get(position);
        //Rellenamos la picturegrafía
        if (!dir.getPicture().equals("") && !dir.getPicture().equals("null")){
        	holder.picture.setImageUrl(dir.getPicture(), imageLoader); //FIXME
        }
        //Rellenamos el name
        holder.txtname.setText(dir.getName());
 
        // Retornamos la vista
        return v;
	}
	
	static class ViewHolderLocal {
		public NetworkImageView picture;
		public TextView txtname;
		public long id;
	}

}
