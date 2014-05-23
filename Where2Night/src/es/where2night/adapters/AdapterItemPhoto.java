package es.where2night.adapters;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.data.ItemPhoto;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.Helper;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
public class AdapterItemPhoto extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<ItemPhoto> items;
    private ImageLoader imageLoader;
    
    public AdapterItemPhoto(Activity activity, ArrayList<ItemPhoto> items) {
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
		return ((ItemPhoto) getItem(position)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Generamos una convertView por motivos de eficiencia
        View v = convertView;
        ViewHolderPhoto holder;
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderPhoto();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemphoto, null);
            holder.photo = (NetworkImageView) v.findViewById(R.id.LocalPhoto);
            v.setTag(holder);
        } else {
        	holder = (ViewHolderPhoto) convertView.getTag();
        }
        
        // Creamos un objeto ItemEvent
        ItemPhoto dir = items.get(position);
        //Rellenamos la picturegrafía
        if (!dir.getPhoto().equals("") && !dir.getPhoto().equals("null")){
        	holder.photo.setImageUrl(dir.getPhoto(), imageLoader); //FIXME
        }else{
        	holder.photo.setImageUrl(Helper.getDefaultProfilePictureUrl(), imageLoader); //FIXME
        }
 
        // Retornamos la vista
        return v;
	}
	
	static class ViewHolderPhoto {
		public NetworkImageView photo;
		public long id;
	}

}
