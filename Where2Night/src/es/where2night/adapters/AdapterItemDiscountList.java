package es.where2night.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.where2night.R;

import es.where2night.data.ItemDiscountList;

public class AdapterItemDiscountList extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<ItemDiscountList> items;
 
    public AdapterItemDiscountList(Activity activity, ArrayList<ItemDiscountList> items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemdiscountlist, null);
        }
 
        // Creamos un objeto ItemEvent
        ItemDiscountList dir = items.get(position);
        //Rellenamos el name
        TextView date = (TextView) v.findViewById(R.id.txtDay);
        date.setText(dir.getDay());
        //Rellenamos el club
        TextView expireDate = (TextView) v.findViewById(R.id.txtExpireDate);
        expireDate.setText(dir.getExpireDate());
        // Retornamos la vista
        return v;
    }

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
