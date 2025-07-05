package com.enth.ecomusic.model.dto;

public class ReportSubscriptionPlanKPIDTO {
	private int totalPlan;
	private int listenerPlan;
	private int creatorPlan;
	public ReportSubscriptionPlanKPIDTO(int totalPlan, int listenerPlan, int creatorPlan) {
		super();
		this.totalPlan = totalPlan;
		this.listenerPlan = listenerPlan;
		this.creatorPlan = creatorPlan;
	}
	public int getTotalPlan() {
		return totalPlan;
	}
	public int getListenerPlan() {
		return listenerPlan;
	}
	public int getCreatorPlan() {
		return creatorPlan;
	}
	
	
}
