package com.enth.ecomusic.model.entity;

import java.time.LocalDate;

public class UserMusicDailyStats {
	
    private int userId;
    private int musicId;
    private LocalDate statDate;
    private int totalPlays;
    private int totalSkips;
    private long totalListenTime;
    
	public UserMusicDailyStats() {
		super();
	}
	public UserMusicDailyStats(int userId, int musicId, LocalDate statDate, int totalPlays, int totalSkips,
			long totalListenTime) {
		super();
		this.userId = userId;
		this.musicId = musicId;
		this.statDate = statDate;
		this.totalPlays = totalPlays;
		this.totalSkips = totalSkips;
		this.totalListenTime = totalListenTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMusicId() {
		return musicId;
	}
	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}
	public LocalDate getStatDate() {
		return statDate;
	}
	public void setStatDate(LocalDate statDate) {
		this.statDate = statDate;
	}
	public int getTotalPlays() {
		return totalPlays;
	}
	public void setTotalPlays(int totalPlays) {
		this.totalPlays = totalPlays;
	}
	public int getTotalSkips() {
		return totalSkips;
	}
	public void setTotalSkips(int totalSkips) {
		this.totalSkips = totalSkips;
	}
	public double getTotalListenTime() {
		return totalListenTime;
	}
	public void setTotalListenTime(long totalListenTime) {
		this.totalListenTime = totalListenTime;
	}
    
    
}
