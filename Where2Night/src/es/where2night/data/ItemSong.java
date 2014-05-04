package es.where2night.data;

public class ItemSong {
	private String songName;
	private String artist;
	private int votes;
	protected long id;
	private boolean voted;
	private boolean checkIn;
	
	public ItemSong(String songName, String artist, int votes, boolean voted, boolean checkIn) {
		super();
		this.songName = songName;
		this.artist = artist;
		this.votes = votes;
		this.voted = voted;
		this.checkIn = checkIn;
	}
	
	public ItemSong(String songName, String artist, int votes, long id, boolean voted, boolean checkIn) {
		super();
		this.songName = songName;
		this.artist = artist;
		this.votes = votes;
		this.voted = voted;
		this.id = id;
		this.checkIn = checkIn;
	}
	
	public String getSongName() {
		return songName;
	}
	
	public void setSongName(String name) {
		this.songName = name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public boolean isCheckIn() {
		return checkIn;
	}

	public void setCheckIn(boolean checkIn) {
		this.checkIn = checkIn;
	}

}
