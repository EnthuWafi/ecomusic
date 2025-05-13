package com.enth.ecomusic.model.service;
import com.enth.ecomusic.model.Subscription;
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
    public boolean createSubscription(Subscription sub) {
        return subscriptionDAO.insertSubscription(sub);
    }

    // GET by subscription ID (with plan loaded)
    public Subscription getSubscriptionById(int id) {
        Subscription sub = subscriptionDAO.getSubscriptionById(id);
        attachPlanIfAvailable(sub);
        return sub;
    }

    // GET all user subscriptions (with plans)
    public List<Subscription> getSubscriptionsByUserId(int userId) {
        List<Subscription> subs = subscriptionDAO.getSubscriptionsByUserId(userId);
        for (Subscription sub : subs) {
            attachPlanIfAvailable(sub);
        }
        return subs;
    }

    // UPDATE
    public boolean updateSubscription(Subscription sub) {
        return subscriptionDAO.updateSubscription(sub);
    }

    // DELETE
    public boolean deleteSubscription(int subscriptionId) {
        return subscriptionDAO.deleteSubscription(subscriptionId);
    }

    // PRIVATE — reusability booster
    private void attachPlanIfAvailable(Subscription sub) {
        if (sub != null && sub.getSubscriptionPlanId() > 0) {
            SubscriptionPlan plan = subscriptionPlanDAO.getSubscriptionPlanById(sub.getSubscriptionPlanId());
            sub.setSubscriptionPlan(plan);
        }
    }

    // Optional — get all available plans
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        return subscriptionPlanDAO.getAllSubscriptionPlans();
    }

    // Optional — get a single plan
    public SubscriptionPlan getSubscriptionPlanById(int planId) {
        return subscriptionPlanDAO.getSubscriptionPlanById(planId);
    }

    // Optional — by Stripe ID
    public SubscriptionPlan getSubscriptionPlanByStripeId(String stripePriceId) {
        return subscriptionPlanDAO.getSubscriptionPlanByStripePriceId(stripePriceId);
    }
}