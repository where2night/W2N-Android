package es.where2night.data;


public class ItemSearch extends Item{
	
	private String name;
	private String picture;
	private int type;
	
	
	public ItemSearch (String picture, String name, long id, int type){
		this.picture = picture;
		this.name = name;
		this.id = id;
		this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
