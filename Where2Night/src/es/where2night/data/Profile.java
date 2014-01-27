package es.where2night.data;


public class Profile {
	
	
	private String name;
	private String password;
	private String surnames;
	private String birthday;
	private String gender;
	
	public Profile (String name,String password, String surnames, String birthday, String gender){
		this.name = name;
		this.password = password;
		this.surnames = surnames;
		this.birthday = birthday;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurnames() {
		return surnames;
	}

	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
	
	
	
	

	

}
