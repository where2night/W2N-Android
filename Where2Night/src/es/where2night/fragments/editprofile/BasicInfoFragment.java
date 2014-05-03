package es.where2night.fragments.editprofile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.activities.EditProfileActivity;
import es.where2night.activities.LocalViewActivity;
import es.where2night.adapters.AdapterItemLocal;
import es.where2night.data.ItemLocalAndDJ;
import es.where2night.data.LocalListData;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class BasicInfoFragment  extends Fragment {
	private static int RESULT_LOAD_IMAGE = 1;
	
	private NetworkImageView imgEditProfile;
	private ImageLoader imageLoader;
	private EditText etEditName;
	private EditText etEditSurname;
	private EditText etEditDate;
	private RadioButton rdEditFemale;
	private RadioButton rdEditMale;
	private String picturePath;
	private String pictureUrl;	
	String encodedImage = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_editprofile_basicinfo, container, false);
		
		imgEditProfile = (NetworkImageView) view.findViewById(R.id.imgEditProfile);
		etEditName = (EditText)  view.findViewById(R.id.etEditName);
		etEditSurname = (EditText)  view.findViewById(R.id.etEditSurname);
		etEditDate = (EditText)  view.findViewById(R.id.etEditDate);
		rdEditFemale = (RadioButton)  view.findViewById(R.id.rdEditFemale);
		rdEditMale = (RadioButton)  view.findViewById(R.id.rdEditMale);
		
	
		imgEditProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		 fillData();
		return view;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            imgEditProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(picturePath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG,40,baos); 


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

                        //generate base64 string of image

            encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
            
        }
     }
	
	private void fillData() {
		imageLoader = new ImageLoader(EditProfileActivity.requestQueue, new BitmapLRUCache());
	}

	public void setData(JSONObject respuesta) {
		try {
			etEditName.setText(respuesta.getString("name"));
			etEditSurname.setText(respuesta.getString("surnames"));
	        String[] date = respuesta.getString("birthdate").split("/");
	        etEditDate.setText(date[2] + "/" + date[1] + "/" + date[0]);
	        if (respuesta.getString("gender").equals("male")){
            	rdEditMale.setChecked(true);
            }else{
            	rdEditFemale.setChecked(true);
            }
            
            pictureUrl = respuesta.getString("picture");
            if (pictureUrl.equals("") || pictureUrl.contains("face"))
    			pictureUrl = Helper.getDefaultProfilePictureUrl();
            
            imgEditProfile.setImageUrl(pictureUrl, imageLoader);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

	public String[] getData() {
		String[] data = new String[6];
		
		if (encodedImage.equals("")){
			data[0] = pictureUrl;
			data[1] = "false";
		}else{
    		data[0] = encodedImage;
    		data[1] = "true";
    	}
    	data[2] =  etEditName.getText().toString();
    	data[3] = etEditSurname.getText().toString();
    	String[] date = etEditDate.getText().toString().split("/");
    	data[4] = date[2] + "/" + date[1] + "/" + date[0];
    	
    	if (rdEditFemale.isChecked()){
    		data[5] = "female";
		}else if(rdEditMale.isChecked()){
			data[5] = "male";
		}
    	
		return data;
		
	}

}
