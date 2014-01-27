package com.where2night.utilities;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataManager {
	
	DBManager dbm;
	
	public DataManager(Context context){
		dbm = new DBManager(context);
	}
	
	public boolean checkLogin(){		
			SQLiteDatabase db = dbm.getWritableDatabase();
			String[] rows = {"email"};	
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
		String[] rows = {"name","picture","surnames","birthdate","gender"};
		Cursor c = db.query("User",rows,null,null,null,null,null);
		try {
			if (c.moveToFirst()) {
				data.put("email",email);
	            data.put("name", c.getString(0));
	            data.put("picture",c.getString(1));
	            data.put("surnames", c.getString(2));
	            data.put("birthdate", c.getString(3));
	            data.put("gender", c.getString(4));
	        }
		} finally {
			c.close();
	        db.close();
	    }
		return data;
	}
	
	public HashMap<String,String> getEmail(){
		HashMap<String, String> data = new HashMap<String, String>();
		SQLiteDatabase db = dbm.getWritableDatabase();
		String[] rows = {"email"};
		Cursor c = db.query("UserLogin",rows,null,null,null,null,null);
		try {
			if (c.moveToFirst()) {
				data.put("email",c.getString(0));
	        }
		} finally {
			c.close();
	        db.close();
	    }
		return data;
	}
	
	public void setUser(String email,String picture, String name, String surnames, String birthday, String gender){
		SQLiteDatabase db = dbm.getWritableDatabase();
		try{
			db.execSQL("INSERT INTO User (email,picture,name,surnames,birthdate,gender) " +
	            	"VALUES (\'" + email + "\',\'" + picture + "\',\'" + name + "\',\'" + surnames + "\',\'" + 
					birthday + "\',\'" + gender + "\')");
		}catch (Exception e){}
		db.close();
	}
	
	
	public void login(String email,String idProfile, String token, int type){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		try{
			db.execSQL("INSERT INTO UserLogin (email,idProfile,token,type) " +
	            	"VALUES (\'" + email + "\',\'" + idProfile + "\',\'" + token + "\'," + type + ")");
		}catch (Exception e){}
		db.close();
	}
	
	public void logout(){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.delete("UserLogin",null,null);
		db.delete("User",null,null);
		Log.e("db","Borrado");
		db.close();
	}
	
	
	
	
	
	

}
