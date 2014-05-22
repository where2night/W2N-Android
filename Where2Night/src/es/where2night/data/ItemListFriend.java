package es.where2night.data;

public class ItemListFriend extends Item{
	private String picture;
	private String text;
	private String nombre;
	private String title;
	private String description;
	private String date;
	private String start;
	private String end;
	private String expireDate;
	private boolean goes;
	
	public ItemListFriend(String picture, String text, String nombre, String title,
			String description, String date, String start, String end,
			String expireDate, boolean goes) {
		super();
		this.picture = picture;
		this.text = text;
		this.title = title;
		this.description = description;
		this.date = date;
		this.start = start;
		this.end = end;
		this.expireDate = expireDate;
		this.goes = goes;
		this.nombre = nombre;
	}
	
	public ItemListFriend(String picture, String text, String nombre, String title,
			String description, String date, String start, String end,
			String expireDate, boolean goes, long id) {
		super();
		this.picture = picture;
		this.text = text;
		this.title = title;
		this.description = description;
		this.date = date;
		this.start = start;
		this.end = end;
		this.expireDate = expireDate;
		this.goes = goes;
		this.id = id;
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isGoes() {
		return goes;
	}

	public void setGoes(boolean goes) {
		this.goes = goes;
	}
}
