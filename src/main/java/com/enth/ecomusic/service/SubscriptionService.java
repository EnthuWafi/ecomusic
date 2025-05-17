package com.enth.ecomusic.service;
import com.enth.ecomusic.model.UserSubscription;
import com.enth.ecomusic.model.SubscriptionPlan;
import com.enth.ecomusic.model.dao.SubscriptionDAO;
import com.enth.ecomusic.model.dao.SubscriptionPlanDAO;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {

    private final SubscriptionDAO subscriptionDAO;
    private final SubscriptionPlanDAO subscriptionPlanDAO;

    public SubscriptionService() {
        this.subscriptionDAO = new SubscriptionDAO();
        this.subscriptionPlanDAO = new SubscriptionPlanDAO();
    }

    // CREATE
    public boolean createSubscription(UserSubscription sub) {
        return subscriptionDAO.insertSubscription(sub);
    }

    // GET by subscription ID (with plan loaded)
    public UserSubscription getSubscriptionById(int id) {
        UserSubscription sub = subscriptionDAO.getSubscriptionById(id);
        attachPlanIfAvailable(sub);
        return sub;
    }

    // GET all user subscriptions (with plans)
    public List<UserSubscription> getSubscriptionsByUserId(int userId) {
        List<UserSubscription> subs = subscriptionDAO.getSubscriptionsByUserId(userId);
        for (UserSubscription sub : subs) {
            attachPlanIfAvailable(sub);
        }
        return subs;
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
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        return subscriptionPlanDAO.getAllSubscriptionPlans();
    }
    
    public List<SubscriptionPlan> getAllSubscriptionPlansForListener() {
        return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("listener");
    }
    
    public List<SubscriptionPlan> getAllSubscriptionPlansForCreator() {
        return subscriptionPlanDAO.getAllSubscriptionPlansByPlanType("creator");
    }

    public SubscriptionPlan getSubscriptionPlanById(int planId) {
        return subscriptionPlanDAO.getSubscriptionPlanById(planId);
    }

    public SubscriptionPlan getSubscriptionPlanByStripeId(String stripePriceId) {
        return subscriptionPlanDAO.getSubscriptionPlanByStripePriceId(stripePriceId);
    }
}