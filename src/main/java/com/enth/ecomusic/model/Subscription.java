package com.enth.ecomusic.model;

import java.util.Date;

public class Subscription {
	private int subscriptionId;
    private int userId;
    private Date startDate;
    private Date endDate;
    private double amountPaid;
    private String paymentStatus;
    private String paymentGatewayRef;
    private Date createdAt;
    private int subscriptionPlanId;

 // Optional but convenient
    private SubscriptionPlan subscriptionPlan;

    public Subscription(int subscriptionId, int userId, Date startDate, Date endDate,
                        double amountPaid, String paymentStatus, String paymentGatewayRef,
                        Date createdAt, int subscriptionPlanId) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paymentGatewayRef = paymentGatewayRef;
        this.createdAt = createdAt;
        this.subscriptionPlanId = subscriptionPlanId;
    }
    
    
    
	public Subscription(int userId, Date startDate, Date endDate, double amountPaid, String paymentStatus,
			String paymentGatewayRef, int subscriptionPlanId) {
		super();
		this.userId = userId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amountPaid = amountPaid;
		this.paymentStatus = paymentStatus;
		this.paymentGatewayRef = paymentGatewayRef;
		this.subscriptionPlanId = subscriptionPlanId;
	}



	// Getters & Setters
    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentGatewayRef() { return paymentGatewayRef; }
    public void setPaymentGatewayRef(String paymentGatewayRef) { this.paymentGatewayRef = paymentGatewayRef; }

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getSubscriptionPlanId() {
		return subscriptionPlanId;
	}

	public void setSubscriptionPlanId(int subscriptionPlanId) {
		this.subscriptionPlanId = subscriptionPlanId;
	}

	public SubscriptionPlan getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}
	
	

    
}
