package es.where2night.data;

import android.graphics.drawable.Drawable;

public class ItemEvent {
	private Drawable picture;
	private String name;
	private String club;
	private String date;
	private long id;
	

public ItemEvent(Drawable picture, String name, String club, String date){

		this.picture = picture;
		this.name = name;
		this.club = club;
		this.date = date;
}

public ItemEvent(Drawable picture, String name, String club, String date, long id){

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
