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
import com.stripe.exception.StripeException;

import java.sql.Connection;
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
				userUpdated = userService.updateUserSetArtist(sub.getUserId(), true, transaction.getConnection());
				break;
			case LISTENER:
				userUpdated = userService.updateUserSetPremium(sub.getUserId(), true, transaction.getConnection());
				break;
			}

			if (!userUpdated) {
				throw new SQLException("Failed to update user role.");
			}

			boolean insert = subscriptionDAO.insertSubscription(sub, transaction.getConnection());
			
			transaction.commit();
			return insert;
		} catch (SQLException e) {
			e.printStackTrace(); 
			return false;
		}
	}
	
	public boolean updateSubscriptionEndDate(String stripeSubscriptionId) {
	    UserSubscription sub = subscriptionDAO.getSubscriptionByPaymentGatewayRef(stripeSubscriptionId);

	    if (sub == null) {
	        System.err.println("No subscription found for ID: " + stripeSubscriptionId);
	        return false;
	    }

	    SubscriptionPlan plan = subscriptionPlanDAO.getSubscriptionPlanById(sub.getSubscriptionPlanId());

	    if (plan == null) {
	        System.err.println("No plan found for ID: " + sub.getSubscriptionPlanId());
	        return false;
	    }

	    try (TransactionTemplate transaction = new TransactionTemplate()) {
	        boolean userUpdated = false;
	        switch (plan.getPlanType()) {
	            case CREATOR:
	                userUpdated = userService.updateUserSetArtist(sub.getUserId(), false, transaction.getConnection());
	                break;
	            case LISTENER:
	                userUpdated = userService.updateUserSetPremium(sub.getUserId(), false, transaction.getConnection());
	                break;
	            default:
	                System.err.println("Unknown plan type: " + plan.getPlanType());
	                return false;
	        }

	        if (!userUpdated) {
	            throw new SQLException("Failed to update user role.");
	        }

	        boolean update = subscriptionDAO.updateSubscriptionEndDate(sub.getSubscriptionId(), transaction.getConnection());

	        transaction.commit();
	        return update;
	    } catch (SQLException e) {
	        e.printStackTrace();
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

	private void attachPlanIfAvailable(UserSubscription sub) {
		if (sub != null && sub.getSubscriptionPlanId() > 0) {
			SubscriptionPlan plan = subscriptionPlanDAO.getSubscriptionPlanById(sub.getSubscriptionPlanId());
			sub.setSubscriptionPlan(plan);
		}
	}
	
	public UserSubscription getLatestSubscriptionByUserAndPlan(int userId, PlanType planType) {
        return subscriptionDAO.getLatestSubscriptionByUserIdAndPlanType(userId, planType);
    }
	
	public boolean hasActiveListenerSubscription(int userId) {
        UserSubscription sub = getLatestSubscriptionByUserAndPlan(userId, PlanType.LISTENER);
        return sub != null && sub.getEndDate() == null;
    }
	
	 public boolean hasActiveCreatorSubscription(int userId) {
        UserSubscription sub = getLatestSubscriptionByUserAndPlan(userId, PlanType.CREATOR);
        return sub != null && sub.getEndDate() == null;
    }
	
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