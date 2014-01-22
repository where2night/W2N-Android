package com.where2night.utilities;

public class Helper {
	public final static String BASE_API_URL = "http://where2night.es/";
	
	public static String getLoginUrl(){
		return BASE_API_URL + "login.php"; 
	}
	public static String getLoginFBUrl(){
		return BASE_API_URL + "loginfb.php"; 
	}
	public static String getRegisterUrl(){
		return BASE_API_URL + "registerFiestero.php"; 
	}
	public static String getProfileUrl(){
		return BASE_API_URL + "editprofile.php"; 
	}
}
