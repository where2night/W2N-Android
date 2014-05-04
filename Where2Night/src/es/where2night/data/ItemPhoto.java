package es.where2night.data;

public class ItemPhoto {
	protected String photo;
	protected long id;
	
	public ItemPhoto(String photo) {
		super();
		this.photo = photo;
	}

	public ItemPhoto(String photo, long id) {
		super();
		this.photo = photo;
		this.id = id;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
