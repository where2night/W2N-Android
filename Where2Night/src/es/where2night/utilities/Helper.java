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
	
	public static String getPassChangeUrl(){
		return BASE_API_URL + "update/updatePass.php"; 
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
	
	public static String getFollowedLocalsUrl(){
		return BASE_API_URL + "actions/myFavLocals.php";
	}
	
	public static String getAllDjsUrl(){
		return BASE_API_URL + "read/djs.php";
	}
	
	public static String getDefaultProfilePictureUrl(){
		return "http://www.where2night.es/images/profile.jpg";
	}
	
	public static String getDefaultPubPictureUrl(){
		return "http://www.where2night.es/profilesImages/pollo.jpg";
	}
	
	public static String getProfilePictureUrl(){
		return "http://www.where2night.es/profilesImages/";
	}
	
	public static String getFollowUrl(){
		return BASE_API_URL + "actions/follow.php";
	}
	
	public static String getGoToLocalUrl(){
		return BASE_API_URL + "actions/goToPub.php";
	}
	
	public static String getGoToEventUrl(){
		return BASE_API_URL + "actions/goToEvent.php";
	}
	
	public static String getMyEventsUrl(){
		return BASE_API_URL + "actions/myEvents.php";
	}
	
	public static String getNewsUrl(){
		return BASE_API_URL + "read/news.php";
	}
	public static String getSetModeUrl(){
		return BASE_API_URL + "update/mode.php";
	}
	
	public static String getSetStatusUrl(){
		return BASE_API_URL + "update/status.php";
	}
	
	public static String getPartierListUrl(){
		return BASE_API_URL + "read/partiers.php";
	}
	
	public static String getMyFriendListUrl(){
		return BASE_API_URL + "read/myFriends.php";
	}
	
	public static String getFriendshipPetUrl(){
		return BASE_API_URL + "read/petFriendship.php";
	}
	public static String getNewsFriendUrl() {
		return BASE_API_URL + "read/newsUser.php";
	}
	
	public static String getFriendshipResponseUrl(){
		return BASE_API_URL + "actions/followFriend.php";
	}
	public static String getSongsUrl() {
		return BASE_API_URL + "read/playList.php";
	}
	public static String getVoteSongUrl() {
		return BASE_API_URL + "actions/voteTrack.php";
	}
	
	public static String getPhotosUrl() {
		return BASE_API_URL + "actions/photoLocal.php";
	}
	
	public static String getDiscountListUrl(){
		return BASE_API_URL + "read/lists.php";
	}
	public static String joinDiscountListUrl(){
		return BASE_API_URL + "actions/joinList.php";
	}
	
	public static String getStatisticsUrl() {
		return BASE_API_URL + "actions/statisticsPub.php";
	}
	
	public static String getAllUsersUrl() {
		return BASE_API_URL + "read/allusers.php";
	}
	public static String getFriendsMessagesUrl() {
		return BASE_API_URL + "read/messagesSorted.php";
	}
	public static String getMessage() {
		return BASE_API_URL + "read/messagesFriend.php";
	}
	
	public static String getSendMessageUrl() {
		return BASE_API_URL + "actions/sendMessage.php";
	}
	
	public static String getWereUserGoesTodayUrl() {
		return BASE_API_URL + "read/eventsTodayUser.php";
	}
	public static String getCheckInUrl() {
		return BASE_API_URL + "actions/checkIn.php";
	}
	
	
	
}
