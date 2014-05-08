package es.where2night.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.adapters.AdapterItemEvent.ViewHolderEvent;
import es.where2night.data.ItemDiscountList;
import es.where2night.data.ItemEvent;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class AdapterItemDiscountList extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<ItemDiscountList> items;
    ViewHolderItemDiscount holder;
    RequestQueue requestQueue;
 
    public AdapterItemDiscountList(Activity activity, ArrayList<ItemDiscountList> items) {
    	requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        this.activity = activity;
        this.items = items;
      }
 
    @Override
    public int getCount() {
        return items.size();
    }
 
    @Override
    public Object getItem(int position) {
    	return items.get(position);
    }
 
    static class ViewHolderItemDiscount {
		public TextView txtTitle;
		public TextView txtDate;
		public Button btnSignMe;
	}
   
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;
        final ItemDiscountList dir = items.get(position);
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderItemDiscount();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemdiscountlist, null);
            holder.txtDate = (TextView) v.findViewById(R.id.txtDay);
            holder.txtTitle = (TextView) v.findViewById(R.id.txtExpireDate);
            holder.btnSignMe = (Button) v.findViewById(R.id.btnSignMeIn);
            
            v.setTag(holder);
        }else {
        	holder = (ViewHolderItemDiscount) convertView.getTag();
        }
        
        holder.btnSignMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long listId = dir.getId();
				if (v.isSelected()){
					holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
		        	holder.btnSignMe.setSelected(false);
		        	dir.setGoes(false);
		        	
					singIntoList(listId,true);
				}else{
					holder.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
		        	holder.btnSignMe.setSelected(true);
		        	dir.setGoes(true);
		        	singIntoList(listId,false);
				}
				notifyDataSetChanged(); // tells the adapter that the data changed
			}
		});
        
        if (dir.isGoes()){
        	holder.btnSignMe.setText(activity.getResources().getString(R.string.Signed));
        	holder.btnSignMe.setSelected(true);
        }else {
        	holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
        	holder.btnSignMe.setSelected(false);
        }
 
        // Creamos un objeto ItemEvent
        //Rellenamos el name
        holder.txtDate.setText(dir.getDay());
        //Rellenamos el club
        holder.txtTitle.setText(dir.getExpireDate());
        // Retornamos la vista
        return v;
    }

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
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
	
}
