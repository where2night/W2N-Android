package es.where2night.fragments.editprofile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemLocal;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.data.LocalListData;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class DetailedInfoFragment  extends Fragment {
	
	private EditText etEditMusic;
	private EditText etEditDrink;
	private EditText etEditCivilState;
	private EditText etEditCity;
	private EditText edEditAbout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile_detailedinfo, container, false);
		
		etEditMusic = (EditText) view.findViewById(R.id.etEditMusic);
		etEditDrink = (EditText) view.findViewById(R.id.etEditDrink);
		etEditCivilState = (EditText) view.findViewById(R.id.etEditCivilState);
		etEditCity = (EditText) view.findViewById(R.id.etEditCity);
		edEditAbout = (EditText) view.findViewById(R.id.edEditAbout);
		
		return view;
	}
	
	public void fill(){
		
		
	}
	
	private void fillData() {
	}

	public void setData(JSONObject respuesta) {

		 try {
			 etEditMusic.setText(respuesta.getString("music"));
			 etEditDrink.setText(respuesta.getString("drink"));
	         etEditCivilState.setText(respuesta.getString("civil_state"));
	         etEditCity.setText(respuesta.getString("city"));
	         edEditAbout.setText(respuesta.getString("about"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

	public String[] getData() {
		String[] data = new String[5];
		
		data[0] =  etEditCity.getText().toString();
		data[1] = etEditMusic.getText().toString();
		data[2] = etEditCivilState.getText().toString();
		data[3] = etEditDrink.getText().toString();
		data[4] = edEditAbout.getText().toString();
    	
		return data;
		
	}

}
