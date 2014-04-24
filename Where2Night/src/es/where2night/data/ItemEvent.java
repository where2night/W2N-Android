package es.where2night.data;


public class ItemEvent extends Item{
	private String picture;
	private String name;
	private String title;
	private String text;
	private String date;
	private String start;
	private String close;
	private String idCreator;
	public long id;
	private boolean goes; 
	



public ItemEvent(String picture, String name,String title, String text, String date, String start, String close, String idCreator, long id, boolean goes){

	this.picture = picture;
	this.name = name;
	this.text = text;
	this.title = title;
	this.date = date;
	this.start = start;
	this.close = close;
	this.idCreator = idCreator;
	this.id = id;
	this.goes = goes;
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

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
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

public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}

public String getStart() {
	return start;
}

public void setStart(String start) {
	this.start = start;
}

public String getClose() {
	return close;
}

public void setClose(String close) {
	this.close = close;
}

public String getIdCreator() {
	return idCreator;
}

public void setIdCreator(String idCreator) {
	this.idCreator = idCreator;
}

public boolean isGoes() {
	return goes;
}

public void setGoes(boolean goes) {
	this.goes = goes;
}

}
