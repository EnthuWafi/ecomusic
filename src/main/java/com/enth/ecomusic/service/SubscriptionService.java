package com.enth.ecomusic.service;

import com.enth.ecomusic.dao.SubscriptionDAO;
import com.enth.ecomusic.dao.SubscriptionPlanDAO;
import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.mapper.SubscriptionMapper;
import com.enth.ecomusic.model.transaction.TransactionTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionService {

	private final SubscriptionDAO subscriptionDAO;
	private final SubscriptionPlanDAO subscriptionPlanDAO;
	private final UserService userService;

	public SubscriptionService(UserService userService) {
		this.subscriptionDAO = new SubscriptionDAO();
		this.subscriptionPlanDAO = new SubscriptionPlanDAO();
		this.userService = userService;
	}

	// CREATE
	public boolean createSubscription(UserSubscription sub, PlanType planType) {
		// TODO: do operations with userService (update user -> artist)
		// autoclosable transaction
		try (TransactionTemplate transaction = new TransactionTemplate()) {
			boolean userUpdated = false;

			switch (planType) {
			case CREATOR:
				userUpdated = userService.updateUserWithRoleName(sub.getUserId(), RoleType.ARTIST,
						transaction.getConnection());
				break;
			case LISTENER:
				userUpdated = userService.updateUserWithRoleName(sub.getUserId(), RoleType.PREMIUMUSER,
						transaction.getConnection());
				break;
			}

			if (!userUpdated) {
				throw new SQLException("Failed to update user role.");
			}

			return subscriptionDAO.insertSubscription(sub, transaction.getConnection());
		} catch (SQLException e) {
			e.printStackTrace(); // or use a logger
			return false;
		}
	}

	// GET by subscription ID (with plan loaded)
	public SubscriptionDTO getSubscriptionById(int id) {
		UserSubscription sub = subscriptionDAO.getSubscriptionById(id);
		attachPlanIfAvailable(sub);

		return SubscriptionMapper.INSTANCE.toDTO(sub);
	}

	// GET all user subscriptions (with plans)
	public List<SubscriptionDTO> getSubscriptionsByUserId(int userId) {
		List<UserSubscription> subs = subscriptionDAO.getSubscriptionsByUserId(userId);
		List<SubscriptionDTO> subsDTO = new ArrayList<>();
		for (UserSubscription sub : subs) {
			attachPlanIfAvailable(sub);
			subsDTO.add(SubscriptionMapper.INSTANCE.toDTO(sub));
		}
		return subsDTO;
	}

	// UPDATE
	public boolean updateSubscription(UserSubscription sub) {
		return subscriptionDAO.updateSubscription(sub);
	}

	// DELETE
	public boolean deleteSubscription(int subscriptionId) {
		return subscriptionDAO.deleteSubscription(subscriptionId);
	}

	// PRIVATE — reusability booster
	private void attachPlanIfAvailable(UserSubscription sub) {
		if (sub != null && sub.getSubscriptionPlanId() > 0) {
			SubscriptionPlan plan = subscriptionPlanDAO.getSubscriptionPlanById(sub.getSubscriptionPlanId());
			sub.setSubscriptionPlan(plan);
		}
	}

	// Optional — get all available plans
	public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
		return subscriptionPlanDAO.getAllSubscriptionPlans().stream().map(SubscriptionMapper.INSTANCE::toDTO)
				.collect(Collectors.toList());
	}

	public List<SubscriptionPlanDTO> getAllSubscriptionPlansForListener() {
		return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("listener").stream()
				.map(SubscriptionMapper.INSTANCE::toDTO).collect(Collectors.toList());
	}

	public List<SubscriptionPlanDTO> getAllSubscriptionPlansForCreator() {
		return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("creator").stream()
				.map(SubscriptionMapper.INSTANCE::toDTO).collect(Collectors.toList());
	}

	public SubscriptionPlanDTO getSubscriptionPlanById(int planId) {
		SubscriptionPlan subscriptionPlan = subscriptionPlanDAO.getSubscriptionPlanById(planId);
		return subscriptionPlan != null ? SubscriptionMapper.INSTANCE.toDTO(subscriptionPlan) : null;
	}

	public SubscriptionPlanDTO getSubscriptionPlanByStripeId(String stripePriceId) {
		SubscriptionPlan subscriptionPlan = subscriptionPlanDAO.getSubscriptionPlanByStripePriceId(stripePriceId);
		return subscriptionPlan != null ? SubscriptionMapper.INSTANCE.toDTO(subscriptionPlan) : null;
	}
}