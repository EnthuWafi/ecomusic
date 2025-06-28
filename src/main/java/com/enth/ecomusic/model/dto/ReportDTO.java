package com.enth.ecomusic.model.dto;

public class ReportDTO {
	private int totalUsers;
    private int musicCount;
    
    private int activeSubscriptionCount;
    private int revenueCount;
    
    private int registeredUsersToday;
    private int musicUploadedToday;
    
	public ReportDTO(int totalUsers, int musicCount, int activeSubscriptionCount, int revenueCount,
			int registeredUsersToday, int musicUploadedToday) {
		super();
		this.totalUsers = totalUsers;
		this.musicCount = musicCount;
		this.activeSubscriptionCount = activeSubscriptionCount;
		this.revenueCount = revenueCount;
		this.registeredUsersToday = registeredUsersToday;
		this.musicUploadedToday = musicUploadedToday;
	}
	public int getTotalUsers() {
		return totalUsers;
	}
	public int getMusicCount() {
		return musicCount;
	}
	public int getActiveSubscriptionCount() {
		return activeSubscriptionCount;
	}
	public int getRevenueCount() {
		return revenueCount;
	}
	public int getRegisteredUsersToday() {
		return registeredUsersToday;
	}
	public int getMusicUploadedToday() {
		return musicUploadedToday;
	}
    
    
    
}
