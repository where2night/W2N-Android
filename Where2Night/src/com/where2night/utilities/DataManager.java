package com.where2night.utilities;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {
	
	DBManager dbm;
	
	public DataManager(Context context){
		dbm = new DBManager(context);
	}
	
	public boolean checkLogin(){		
			SQLiteDatabase db = dbm.getWritableDatabase();
			String[] rows = {"token"};	
			boolean logedin = false;
			Cursor c = db.query("UserLogin",rows,null,null,null,null,null);
			try{
				if (c.moveToFirst()){
					logedin = true;
				}
			} finally {
				 c.close();
				 db.close();
			}
			return logedin;
	}
	
	public HashMap<String,String> getUser(String email){
		HashMap<String, String> data = new HashMap<String, String>();
		SQLiteDatabase db = dbm.getWritableDatabase();
		String[] rows = {"name","surnames","birthdate","gender"};
		Cursor c = db.query("User",rows,null,null,null,null,null);
		try {
			if (c.moveToFirst()) {
				data.put("email",email);
	            data.put("name", c.getString(0));
	            data.put("surnames", c.getString(1));
	            data.put("birthdate", c.getString(2));
	            data.put("gender", c.getString(3));
	        }
		} finally {
			c.close();
	        db.close();
	    }
		return data;
	}
	
	public void setUser(String email, String name, String surnames, String birthday, String gender){
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.execSQL("INSERT INTO User (email,name,surnames,birthdate,gender) " +
            	"VALUES (\'" + email + "\',\'" + name + "\',\'" + surnames + "\',\'" + 
				birthday + "\',\'" + gender + "\')");
		db.close();
	}
	
	
	public void login(String email, String token, int type){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.execSQL("INSERT INTO UserLogin (email,token,type) " +
            	"VALUES (\'" + email + "\',\'" + token + "\'," + type + ")");
		db.close();
	}
	
	public void logout(){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.execSQL("DELETE * FROM UserLogin");
		db.close();
	}
	
	
	
	
	
	

}
