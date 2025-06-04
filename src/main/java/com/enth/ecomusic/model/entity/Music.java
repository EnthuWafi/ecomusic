package com.enth.ecomusic.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Music implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int musicId;
    private int artistId;
    private String title;
    private String description;
    private LocalDateTime uploadDate;
    private String audioFileUrl;
    private String imageUrl;
    private boolean premiumContent;
    private int genreId;
    private int moodId;

	
	
	public Music(int musicId, int artistId, String title, int genreId, int moodId, String description, LocalDateTime uploadDate,
			String audioFileUrl, String imageUrl, boolean premiumContent) {
		super();
		this.musicId = musicId;
		this.artistId = artistId;
		this.title = title;
		this.genreId = genreId;
		this.moodId = moodId;
		this.description = description;
		this.uploadDate = uploadDate;
		this.audioFileUrl = audioFileUrl;
		this.imageUrl = imageUrl;
		this.premiumContent = premiumContent;
	}


	//this constructor for database insertion 

	public Music(int artistId, String title, int genreId, int moodId, String description, String audioFileUrl, String imageUrl,
			boolean premiumContent) {
		super();
		this.artistId = artistId;
		this.title = title;
		this.genreId = genreId;
		this.moodId = moodId;
		this.description = description;
		this.audioFileUrl = audioFileUrl;
		this.imageUrl = imageUrl;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public LocalDateTime getUploadDate() {
		return uploadDate;
	}


	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}


	public int getGenreId() {
		return genreId;
	}


	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}


	public int getMoodId() {
		return moodId;
	}


	public void setMoodId(Integer moodId) {
		this.moodId = moodId;
	}


	public String getAudioFileUrl() {
		return audioFileUrl;
	}

	public void setAudioFileUrl(String audioFileUrl) {
		this.audioFileUrl = audioFileUrl;
	}
	

	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public boolean isPremiumContent() {
		return premiumContent;
	}

	public void setPremiumContent(boolean premiumContent) {
		this.premiumContent = premiumContent;
	}

}
