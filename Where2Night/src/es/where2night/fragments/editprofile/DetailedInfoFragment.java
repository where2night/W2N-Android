package es.where2night.fragments.editprofile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
	
	private Spinner etEditMusic;
	private Spinner etEditDrink;
	private Spinner etEditCivilState;
	private EditText etEditCity;
	private EditText edEditAbout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile_detailedinfo, container, false);
		
		etEditMusic = (Spinner) view.findViewById(R.id.spinnerMusica);
		etEditDrink = (Spinner) view.findViewById(R.id.spinnerBebida);
		etEditCivilState = (Spinner) view.findViewById(R.id.spinnerEstadoCivil);
		etEditCity = (EditText) view.findViewById(R.id.etEditCity);
		edEditAbout = (EditText) view.findViewById(R.id.edEditAbout);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.musica_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		etEditMusic.setAdapter(adapter);
		
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
		        R.array.bebida_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		etEditDrink.setAdapter(adapter2);
		
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(),
		        R.array.estado_civil_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		etEditCivilState.setAdapter(adapter3);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Te recordamos que el estado civil es privado, y nadie podrá verlo. Solamente es para las estadísticas de los locales.")
		        .setTitle("Estado 'civil'")
		        .setCancelable(false)
		        .setNeutralButton("Aceptar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
		
		return view;
	}
	
	public void fill(){
		
		
	}
	
	private void fillData() {
	}

	public void setData(JSONObject respuesta) {

		 try {
			 String vacio = "";
			 if(!respuesta.getString("music").equals(vacio))
				 etEditMusic.setSelection(Integer.parseInt(respuesta.getString("music")));
			 if(!respuesta.getString("drink").equals(vacio))
				 etEditDrink.setSelection(Integer.parseInt(respuesta.getString("drink")));
			 if(!respuesta.getString("civl_state").equals(vacio))
				 etEditCivilState.setSelection(Integer.parseInt(respuesta.getString("civil_state")));
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
		data[1] = ((Integer)etEditMusic.getSelectedItemPosition()).toString();
		data[2] = ((Integer)etEditCivilState.getSelectedItemPosition()).toString();
		data[3] = ((Integer)etEditDrink.getSelectedItemPosition()).toString();
		data[4] = edEditAbout.getText().toString();
    	
		return data;
		
	}

}
