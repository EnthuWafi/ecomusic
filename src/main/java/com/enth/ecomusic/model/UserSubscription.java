package com.enth.ecomusic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class UserSubscription {
	private int subscriptionId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amountPaid;
    private String paymentStatus;
    private String paymentGatewayRef;
    private LocalDateTime createdAt;
    private int subscriptionPlanId;

    // Optional but convenient
    private SubscriptionPlan subscriptionPlan;


	public UserSubscription(int subscriptionId, int userId, LocalDate startDate, LocalDate endDate, double amountPaid,
			String paymentStatus, String paymentGatewayRef, LocalDateTime createdAt, int subscriptionPlanId) {
		super();
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
	
	
	public UserSubscription(int userId, LocalDate startDate, LocalDate endDate, double amountPaid, String paymentStatus,
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

    

    public LocalDate getStartDate() {
		return startDate;
	}



	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}



	public LocalDate getEndDate() {
		return endDate;
	}



	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}



	public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentGatewayRef() { return paymentGatewayRef; }
    public void setPaymentGatewayRef(String paymentGatewayRef) { this.paymentGatewayRef = paymentGatewayRef; }

	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
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
