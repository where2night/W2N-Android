package com.where2night.utilities;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

	String sqlCreateUserLogin = "CREATE TABLE UserLogin (token TEXT PRIMARY KEY, type INT)";
	public DBManager(Context context) {
		super(context, "Where2Night", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreateUserLogin);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS UserLogin");
		db.execSQL(sqlCreateUserLogin);
	}

}
