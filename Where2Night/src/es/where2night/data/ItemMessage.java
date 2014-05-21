package es.where2night.data;

public class ItemMessage {
	private String message;
	private int mode;
	private String date;
	
	public ItemMessage(String message, int mode, String date) {
		super();
		this.message = message;
		this.mode = mode;
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
