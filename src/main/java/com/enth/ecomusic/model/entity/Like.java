package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;

public class Like{
    /**
	 * 
	 */
	private int userId;
    private int musicId;
    private LocalDateTime likedAt;
 
    private Music music;
    
    public Like() {
		super();
	}
	public Like(int userId, int musicId, LocalDateTime likedAt) {
		super();
		this.userId = userId;
		this.musicId = musicId;
		this.likedAt = likedAt;
	}
	// Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMusicId() { return musicId; }
    public void setMusicId(int musicId) { this.musicId = musicId; }

    public LocalDateTime getLikedAt() { return likedAt; }
    public void setLikedAt(LocalDateTime likedAt) { this.likedAt = likedAt; }
	public Music getMusic() {
		return music;
	}
	public void setMusic(Music music) {
		this.music = music;
	}
    
    
}