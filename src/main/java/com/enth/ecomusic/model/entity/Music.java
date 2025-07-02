package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;

import com.enth.ecomusic.model.enums.VisibilityType;

public class Music{

	/**
	 * 
	 */
	private int musicId;
    private int artistId;
    private String title;
    private String description;
    private LocalDateTime updatedAt;
    private LocalDateTime uploadDate;
    private String audioFileUrl;
    private String imageUrl;
    private boolean premiumContent;
    private int genreId;
    private int moodId;
    private int likeCount;
    private int totalPlayCount;
    private VisibilityType visibility;
    
	private Genre genre;
	private Mood mood;
	private User artist;
	
	public Music(int musicId, int artistId, String title, String description, LocalDateTime updatedAt, LocalDateTime uploadDate,
			String audioFileUrl, String imageUrl, boolean premiumContent, int genreId, int moodId, int likeCount,
			int totalPlayCount, VisibilityType visibility) {
		super();
		this.musicId = musicId;
		this.artistId = artistId;
		this.title = title;
		this.description = description;
		this.updatedAt = updatedAt;
		this.uploadDate = uploadDate;
		this.audioFileUrl = audioFileUrl;
		this.imageUrl = imageUrl;
		this.premiumContent = premiumContent;
		this.genreId = genreId;
		this.moodId = moodId;
		this.likeCount = likeCount;
		this.totalPlayCount = totalPlayCount;
		this.visibility = visibility;
	}

	public Music(int artistId, String title, int genreId, int moodId, String description, String audioFileUrl, String imageUrl,
			boolean premiumContent, VisibilityType visibility) {
		super();
		this.artistId = artistId;
		this.title = title;
		this.genreId = genreId;
		this.moodId = moodId;
		this.description = description;
		this.audioFileUrl = audioFileUrl;
		this.imageUrl = imageUrl;
		this.premiumContent = premiumContent;
		this.visibility = visibility;
	}
	
	

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


	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}


	public int getMoodId() {
		return moodId;
	}


	public void setMoodId(int moodId) {
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

	

	public int getLikeCount() {
		return likeCount;
	}


	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}


	public int getTotalPlayCount() {
		return totalPlayCount;
	}


	public void setTotalPlayCount(int totalPlayCount) {
		this.totalPlayCount = totalPlayCount;
	}


	public VisibilityType getVisibility() {
		return visibility;
	}


	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}


	public Genre getGenre() {
		return genre;
	}


	public void setGenre(Genre genre) {
		this.genre = genre;
	}


	public Mood getMood() {
		return mood;
	}


	public void setMood(Mood mood) {
		this.mood = mood;
	}


	public User getArtist() {
		return artist;
	}


	public void setArtist(User artist) {
		this.artist = artist;
	}


	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	

}
