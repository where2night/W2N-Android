package com.where2night.data;

import android.graphics.drawable.Drawable;

public class ItemFriend {
	protected Drawable picture;
	protected String name;
	protected String ubication;
	protected long id;
	

	public ItemFriend (Drawable picture, String name, String ubication){

	super();
	this.picture = picture;
	this.name = name;
	this.ubication = ubication;
	}

public ItemFriend (Drawable picture, String name, String ubication, String date, long id){

	super();
	this.picture = picture;
	this.name = name;
	this.ubication = ubication;
	this.id = id;
	}

public Drawable getPicture() {
	return picture;
}

public void setPicture(Drawable picture) {
	this.picture = picture;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getUbication() {
	return ubication;
}

public void setUbication(String ubication) {
	this.ubication = ubication;
}

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

}