package es.where2night.data;

public class ItemDiscountList {
	
	private String day;
	private String expireDate;
	
	public ItemDiscountList(String day, String expireDate){
		this.day = day;
		this.expireDate = expireDate;
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

}
