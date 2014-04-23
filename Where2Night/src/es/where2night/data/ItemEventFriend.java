package es.where2night.data;

public class ItemEventFriend extends Item{
	private String picture;
	private String nameEvent;
	private String title;
	private String text;
	private String date;
	private String start;
	private String close;
	private String idCreator;
	private String nameFriend;
	
	public ItemEventFriend(String picture, String nameEvent, String title,
			String text, String date, String start, String close,
			String idCreator, String nameFriend) {
		super();
		this.picture = picture;
		this.nameEvent = nameEvent;
		this.title = title;
		this.text = text;
		this.date = date;
		this.start = start;
		this.close = close;
		this.idCreator = idCreator;
		this.nameFriend = nameFriend;
	}
	
	public ItemEventFriend(String picture, String nameEvent, String title,
			String text, String date, String start, String close,
			String idCreator, String nameFriend, long id) {
		super();
		this.picture = picture;
		this.nameEvent = nameEvent;
		this.title = title;
		this.text = text;
		this.date = date;
		this.start = start;
		this.close = close;
		this.idCreator = idCreator;
		this.nameFriend = nameFriend;
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getNameEvent() {
		return nameEvent;
	}

	public void setNameEvent(String nameEvent) {
		this.nameEvent = nameEvent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getNameFriend() {
		return nameFriend;
	}

	public void setNameFriend(String nameFriend) {
		this.nameFriend = nameFriend;
	}
	
	

}
