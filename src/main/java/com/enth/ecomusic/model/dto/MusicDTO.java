package com.enth.ecomusic.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enth.ecomusic.model.enums.VisibilityType;

public class MusicDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int musicId;
    private int artistId;
    private String title;
    private String description;
    private String audioFileUrl;
    private String imageUrl;
    private boolean premiumContent;
    private int genreId;
    private int moodId;
    private String genreName;
    private String moodName;
    private int likeCount;
    private int totalPlayCount;
    private VisibilityType visibility;
    private LocalDateTime uploadDate;
    
	

	public MusicDTO(int musicId, int artistId, String title, String description, String audioFileUrl, String imageUrl,
			boolean premiumContent, int genreId, int moodId, String genreName, String moodName, int likeCount,
			int totalPlayCount, VisibilityType visibility, LocalDateTime uploadDate) {
		super();
		this.musicId = musicId;
		this.artistId = artistId;
		this.title = title;
		this.description = description;
		this.audioFileUrl = audioFileUrl;
		this.imageUrl = imageUrl;
		this.premiumContent = premiumContent;
		this.genreId = genreId;
		this.moodId = moodId;
		this.genreName = genreName;
		this.moodName = moodName;
		this.likeCount = likeCount;
		this.totalPlayCount = totalPlayCount;
		this.visibility = visibility;
		this.uploadDate = uploadDate;
	}


	public MusicDTO() {
		super();
	}


	// Getters and Setters
    public LocalDateTime getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
   
    public int getMusicId() { return musicId; }

	public void setMusicId(int musicId) { this.musicId = musicId; }

    public int getArtistId() {
		return artistId;
	}
	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
	public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAudioFileUrl() { return audioFileUrl; }
    public void setAudioFileUrl(String audioFileUrl) { this.audioFileUrl = audioFileUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPremiumContent() { return premiumContent; }
    public void setPremiumContent(boolean premiumContent) { this.premiumContent = premiumContent; }

    
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
	public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }

    public String getMoodName() { return moodName; }
    public void setMoodName(String moodName) { this.moodName = moodName; }


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
	
	
    
}