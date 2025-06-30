package com.enth.ecomusic.service;

import java.time.LocalDate;

import com.enth.ecomusic.dao.ReportDAO;
import com.enth.ecomusic.model.dto.ChartDTO;
import com.enth.ecomusic.model.dto.ReportKPIDTO;
import com.enth.ecomusic.model.dto.ReportUserKPIDTO;

public class ReportService {
	
	private final ReportDAO reportDAO;
	private final SubscriptionService subscriptionService;
	private final UserService userService;
	private final MusicService musicService;
	
	public static final String FORMAT_WEEKLY = "IYYY-\"W\"IW";
	public static final String FORMAT_MONTHLY = "YYYY-MM";
	public static final String FORMAT_DAILY = "YYYY-MM-DD";
	public static final String FORMAT_YEARLY = "YYYY";
	
	
	public ReportService(SubscriptionService subscriptionService, UserService userService, MusicService musicService) {
		this.reportDAO = new ReportDAO();
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.musicService = musicService;
	}
	
	public ReportKPIDTO getReportKPIDTO() {
		int userCount = userService.getUserCount();
		int musicCount = musicService.getPublicMusicCount();

	    int activeSubscriptionCount = subscriptionService.getActiveSubscriptionCount();
	    double totalRevenueAmount = subscriptionService.getRevenueAmount();
	    
	    int registeredUsersToday = userService.getRegisteredUserTodayCount();
	    int musicUploadedToday = musicService.getMusicUploadedTodayCount();
		
		return new ReportKPIDTO(userCount, musicCount, activeSubscriptionCount, totalRevenueAmount, registeredUsersToday, musicUploadedToday);
	}
	
	public ReportUserKPIDTO getReportUserKPIDTO() {
		int totalUserCount = userService.getUserCount();
		int adminCount = userService.getAdminCount();
		int superAdminCount = userService.getSuperAdminCount();
		int artistCount = userService.getArtistCount();
		int premiumCount = userService.getPremiumCount();
		int userCount = userService.getNormalUserCount();
		
		return new ReportUserKPIDTO(totalUserCount, adminCount, superAdminCount, artistCount, premiumCount, userCount);
	}
	
	private String getDateFormat(String dateType) {
		if (dateType == null) return FORMAT_WEEKLY;
		switch (dateType.toLowerCase()) {
			case "yearly": return FORMAT_YEARLY;
			case "daily": return FORMAT_DAILY;
			case "monthly": return FORMAT_MONTHLY;
			default: return FORMAT_WEEKLY;
		}
	}
	
	public ChartDTO getUserPlayChartDTO(LocalDate start, LocalDate end, String dateType) {
		return reportDAO.getChartByDateGroup("PlayHistory", "played_at", "Play Count", "*", false, start, end, getDateFormat(dateType));

	}
	
	public ChartDTO getMusicUploadChartDTO(LocalDate start, LocalDate end, String dateType) {
		return reportDAO.getChartByDateGroup("Music", "upload_date", "Music Upload", "*", false, start, end, getDateFormat(dateType));
	}

	public ChartDTO getUserGrowthChartDTO(LocalDate start, LocalDate end, String dateType) {
		return reportDAO.getChartByDateGroup("Users", "created_at", "User Count", "*", false, start, end, getDateFormat(dateType));
	}

	public ChartDTO getRevenueSumChartDTO(LocalDate start, LocalDate end, String dateType) {
		return reportDAO.getChartByDateGroup("Subscriptions", "created_at", "Revenue", "amount_paid", true, start, end, getDateFormat(dateType));
	}
	
}
