package com.where2night.utilities;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

	String sqlCreateUserLogin = "CREATE TABLE UserLogin (email TEXT PRIMARY KEY, token TEXT , type INT)";
	String sqlCreateUser = "CREATE TABLE User (email TEXT PRIMARY KEY, name TEXT, surnames TEXT, birthdate TEXT, gender TEXT)";
	public DBManager(Context context) {
		super(context, "Where2Night", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreateUserLogin);
		db.execSQL(sqlCreateUser);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS UserLogin");
		db.execSQL(sqlCreateUserLogin);
	}

}
