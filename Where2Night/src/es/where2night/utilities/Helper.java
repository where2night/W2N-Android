package es.where2night.utilities;

public class Helper {
	public final static String BASE_API_URL = "http://www.develop.where2night.es/";
	
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
		return BASE_API_URL + "register/user.php"; 
	}
	
	public static String getProfileUrl(){
		return BASE_API_URL + "update/user.php"; 
	}
	
	public static String getLocalUrl(){
		return BASE_API_URL + "update/local.php";
	}
	
	public static String getDJUrl(){
		return BASE_API_URL + "update/dj.php";
	}
	
	public static String getEventsUrl(){
		return BASE_API_URL + "read/events.php";
	}
	
	public static String getAllLocalsUrl(){
		return BASE_API_URL + "read/locals.php";
	}
	
	public static String getAllDjsUrl(){
		return BASE_API_URL + "read/djs.php";
	}
	
	public static String getDefaultProfilePictureUrl(){
		return "http://www.where2night.es/images/profile.jpg";
	}
	
	public static String getProfilePictureUrl(){
		return "http://www.where2night.es/profilesImages/";
	}
	
	public static String getFollowUrl(){
		return BASE_API_URL + "actions/follow.php";
	}
	
	
}
