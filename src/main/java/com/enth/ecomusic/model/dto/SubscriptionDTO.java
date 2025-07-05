package com.enth.ecomusic.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.enth.ecomusic.util.CommonUtil;

public class SubscriptionDTO {
	private int subscriptionId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amountPaid;
    private String paymentStatus;
    private String paymentGatewayRef;
    private LocalDateTime createdAt;
    private int subscriptionPlanId;

    private UserDTO user;
    private SubscriptionPlanDTO subscriptionPlan;

    

	public SubscriptionDTO(int subscriptionId, int userId, LocalDate startDate, LocalDate endDate, double amountPaid,
			String paymentStatus, String paymentGatewayRef, LocalDateTime createdAt, int subscriptionPlanId,
			UserDTO user, SubscriptionPlanDTO subscriptionPlan) {
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
		this.user = user;
		this.subscriptionPlan = subscriptionPlan;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public int getUserId() {
		return userId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	
	public Date getStartDateDate() {
		return CommonUtil.toDate(startDate);
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public Date getEndDateDate() {
		return CommonUtil.toDate(endDate);
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getPaymentGatewayRef() {
		return paymentGatewayRef;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getSubscriptionPlanId() {
		return subscriptionPlanId;
	}

	public SubscriptionPlanDTO getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public UserDTO getUser() {
		return user;
	}
    
	
    
}
