package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.NumberFormatUtil;

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
    private LocalDateTime updatedAt;
    
	
	public MusicDTO(int musicId, int artistId, String title, String description, String audioFileUrl, String imageUrl,
			boolean premiumContent, int genreId, int moodId, String genreName, String moodName, int likeCount,
			int totalPlayCount, VisibilityType visibility, LocalDateTime uploadDate, LocalDateTime updatedAt) {
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
		this.updatedAt = updatedAt;
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

	public Date getUploadDateDate() {
		return CommonUtil.toDate(uploadDate);
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public Date getUpdatedAtDate() {
		return CommonUtil.toDate(updatedAt);
	}
	
	public String getTotalPlayCountString() {
		return NumberFormatUtil.format(totalPlayCount);
	}
	public String getLikeCountString() {
		return NumberFormatUtil.format(likeCount);
	}

	
	
    
}