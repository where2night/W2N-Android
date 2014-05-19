package es.where2night.fragments.editprofile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.where2night.R;

public class PasswordChangeFragment  extends Fragment {
	
	private EditText etOldPassword;
	private EditText etNewPassword;
	private EditText etNewPasswordRepeat;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile_changepassword, container, false);
		
		etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
		etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
		etNewPasswordRepeat = (EditText) view.findViewById(R.id.etNewPasswordRepeat);
		
		return view;
	}
	
	public String[] getData() {
		boolean show = false;
		String[] data = new String[3];
		
		data[0] =  etOldPassword.getText().toString();
		data[1] =  etNewPassword.getText().toString();
		data[2] =  etNewPasswordRepeat.getText().toString();
		
		String msg = "";
		if(data[0].equals("") && !data[1].equals("") && !data[2].equals("")){
			msg = "Por favor introduce la contraseña antigua";
			show = true;
		} else if(!data[0].equals("") && data[1].equals("")){
			msg = "Por favor introduce la nueva contraseña";
			show = true;
		}else if(!data[0].equals("") && data[2].equals("")){
			msg = "Por favor repite la nueva contraseña";
			show = true;
		}else if (!data[0].equals("") && !data[1].equals(data[2])){
			msg = "La contraseña no coincide";
			show = true;
		}else if (!data[0].equals("") && data[1].length() < 6){
			msg = "La nueva contraseña debe tener al menos 6 caracteres";
			show = true;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(msg)
		        .setTitle("Cambio de contraseña")
		        .setCancelable(false)
		        .setNeutralButton("Aceptar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                });
		if (show){
			AlertDialog alert = builder.create();
			alert.show();			
			return null;
		}
		
		return data;
	}

}
