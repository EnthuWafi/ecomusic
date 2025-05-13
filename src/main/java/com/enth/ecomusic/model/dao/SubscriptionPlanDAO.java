package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.SubscriptionPlan;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanDAO {

    // CREATE
    public boolean insertSubscriptionPlan(SubscriptionPlan plan) {
        String sql = "INSERT INTO SubscriptionPlans (name, stripe_price_id, billing_cycle, price, description, features, created_at, plan_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, plan.getName());
            stmt.setString(2, plan.getStripePriceId());
            stmt.setString(3, plan.getBillingCycle());
            stmt.setDouble(4, plan.getPrice());
            stmt.setString(5, plan.getDescription());
            stmt.setString(6, plan.getFeatures());
            stmt.setTimestamp(7, new Timestamp(plan.getCreatedAt().getTime()));
            stmt.setString(8, plan.getPlanType());

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

    // Helper: Map ResultSet to SubscriptionPlan
    private SubscriptionPlan mapResultSetToSubscriptionPlan(ResultSet rs) throws SQLException {
        return new SubscriptionPlan(
                rs.getInt("subscription_plan_id"),
                rs.getString("name"),
                rs.getString("stripe_price_id"),
                rs.getString("billing_cycle"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getString("features"),
                rs.getTimestamp("created_at"),
                rs.getString("plan_type")
        );
    }
}