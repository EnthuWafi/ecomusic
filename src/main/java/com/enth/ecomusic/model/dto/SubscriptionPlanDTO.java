package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionPlanDTO {
	private int subscriptionPlanId;
    private String name;
    private String stripePriceId;
    private String billingCycle;
    private double price;
    private String description;
    private LocalDateTime createdAt;
    private String planType;

    private List<String> features;

	public SubscriptionPlanDTO(int subscriptionPlanId, String name, String stripePriceId, String billingCycle,
			double price, String description, LocalDateTime createdAt, String planType, List<String> features) {
		super();
		this.subscriptionPlanId = subscriptionPlanId;
		this.name = name;
		this.stripePriceId = stripePriceId;
		this.billingCycle = billingCycle;
		this.price = price;
		this.description = description;
		this.createdAt = createdAt;
		this.planType = planType;
		this.features = features;
	}

	public int getSubscriptionPlanId() {
		return subscriptionPlanId;
	}

	public String getName() {
		return name;
	}

	public String getStripePriceId() {
		return stripePriceId;
	}

	public String getBillingCycle() {
		return billingCycle;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getPlanType() {
		return planType;
	}

	public List<String> getFeatures() {
		return features;
	} 
    
    
}
