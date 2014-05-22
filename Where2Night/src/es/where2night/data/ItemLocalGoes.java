package es.where2night.data;

public class ItemLocalGoes extends Item{
	
	protected String nameLocal;
	protected String picture;
	protected String nameFriend;
	protected String date;
	
	public ItemLocalGoes(String nameLocal, String picture, String nameFriend, String date) {
		super();
		this.nameLocal = nameLocal;
		this.picture = picture;
		this.nameFriend = nameFriend;
		this.date = date;
	}
	
	public ItemLocalGoes(String nameLocal, String picture, String nameFriend, String date, long id) {
		super();
		this.nameLocal = nameLocal;
		this.picture = picture;
		this.nameFriend = nameFriend;
		this.date = date;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
}
