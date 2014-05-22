package es.where2night.data;

public class ItemLocalCheck extends Item{
	
	protected String nameLocal;
	protected String picture;
	protected String nameFriend;
	
	public ItemLocalCheck(String nameLocal, String picture, String nameFriend) {
		super();
		this.nameLocal = nameLocal;
		this.picture = picture;
		this.nameFriend = nameFriend;
	}
	
	public ItemLocalCheck(String nameLocal, String picture, String nameFriend, long id) {
		super();
		this.nameLocal = nameLocal;
		this.picture = picture;
		this.nameFriend = nameFriend;
		this.id = id;
	}

	public String getNameLocal() {
		return nameLocal;
	}

	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getNameFriend() {
		return nameFriend;
	}

	public void setNameFriend(String nameFriend) {
		this.nameFriend = nameFriend;
	}
}
