package com.enth.ecomusic.dao;

import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.util.DBConnection;
import com.enth.ecomusic.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanDAO {

    // CREATE
    public boolean insertSubscriptionPlan(SubscriptionPlan plan) {
        String sql = "INSERT INTO SubscriptionPlans (name, stripe_price_id, billing_cycle, price, description, features, plan_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, plan.getName());
            stmt.setString(2, plan.getStripePriceId());
            stmt.setString(3, plan.getBillingCycle());
            stmt.setDouble(4, plan.getPrice());
            stmt.setString(5, plan.getDescription());
            stmt.setString(6, plan.getFeaturesJson());
            stmt.setString(7, plan.getPlanType().getValue());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting subscription plan: " + e.getMessage());
            return false;
        }
    }

    // READ by ID
    public SubscriptionPlan getSubscriptionPlanById(int id) {
        String sql = "SELECT * FROM SubscriptionPlans WHERE subscription_plan_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscriptionPlan(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching subscription plan by ID: " + e.getMessage());
        }

        return null;
    }

    // READ by Stripe Price ID
    public SubscriptionPlan getSubscriptionPlanByStripePriceId(String stripePriceId) {
        String sql = "SELECT * FROM SubscriptionPlans WHERE stripe_price_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stripePriceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscriptionPlan(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching subscription plan by Stripe Price ID: " + e.getMessage());
        }

        return null;
    }

    // READ all
    public List<SubscriptionPlan> getAllSubscriptionPlans() {
        List<SubscriptionPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM SubscriptionPlans ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                plans.add(mapResultSetToSubscriptionPlan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all subscription plans: " + e.getMessage());
        }

        return plans;
    }
    
    public List<SubscriptionPlan> getAllSubscriptionPlansByPlanType(String planType) {
        List<SubscriptionPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM SubscriptionPlans WHERE plan_type = ?"
        		+ "ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setString(1, planType);
        	ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                plans.add(mapResultSetToSubscriptionPlan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all subscription plans: " + e.getMessage());
        }

        return plans;
    }
  

    // Helper: Map ResultSet to SubscriptionPlan
    private SubscriptionPlan mapResultSetToSubscriptionPlan(ResultSet rs) throws SQLException {
        int id = rs.getInt("subscription_plan_id");
        String name = rs.getString("name");
        String stripeId = rs.getString("stripe_price_id");
        String cycle = rs.getString("billing_cycle");
        double price = rs.getDouble("price");
        String desc = rs.getString("description");
        String rawJson = rs.getString("features");
        PlanType type = PlanType.fromString(rs.getString("plan_type"));
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        List<String> features;
        if (rawJson != null && rawJson.trim().startsWith("[")) {
            TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
            features = JsonUtil.fromJson(rawJson, typeToken);
        } else {
            features = new ArrayList<>();
        }


        return new SubscriptionPlan(
                id,
                name,
                stripeId,
                cycle,
                price,
                desc,
                features,
                createdAt,
                type
        );
    }
}