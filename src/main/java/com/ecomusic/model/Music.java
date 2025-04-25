package com.ecomusic.model;

import java.util.Date;

public class Music {

	private int musicId;
	private int artistId;
	private String title;
	private String genre;
	private String description;
	private Date uploadDate;
	private String audioFileUrl;
	private boolean premiumContent;

	public Music(int musicId, int artistId, String title, String genre, String description, Date uploadDate,
			String audioFileUrl, boolean premiumContent) {
		super();
		this.musicId = musicId;
		this.artistId = artistId;
		this.title = title;
		this.genre = genre;
		this.description = description;
		this.uploadDate = uploadDate;
		this.audioFileUrl = audioFileUrl;
		this.premiumContent = premiumContent ? true : false;
	}
	
	
	//this constructor for database insertion 
	public Music(int artistId, String title, String genre, String description, String audioFileUrl,
			boolean premiumContent) {
		super();
		this.artistId = artistId;
		this.title = title;
		this.genre = genre;
		this.description = description;
		this.audioFileUrl = audioFileUrl;
		this.premiumContent = premiumContent;
	}



	public Music() {
		super();
	}


	// Getters & Setters
	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getAudioFileUrl() {
		return audioFileUrl;
	}

	public void setAudioFileUrl(String audioFileUrl) {
		this.audioFileUrl = audioFileUrl;
	}

	public boolean isPremiumContent() {
		return premiumContent;
	}

	public void setPremiumContent(boolean premiumContent) {
		this.premiumContent = premiumContent;
	}

}
