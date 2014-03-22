package es.where2night.utilities;

public class Helper {
	public final static String BASE_API_URL = "http://www.api.where2night.es/";
	
	public static String getLoginUrl(){
		return BASE_API_URL + "login/login.php"; 
	}
	public static String getLoginFBUrl(){
		return BASE_API_URL + "login/loginfb.php"; 
	}
	public static String getLoginGPUrl(){
		return BASE_API_URL + "login/logingp.php"; 
	}
	public static String getRegisterUrl(){
		return BASE_API_URL + "register/partier.php"; 
	}
	public static String getProfileUrl(){
		return BASE_API_URL + "update/partier.php"; 
	}
}
