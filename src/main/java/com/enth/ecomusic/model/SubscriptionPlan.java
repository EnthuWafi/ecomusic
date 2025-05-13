package com.enth.ecomusic.model;

import java.util.Date;

public class SubscriptionPlan {
    private int subscriptionPlanId;
    private String name;
    private String stripePriceId;
    private String billingCycle;
    private double price;
    private String description;
    private String features; 
    private Date createdAt;
    private String planType;

    
    public SubscriptionPlan(int subscriptionPlanId, String name, String stripePriceId, String billingCycle,
                            double price, String description, String features, Date createdAt, String planType) {
        this.subscriptionPlanId = subscriptionPlanId;
        this.name = name;
        this.stripePriceId = stripePriceId;
        this.billingCycle = billingCycle;
        this.price = price;
        this.description = description;
        this.features = features;
        this.createdAt = createdAt;
        this.planType = planType;
    }


	public int getSubscriptionPlanId() {
		return subscriptionPlanId;
	}


	public void setSubscriptionPlanId(int subscriptionPlanId) {
		this.subscriptionPlanId = subscriptionPlanId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getStripePriceId() {
		return stripePriceId;
	}


	public void setStripePriceId(String stripePriceId) {
		this.stripePriceId = stripePriceId;
	}


	public String getBillingCycle() {
		return billingCycle;
	}


	public void setBillingCycle(String billingCycle) {
		this.billingCycle = billingCycle;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getFeatures() {
		return features;
	}


	public void setFeatures(String features) {
		this.features = features;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getPlanType() {
		return planType;
	}


	public void setPlanType(String planType) {
		this.planType = planType;
	}
    
    
}