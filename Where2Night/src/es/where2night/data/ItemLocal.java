package es.where2night.data;

import android.graphics.drawable.Drawable;

public class ItemLocal{
	
	private String name;
	private Drawable picture;
	private long id;
	
	
	public ItemLocal (Drawable picture, String name, long id){
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
	public Drawable getPicture() {
		return picture;
	}
	public void setPicture(Drawable picture) {
		this.picture = picture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
