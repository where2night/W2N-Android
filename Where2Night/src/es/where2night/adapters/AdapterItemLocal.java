package es.where2night.adapters;

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

import es.where2night.data.ItemLocal;

public class AdapterItemLocal extends BaseAdapter{
 
    protected Activity activity;
    protected ArrayList<ItemLocal> items;
 
    public AdapterItemLocal(Activity activity, ArrayList<ItemLocal> items) {
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
    	return ((ItemLocal) getItem(position)).getId();
    }

    
    
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		 // Generamos una convertView por motivos de eficiencia
        View v = convertView;
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlocallist, null);
        }
 
        // Creamos un objeto ItemEvent
        ItemLocal dir = items.get(position);
        //Rellenamos la picturegrafía
        ImageView picture = (ImageView) v.findViewById(R.id.LocalPicture);
        picture.setImageDrawable(dir.getPicture());
        //Rellenamos el name
        TextView name = (TextView) v.findViewById(R.id.txtLocalName);
        name.setText(dir.getName());
 
        // Retornamos la vista
        return v;
	}

}
