package com.enth.ecomusic.service;

import java.time.LocalDate;

import com.enth.ecomusic.model.dto.ReportDTO;
import com.enth.ecomusic.model.mapper.ReportMapper;

public class ReportService {
	
	private final SubscriptionService subscriptionService;
	private final UserService userService;
	private final MusicService musicService;
	
	public ReportService(SubscriptionService subscriptionService, UserService userService, MusicService musicService) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.musicService = musicService;
	}
	
	public ReportDTO retrieveReportDTO() {
		int userCount = userService.getUserCount();
		int musicCount = musicService.getPublicMusicCount();

	    int activeSubscriptionCount = subscriptionService.getActiveSubscriptionCount();
	    int totalRevenueCount = subscriptionService.getRevenueCount();
	    
	    int registeredUsersToday = userService.getRegisteredUserTodayCount();
	    int musicUploadedToday = musicService.getMusicUploadedTodayCount();
		
		return ReportMapper.INSTANCE.toDTO(0, 0, 0, 0, 0, 0);
	}
}
