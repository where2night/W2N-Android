package es.where2night.data;

public class ItemDiscountList {
	
	private String title;
	private String description;
	private String date;
	private String start;
	private String end;
	private String expireDate;
	private long id;
	private boolean goes;
	
	public ItemDiscountList(long id,String title, String description, String date, String start, String end, String expireDate, boolean goes){
		this.title = title;
		this.description = description;
		this.start = start;
		this.end = end;
		this.id = id;
		this.date = date;
		this.expireDate = expireDate;
		this.goes = goes;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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

}
