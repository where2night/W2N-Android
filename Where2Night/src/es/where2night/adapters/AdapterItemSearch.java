package es.where2night.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.data.ItemFriend;
import es.where2night.data.ItemSearch;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.Helper;
 
public class AdapterItemSearch extends BaseAdapter implements OnClickListener{
 
    protected Activity activity;
    protected ArrayList<ItemSearch> items;
    ViewHolderFriend holder;
    private ImageLoader imageLoader;
    RequestQueue requestQueue;
 
    public AdapterItemSearch(Activity activity, ArrayList<ItemSearch> items) {
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
    	Log.e("ID", String.valueOf(items.get(position).getId()));
        return items.get(position).getId();
        
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;
        // Creamos un objeto ItemFriend
        final ItemSearch dir = items.get(position); 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderFriend();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemfriend, null);
            holder.picture = (NetworkImageView) v.findViewById(R.id.FriendPicture);
            holder.txtName = (TextView) v.findViewById(R.id.txtFriendName);
            v.setTag(holder);
        }
        else {
        	holder = (ViewHolderFriend) convertView.getTag();
        }
 
      //Rellenamos la picturegrafía
        if (!dir.getPicture().equals("")){
        	holder.picture.setImageUrl(dir.getPicture(), imageLoader);
        }else{
        	holder.picture.setImageUrl(Helper.getDefaultProfilePictureUrl(), imageLoader);
        }
        	
        //Rellenamos el name
        holder.txtName.setText(dir.getName());
        return v;
    }
    
    static class ViewHolderFriend {
		public NetworkImageView picture;
		public TextView txtName;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
