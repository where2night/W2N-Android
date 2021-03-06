package es.where2night.fragments.editprofile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.where2night.R;

import es.where2night.activities.EditProfileActivity;
import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.Helper;

public class BasicInfoFragment  extends Fragment {
	private static final int RESULT_CROP = 3;

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
	
	private void crop(Uri photoUri) {
	    Intent intent = new Intent("com.android.camera.action.CROP");
	    intent.setData(photoUri);
	    intent.putExtra("outputX", 200);
	    intent.putExtra("outputY", 200);
	    intent.putExtra("aspectX", 1);
	    intent.putExtra("aspectY", 1);
	    intent.putExtra("scale", true);
	    intent.putExtra("return-data", true);
	    startActivityForResult(intent, RESULT_CROP);
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null){
            	crop(selectedImage);
            }
        } else if (requestCode == RESULT_CROP && resultCode == Activity.RESULT_OK && data != null){
        	
        	Bundle extras = data.getExtras();
        	
        	if (extras != null) {
                Bitmap bm = extras.getParcelable("data");
        	
        

            imgEditProfile.setImageBitmap(bm);
                
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG,40,baos); 


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

                        //generate base64 string of image

            encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
        	}
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
            if (pictureUrl.equals(""))
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
