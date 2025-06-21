package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.util.CommonUtil;

public class SubscriptionPlanDTO {
	private int subscriptionPlanId;
    private String name;
    private String stripePriceId;
    private String billingCycle;
    private double price;
    private String description;
    private LocalDateTime createdAt;
    private PlanType planType;

    private List<String> features;

	public SubscriptionPlanDTO(int subscriptionPlanId, String name, String stripePriceId, String billingCycle,
			double price, String description, LocalDateTime createdAt, PlanType planType, List<String> features) {
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
	
	public Date getCreatedAtDate() {
		return CommonUtil.toDate(createdAt);
	}

	public PlanType getPlanType() {
		return planType;
	}

	public List<String> getFeatures() {
		return features;
	} 
    
    
}
