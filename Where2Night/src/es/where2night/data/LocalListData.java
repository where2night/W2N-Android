package es.where2night.data;

public class LocalListData {
	
	private long idProfile;
	private String name;
	private String picture;
	private String latitude;
	private String longitude;
	private String address;
	
	public LocalListData(long idProfile, String name, String picture, String latitude, String longitude, String address){
		this.idProfile = idProfile;
		this.name = name;
		this.picture = picture;
		this.setLatitude(latitude);
		this.longitude = longitude;
		this.address = address;
	}

	public long getIdProfile() {
		return idProfile;
	}

	public void setIdProfile(long idProfile) {
		this.idProfile = idProfile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

