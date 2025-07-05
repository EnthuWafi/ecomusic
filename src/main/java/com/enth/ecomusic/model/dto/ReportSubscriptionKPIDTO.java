package com.enth.ecomusic.model.dto;

public class ReportSubscriptionKPIDTO {
	private int totalSubscription;
	private int activeSubscription;
	private int cancelledSubscription;
	private double totalRevenue;
	
	
	
	public ReportSubscriptionKPIDTO(int totalSubscription, int activeSubscription, int cancelledSubscription,
			double totalRevenue) {
		super();
		this.totalSubscription = totalSubscription;
		this.activeSubscription = activeSubscription;
		this.cancelledSubscription = cancelledSubscription;
		this.totalRevenue = totalRevenue;
	}
	public int getTotalSubscription() {
		return totalSubscription;
	}
	public int getActiveSubscription() {
		return activeSubscription;
	}
	public int getCancelledSubscription() {
		return cancelledSubscription;
	}
	public double getTotalRevenue() {
		return totalRevenue;
	}
	
	
}
