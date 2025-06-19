package com.enth.ecomusic.model.dto;

import java.time.LocalDate;

public class ReportDTO {
	private double totalRevenue;
    private int totalSubscriptions;
    private LocalDate startDate;
    private LocalDate endDate;
    
    private int newUsers;
    private double avgRevenuePerUser;
    
	public ReportDTO(double totalRevenue, int totalSubscriptions, LocalDate startDate, LocalDate endDate, int newUsers,
			double avgRevenuePerUser) {
		super();
		this.totalRevenue = totalRevenue;
		this.totalSubscriptions = totalSubscriptions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.newUsers = newUsers;
		this.avgRevenuePerUser = avgRevenuePerUser;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public int getTotalSubscriptions() {
		return totalSubscriptions;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public int getNewUsers() {
		return newUsers;
	}

	public double getAvgRevenuePerUser() {
		return avgRevenuePerUser;
	} 
    
    
}
