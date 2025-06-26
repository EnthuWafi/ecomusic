package com.enth.ecomusic.model.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class SubscriptionPlan{
    /**
	 * 
	 */
	private int subscriptionPlanId;
    private String name;
    private String stripePriceId;
    private String billingCycle;
    private double price;
    private String description;
    private LocalDateTime createdAt;
    private PlanType planType;

    private List<String> features; 
    
    public SubscriptionPlan(int subscriptionPlanId, String name, String stripePriceId, String billingCycle,
                            double price, String description, List<String> features, LocalDateTime createdAt, PlanType planType) {
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

    

	public SubscriptionPlan(String name, String stripePriceId, String billingCycle, double price, String description,
			List<String> features, PlanType planType) {
		super();
		this.name = name;
		this.stripePriceId = stripePriceId;
		this.billingCycle = billingCycle;
		this.price = price;
		this.description = description;
		this.features = features;
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


    public String getFeaturesJson() {
        return JsonUtil.toJson(this.features);
    }

    public void setFeaturesFromJson(String json) {
        TypeToken<List<String>> typeToken = new TypeToken<>(){};
        this.features = JsonUtil.fromJson(json, typeToken);
    }


    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public PlanType getPlanType() {
		return planType;
	}


	public void setPlanType(PlanType planType) {
		this.planType = planType;
	}

	
    
}