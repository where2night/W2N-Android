package com.prototipo;

public class Helper {
	public final static String INSTAGRAM_API_KEY = "b54ea2b3993840f581401c96f4b34a8d";
	public final static String BASE_API_URL = "https://api.instagram.com/v1/";
	
	public static String getRecentUrl(String tag){
		//return BASE_API_URL + "tags/" + tag + "/media/recent?client_id=" + INSTAGRAM_API_KEY; 
		return BASE_API_URL + "media/popular?client_id=" + INSTAGRAM_API_KEY;
	}
}
