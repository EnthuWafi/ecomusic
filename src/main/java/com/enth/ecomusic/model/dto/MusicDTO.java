package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;

import com.enth.ecomusic.model.enums.VisibilityType;

public class MusicDTO{
    /**
	 * 
	 */

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



	public int getMusicId() {
		return musicId;
	}


	public int getArtistId() {
		return artistId;
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public String getAudioFileUrl() {
		return audioFileUrl;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public boolean isPremiumContent() {
		return premiumContent;
	}


	public int getGenreId() {
		return genreId;
	}


	public int getMoodId() {
		return moodId;
	}

	
	

	public String getGenreName() {
		return genreName;
	}




	public String getMoodName() {
		return moodName;
	}



	public int getLikeCount() {
		return likeCount;
	}


	public int getTotalPlayCount() {
		return totalPlayCount;
	}


	public VisibilityType getVisibility() {
		return visibility;
	}


	public LocalDateTime getUploadDate() {
		return uploadDate;
	}

	
    
}