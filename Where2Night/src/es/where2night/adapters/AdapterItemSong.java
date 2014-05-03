package es.where2night.adapters;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.adapters.AdapterItemFriendList.ViewHolderFriend;
import es.where2night.data.ItemDiscountList;
import es.where2night.data.ItemFriend;
import es.where2night.data.ItemSong;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;
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

public class AdapterItemSong extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<ItemSong> items;
    ViewHolderSong holder;
    RequestQueue requestQueue;
	
	public AdapterItemSong(Activity activity, ArrayList<ItemSong> items) {
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
        final ItemSong dir = items.get(position); 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
        	holder = new ViewHolderSong();
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemsong, null);
            holder.songName = (TextView) v.findViewById(R.id.txtSongName);
            holder.artistName = (TextView) v.findViewById(R.id.txtArtistName);
            holder.votes = (TextView) v.findViewById(R.id.txtVotes);
            holder.btnVote = (Button) v.findViewById(R.id.btnVote);
            v.setTag(holder);
        }
        else{
        	holder = (ViewHolderSong) convertView.getTag();
        }
        
        holder.btnVote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long songId = dir.getId();
				if (v.isSelected()){
					holder.btnVote.setText("+1");
		        	holder.btnVote.setSelected(false);
		        	dir.setVoted(false);
		        	
					voteSong(songId,true);
				}else{
					holder.btnVote.setText(activity.getResources().getString(R.string.Signed));
		        	holder.btnVote.setSelected(true);
		        	dir.setVoted(true);
					voteSong(songId,false);
				}
				notifyDataSetChanged(); // tells the adapter that the data changed
			}
		});
        
        if (dir.isVoted()){
        	holder.btnVote.setText("-1");
        	holder.btnVote.setSelected(true);
        }else {
        	holder.btnVote.setText("+1");
        	holder.btnVote.setSelected(false);
        }
        if (dir.isCheckIn()){
        	holder.btnVote.setVisibility(View.VISIBLE);
        }else{
        	holder.btnVote.setVisibility(View.GONE);
        }
        
 
        //Rellenamos el nombre de la cancion
        holder.songName.setText(dir.getSongName());
        //Rellenamos nombre del artista
        holder.artistName.setText(dir.getArtist());
        //Rellenamos el numero de votos
        holder.votes.setText("Votos: " + dir.getVotes());
        // Retornamos la vista
        return v;
	}
	
	static class ViewHolderSong {
		public TextView songName;
		public TextView artistName;
		public TextView votes;
		public Button btnVote;
	}
	
	private void voteSong(long songId,boolean notVote){
		final DataManager dm = new DataManager(activity.getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = Helper.getVoteSongUrl() + "/" + cred[0] + "/" + cred[1] + "/" + songId;
		Log.e("AdapterURl", url); 
		 
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
		
		if(notVote){
			request = new StringRequest(Request.Method.DELETE, url, succeedListener, errorListener); 
		}
		else{
			request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 

		}
		requestQueue.add(request);
	}

}
