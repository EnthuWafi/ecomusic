package com.enth.ecomusic.model.dto;

public class ReportUserKPIDTO {
	private int totalUserCount;
	private int adminCount;
	private int superAdminCount;
	private int artistCount;
	private int premiumCount;
	private int userCount;
	
	public ReportUserKPIDTO(int totalUserCount, int adminCount, int superAdminCount, int artistCount, int premiumCount,
			int userCount) {
		super();
		this.totalUserCount = totalUserCount;
		this.adminCount = adminCount;
		this.superAdminCount = superAdminCount;
		this.artistCount = artistCount;
		this.premiumCount = premiumCount;
		this.userCount = userCount;
	}
	public int getTotalUserCount() {
		return totalUserCount;
	}
	public int getAdminCount() {
		return adminCount;
	}
	public int getSuperAdminCount() {
		return superAdminCount;
	}
	public int getArtistCount() {
		return artistCount;
	}
	public int getPremiumCount() {
		return premiumCount;
	}
	public int getUserCount() {
		return userCount;
	}
	
	
}
