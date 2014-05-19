package es.where2night.fragments.editprofile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.where2night.R;

public class DetailedInfoFragment  extends Fragment {
	
	private Spinner etEditMusic;
	private Spinner etEditDrink;
	private Spinner etEditCivilState;
	private EditText etEditCity;
	private EditText etEditAbout;
	private EditText etEditFB;
	private EditText etEditTW;
	private EditText etEditIG;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile_detailedinfo, container, false);
		
		etEditMusic = (Spinner) view.findViewById(R.id.spinnerMusica);
		etEditDrink = (Spinner) view.findViewById(R.id.spinnerBebida);
		etEditCivilState = (Spinner) view.findViewById(R.id.spinnerEstadoCivil);
		etEditCity = (EditText) view.findViewById(R.id.etEditCity);
		etEditAbout = (EditText) view.findViewById(R.id.etEditAbout);
		etEditFB = (EditText) view.findViewById(R.id.etEditFB);
		etEditTW = (EditText) view.findViewById(R.id.etEditTW);
		etEditIG = (EditText) view.findViewById(R.id.etEditIG);
		etEditAbout = (EditText) view.findViewById(R.id.etEditIG);
		
		
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
			 Log.e("setData", respuesta.toString());
			 String vacio = "";
			 if(!respuesta.getString("music").equals(vacio))
				 etEditMusic.setSelection(Integer.parseInt(respuesta.getString("music")));
			 if(!respuesta.getString("drink").equals(vacio))
				 etEditDrink.setSelection(Integer.parseInt(respuesta.getString("drink")));
			 if(!respuesta.getString("civil_state").equals(vacio))
				 etEditCivilState.setSelection(Integer.parseInt(respuesta.getString("civil_state")));
	         etEditCity.setText(respuesta.getString("city"));
	         etEditAbout.setText(respuesta.getString("about"));
	         etEditFB.setText(respuesta.getString("facebook"));
	         etEditTW.setText(respuesta.getString("twitter"));
	         etEditIG.setText(respuesta.getString("instagram"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

	public String[] getData() {
		String[] data = new String[8];
		
		data[0] =  etEditCity.getText().toString();
		data[1] = ((Integer)etEditMusic.getSelectedItemPosition()).toString();
		data[2] = ((Integer)etEditCivilState.getSelectedItemPosition()).toString();
		data[3] = ((Integer)etEditDrink.getSelectedItemPosition()).toString();
		data[4] = etEditAbout.getText().toString();
		data[5] = etEditFB.getText().toString();
		data[6] = etEditTW.getText().toString();
		data[7] = etEditIG.getText().toString();
    	
		return data;
		
	}

}
