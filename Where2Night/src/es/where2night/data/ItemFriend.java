package es.where2night.data;


public class ItemFriend extends Item{
	protected String picture;
	protected String name;
	protected String ubication;
	protected String mode;
	protected String estado;
	

	public ItemFriend (String picture, String name, String ubication, String mode, String estado){
		super();
		this.picture = picture;
		this.name = name;
		this.ubication = ubication;
		this.mode = mode;
		this.estado = estado;
	}

	public ItemFriend (String picture, String name, String ubication, String date, String mode, String estado, long id){
	
		super();
		this.picture = picture;
		this.name = name;
		this.ubication = ubication;
		this.id = id;
		this.mode = mode;
		this.estado = estado;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getTitle() {
		return name;
	}
	
	public void setTitle(String name) {
		this.name = name;
	}
	
	public String getUbication() {
		return ubication;
	}
	
	public void setUbication(String ubication) {
		this.ubication = ubication;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}

}