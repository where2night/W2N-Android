package es.where2night.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.where2night.R;

import es.where2night.utilities.BitmapLRUCache;
import es.where2night.utilities.DataManager;
import es.where2night.utilities.Helper;

public class EditProfileActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	
	private NetworkImageView imgEditProfile;
	private ImageLoader imageLoader;
	private EditText etEditName;
	private EditText etEditSurname;
	private EditText etEditDate;
	private EditText etEditMusic;
	private EditText etEditDrink;
	private EditText etEditCivilState;
	private EditText etEditCity;
	private EditText edEditAbout;
	private RadioButton rdEditFemale;
	private RadioButton rdEditMale;
	private Button btnEditAccept;
	private Button btnEditCancel;
	private ProgressBar pgEdit;
	
	private String pictureUrl;	
	private String idProfile;
	private String picturePath;
	
	String encodedImage = "";
	
	private RequestQueue requestQueue;
    private JSONObject respuesta = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		getActionBar().setIcon(R.drawable.logo7);
		
		
		
		imgEditProfile = (NetworkImageView) findViewById(R.id.imgEditProfile);
		etEditName = (EditText) findViewById(R.id.etEditName);
		etEditSurname = (EditText) findViewById(R.id.etEditSurname);
		etEditDate = (EditText) findViewById(R.id.etEditDate);
		etEditMusic = (EditText) findViewById(R.id.etEditMusic);
		etEditDrink = (EditText) findViewById(R.id.etEditDrink);
		etEditCivilState = (EditText) findViewById(R.id.etEditCivilState);
		etEditCity = (EditText) findViewById(R.id.etEditCity);
		edEditAbout = (EditText) findViewById(R.id.edEditAbout);
		rdEditFemale = (RadioButton) findViewById(R.id.rdEditFemale);
		rdEditMale = (RadioButton) findViewById(R.id.rdEditMale);
		btnEditAccept = (Button) findViewById(R.id.btnEditAccept);
		btnEditCancel = (Button) findViewById(R.id.btnEditCancel);
		pgEdit = (ProgressBar) findViewById(R.id.pgEdit);
		pgEdit.setVisibility(View.VISIBLE);
		fillData();
		
		btnEditAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pgEdit.setVisibility(View.VISIBLE);
				btnEditAccept.setEnabled(false);
				sendData();
			}
		});
		
		btnEditCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra(MainActivity.PARENT, "0");
				startActivity(i);
				
			}
		});
		
		imgEditProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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

            encodedImage =Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
            
        }
     }
	

	private void fillData() {
		
		
		
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		idProfile = cred[0];
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1];
		
		imageLoader = new ImageLoader(requestQueue, new BitmapLRUCache());
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            try {
	            	pgEdit.setVisibility(View.GONE);
	            	btnEditAccept.setEnabled(true);
		            respuesta = new JSONObject(response);
		            etEditName.setText(respuesta.getString("name"));
		            etEditSurname.setText(respuesta.getString("surnames"));
		            String[] date = respuesta.getString("birthdate").split("/");
		            etEditDate.setText(date[2] + "/" + date[1] + "/" + date[0]);
		            etEditMusic.setText(respuesta.getString("music"));
		            etEditDrink.setText(respuesta.getString("drink"));
		            etEditCivilState.setText(respuesta.getString("civil_state"));
		            etEditCity.setText(respuesta.getString("city"));
		            edEditAbout.setText(respuesta.getString("about"));
		            if (respuesta.getString("gender").equals("male")){
		            	rdEditMale.setChecked(true);
		            }else{
		            	rdEditFemale.setChecked(true);
		            }
		            
		            pictureUrl = respuesta.getString("picture");
		            
		            if (pictureUrl.equals(""))
		    			pictureUrl = Helper.getDefaultProfilePictureUrl();
		            
		            imgEditProfile.setImageUrl(pictureUrl, imageLoader);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEdit.setVisibility(View.GONE);
	        	 btnEditAccept.setEnabled(true);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.GET, url, succeedListener, errorListener); 
		
		requestQueue.add(request);
	}
	
	private void sendData() {
		
		final DataManager dm = new DataManager(getApplicationContext());
		String[] cred = dm.getCred();
		requestQueue = Volley.newRequestQueue(getApplicationContext()); 
		String url = Helper.getProfileUrl() + "/" + cred[0] + "/" + cred[1];
		
		Response.Listener<String> succeedListener = new Response.Listener<String>() 
	    {
	        @Override
	        public void onResponse(String response) {
	            // response
	        	Log.e("Response", response);
	            pgEdit.setVisibility(View.GONE);
				btnEditAccept.setEnabled(true);
				Toast.makeText(getApplicationContext(), "Perfil Guardado", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra(MainActivity.PARENT, "0");
				startActivity(i);
	        }
	    };
	    Response.ErrorListener errorListener = new Response.ErrorListener() 
	    {
	         @Override
	         public void onErrorResponse(VolleyError error) {
	             // error
	        	 pgEdit.setVisibility(View.GONE);
	        	 btnEditAccept.setEnabled(true);
	             Log.e("Error.Response", error.toString());
	       }
	    };
		
		StringRequest request = new StringRequest(Request.Method.POST, url, succeedListener, errorListener) 
		{     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			    	HashMap<String, String> info = new HashMap<String, String>();
			    	
			    	info.put("idProfile", idProfile);
			    	if (encodedImage.equals(""))
			    		info.put("picture", pictureUrl);
			    	else{
			    		info.put("picture", encodedImage);
			    		info.put("uploading", "true");
			    	}
			    	info.put("name", etEditName.getText().toString());
			    	info.put("surnames", etEditSurname.getText().toString());
			    	String[] date = etEditDate.getText().toString().split("/");
			    	info.put("birthdate", date[2] + "/" + date[1] + "/" + date[0]);
			    	info.put("city", etEditCity.getText().toString());
			    	info.put("music", etEditMusic.getText().toString());
			    	info.put("civil_state", etEditCivilState.getText().toString());
			    	info.put("drink", etEditDrink.getText().toString());
			    	info.put("about", edEditAbout.getText().toString());
			    	
			    	String gender = null;
			    	if (rdEditFemale.isChecked()){
						gender = "female";
					}else if(rdEditMale.isChecked()){
						gender = "male";
					}
			    	info.put("gender", gender);
			    	
			    	return info;
			    }
		};
		
		requestQueue.add(request);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}
}
