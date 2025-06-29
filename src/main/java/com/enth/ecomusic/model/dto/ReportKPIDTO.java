package com.enth.ecomusic.model.dto;

public class ReportKPIDTO {
	private int userCount;
	private int musicCount;

	private int activeSubscriptionCount;
	private double revenueAmount;

	private int registeredUsersToday;
	private int musicUploadedToday;

	public ReportKPIDTO(int userCount, int musicCount, int activeSubscriptionCount, double revenueAmount,
			int registeredUsersToday, int musicUploadedToday) {
		super();
		this.userCount = userCount;
		this.musicCount = musicCount;
		this.activeSubscriptionCount = activeSubscriptionCount;
		this.revenueAmount = revenueAmount;
		this.registeredUsersToday = registeredUsersToday;
		this.musicUploadedToday = musicUploadedToday;
	}

	public int getUserCount() {
		return userCount;
	}

	public int getMusicCount() {
		return musicCount;
	}

	public int getActiveSubscriptionCount() {
		return activeSubscriptionCount;
	}

	public double getRevenueAmount() {
		return revenueAmount;
	}

	public int getRegisteredUsersToday() {
		return registeredUsersToday;
	}

	public int getMusicUploadedToday() {
		return musicUploadedToday;
	}

}
