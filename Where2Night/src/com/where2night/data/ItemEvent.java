package com.where2night.data;

import android.graphics.drawable.Drawable;

public class ItemEvent {
	protected Drawable picture;
	protected String name;
	protected String club;
	protected String date;
	protected long id;
	

public ItemEvent(Drawable picture, String name, String club, String date){

		super();
		this.picture = picture;
		this.name = name;
		this.club = club;
		this.date = date;
		}

public ItemEvent(Drawable picture, String name, String club, String date, long id){

	super();
	this.picture = picture;
	this.name = name;
	this.club = club;
	this.date = date;
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

public String getClub() {
	return club;
}

public void setClub(String club) {
	this.club = club;
}

public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

}
