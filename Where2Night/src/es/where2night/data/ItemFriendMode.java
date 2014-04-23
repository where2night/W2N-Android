package es.where2night.data;

public class ItemFriendMode extends Item{
	
	protected String picture;
	protected String name;
	protected String mode;
	
	public ItemFriendMode(String picture, String name, String mode) {
		super();
		this.picture = picture;
		this.name = name;
		this.mode = mode;
	}
	
	public ItemFriendMode(String picture, String name, String mode, long id) {
		super();
		this.picture = picture;
		this.name = name;
		this.mode = mode;
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
