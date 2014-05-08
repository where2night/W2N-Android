package es.where2night.data;

public class ItemDiscountList {
	
	private String day;
	private String expireDate;
	private long id;
	private boolean goes;
	
	public ItemDiscountList(long id, String day, String expireDate, boolean goes){
		this.id = id;
		this.day = day;
		this.expireDate = expireDate;
		this.goes = goes;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isGoes() {
		return goes;
	}

	public void setGoes(boolean goes) {
		this.goes = goes;
	}

}
