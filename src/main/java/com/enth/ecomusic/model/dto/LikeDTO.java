package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.enth.ecomusic.util.CommonUtil;

public class LikeDTO {
	
	private int userId;
    private int musicId;
    private LocalDateTime likedAt;
    private MusicDTO music;
	public LikeDTO(int userId, int musicId, LocalDateTime likedAt, MusicDTO music) {
		super();
		this.userId = userId;
		this.musicId = musicId;
		this.likedAt = likedAt;
		this.music = music;
	}
	public int getUserId() {
		return userId;
	}
	public int getMusicId() {
		return musicId;
	}
	public LocalDateTime getLikedAt() {
		return likedAt;
	}
	public Date getLikedAtDate() {
		return CommonUtil.toDate(likedAt);
	}
	
	public MusicDTO getMusic() {
		return music;
	}
    
    
    
}
