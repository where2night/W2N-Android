package com.where2night.utilities;

import java.util.ArrayList;
import java.util.Hashtable;

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
	
	public void login(String token, int type){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.execSQL("INSERT INTO UserLogin (token, type) " +
            	"VALUES (\'" + token + "\'," + type + ")");
		db.close();
	}
	
	public void logout(){		
		SQLiteDatabase db = dbm.getWritableDatabase();
		db.execSQL("DELETE * FROM UserLogin");
		db.close();
	}
	
	
	
	
	
	

}
