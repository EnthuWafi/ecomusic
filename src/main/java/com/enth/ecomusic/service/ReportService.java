package com.enth.ecomusic.service;

import java.time.LocalDate;

import com.enth.ecomusic.model.dto.ReportDTO;

public class ReportService {
	
	private final SubscriptionService subscriptionService;
	
	public ReportService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	//I dont know
	public ReportDTO retrieveRevenueReport(LocalDate start, LocalDate end) {
		return null;
	}
}
