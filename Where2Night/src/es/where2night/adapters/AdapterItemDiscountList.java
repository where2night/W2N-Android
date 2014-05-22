package es.where2night.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.data.ItemDiscountList;
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
		public TextView txtDescription;
		public TextView txtDate;
		public TextView txtHour;
		public TextView txtExpireDate;
		public TextView txtInvited;
		public Spinner spinnerInvited;
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
            holder.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            holder.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            holder.txtDate = (TextView) v.findViewById(R.id.txtDate);
            holder.txtHour = (TextView) v.findViewById(R.id.txtHour);
            holder.txtExpireDate = (TextView) v.findViewById(R.id.txtExpireDate);
            holder.txtInvited = (TextView) v.findViewById(R.id.txtInvited);
            holder.btnSignMe = (Button) v.findViewById(R.id.btnSignMeIn);
            holder.spinnerInvited = (Spinner) v.findViewById(R.id.spinnerInvited);
            
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
    		        R.array.invited_array, android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		holder.spinnerInvited.setAdapter(adapter);
            
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
		        	holder.txtInvited.setVisibility(View.VISIBLE);
		        	holder.spinnerInvited.setVisibility(View.VISIBLE);
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
        	holder.txtInvited.setVisibility(View.GONE);
        	holder.spinnerInvited.setVisibility(View.GONE);
        }else {
        	holder.btnSignMe.setText(activity.getResources().getString(R.string.SignMe));
        	holder.btnSignMe.setSelected(false);
        	holder.txtInvited.setVisibility(View.VISIBLE);
        	holder.spinnerInvited.setVisibility(View.VISIBLE);
        }
 
        holder.txtTitle.setText(dir.getTitle());
        holder.txtDescription.setText(dir.getDescription());
        holder.txtDate.setText(dir.getDate());
        String start = dir.getStart().substring(0, 5);
        String end = dir.getEnd().substring(0, 5);
        holder.txtHour.setText(start + " - " + end);
        holder.txtExpireDate.setText(dir.getExpireDate());
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
	            	if (respuesta.getString("join").equals("true")){
	            		
	            	}else if (respuesta.getString("join").equals("false")){
	            		
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
			request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener){     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	HashMap<String, String> info = new HashMap<String, String>();
			    	info.put("numGuest", ((Integer)holder.spinnerInvited.getSelectedItemPosition()).toString());
			    				    	
			    	return info;
			    }
		};

		}
		requestQueue.add(request);
	}
	
}
