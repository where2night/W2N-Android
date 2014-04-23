package es.where2night.data;

public class ItemFriendState extends Item{
	
	protected String picture;
	protected String name;
	protected String state;
	
	public ItemFriendState(String picture, String name, String state) {
		super();
		this.picture = picture;
		this.name = name;
		this.state = state;
	}
	
	public ItemFriendState(String picture, String name, String state, long id) {
		super();
		this.picture = picture;
		this.name = name;
		this.state = state;
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

	public String getState() {
		return state;
	}

	public void setMode(String state) {
		this.state = state;
	}
	
}
