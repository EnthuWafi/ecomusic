package com.enth.ecomusic.model.dto;

public class ReportMusicKPIDTO {
	private int totalMusicCount;
	private int publicMusicCount;
	private int nonPremiumCount;
	private int premiumCount;
	public ReportMusicKPIDTO(int totalMusicCount, int publicMusicCount, int nonPremiumCount, int premiumCount) {
		super();
		this.totalMusicCount = totalMusicCount;
		this.publicMusicCount = publicMusicCount;
		this.nonPremiumCount = nonPremiumCount;
		this.premiumCount = premiumCount;
	}
	public int getTotalMusicCount() {
		return totalMusicCount;
	}
	public int getPublicMusicCount() {
		return publicMusicCount;
	}
	public int getNonPremiumCount() {
		return nonPremiumCount;
	}
	public int getPremiumCount() {
		return premiumCount;
	}
	
	
	
}
