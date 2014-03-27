package es.where2night.data;


public class ItemLocalAndDJ{
	
	private String name;
	private String picture;
	private long id;
	
	
	public ItemLocalAndDJ (String picture, String name, long id){
		this.picture = picture;
		this.name = name;
		this.id = id;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
