package com.enth.ecomusic.service;
import com.enth.ecomusic.model.dao.SubscriptionDAO;
import com.enth.ecomusic.model.dao.SubscriptionPlanDAO;
import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.mapper.SubscriptionMapper;

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
    public boolean createSubscriptionForArtist(UserSubscription sub) {
    	//TODO: do operations with userService (update user -> artist)
    	// RoleType.ARTIST
    	boolean inserted = subscriptionDAO.insertSubscription(sub);
        if (inserted) {
            userService.updateUserWithRoleName(sub.getUserId(), RoleType.ARTIST);
        }
        return subscriptionDAO.insertSubscription(sub);
    }
    
    public boolean createSubscriptionForPremiumUser(UserSubscription sub) {
    	//TODO: do operations with userService (update user -> premium artist)
    	// RoleType.PREMIUMUSER
    	boolean inserted = subscriptionDAO.insertSubscription(sub);
        if (inserted) {
            userService.updateUserWithRoleName(sub.getUserId(), RoleType.PREMIUMUSER);
        }
        return subscriptionDAO.insertSubscription(sub);
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
        return subscriptionPlanDAO.getAllSubscriptionPlans().stream()
                .map(SubscriptionMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPlanDTO> getAllSubscriptionPlansForListener() {
        return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("listener").stream()
                .map(SubscriptionMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPlanDTO> getAllSubscriptionPlansForCreator() {
        return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("creator").stream()
                .map(SubscriptionMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
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