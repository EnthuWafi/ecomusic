package com.enth.ecomusic.dao;

import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {

    // CREATE
    public boolean insertSubscription(UserSubscription sub) {
        String sql = "INSERT INTO Subscriptions (subscription_id, user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getSubscriptionId());
            stmt.setInt(2, sub.getUserId());
            stmt.setDate(3, sub.getStartDate() != null ? Date.valueOf(sub.getStartDate()) : null);
            stmt.setDate(4, sub.getEndDate() != null ? Date.valueOf(sub.getEndDate()) : null);
            stmt.setDouble(5, sub.getAmountPaid());
            stmt.setString(6, sub.getPaymentStatus());
            stmt.setString(7, sub.getPaymentGatewayRef());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertSubscription(UserSubscription sub, Connection conn) {
        String sql = "INSERT INTO Subscriptions (subscription_id, user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getSubscriptionId());
            stmt.setInt(2, sub.getUserId());
            stmt.setDate(3, sub.getStartDate() != null ? Date.valueOf(sub.getStartDate()) : null);
            stmt.setDate(4, sub.getEndDate() != null ? Date.valueOf(sub.getEndDate()) : null);
            stmt.setDouble(5, sub.getAmountPaid());
            stmt.setString(6, sub.getPaymentStatus());
            stmt.setString(7, sub.getPaymentGatewayRef());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (by ID)
    public UserSubscription getSubscriptionById(int id) {
        String sql = "SELECT * FROM Subscriptions WHERE subscription_id = ?";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSubscription(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ (all for a user)
    public List<UserSubscription> getSubscriptionsByUserId(int userId) {
        List<UserSubscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Subscriptions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToSubscription(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateSubscription(UserSubscription sub) {
        String sql = "UPDATE Subscriptions SET user_id = ?, start_date = ?, end_date = ?, amount_paid = ?, "
                   + "payment_status = ?, payment_gateway_ref = ? WHERE subscription_id = ?";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getUserId());
            stmt.setDate(2, sub.getStartDate() != null ? Date.valueOf(sub.getStartDate()) : null);
            stmt.setDate(3, sub.getEndDate() != null ? Date.valueOf(sub.getEndDate()) : null);
            stmt.setDouble(4, sub.getAmountPaid());
            stmt.setString(5, sub.getPaymentStatus());
            stmt.setString(6, sub.getPaymentGatewayRef());
            stmt.setInt(7, sub.getSubscriptionId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteSubscription(int id) {
        String sql = "DELETE FROM Subscriptions WHERE subscription_id = ?";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method
    private UserSubscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
    	LocalDate startDate = rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null;
    	LocalDate endDate = rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null;
    	LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return new UserSubscription(
                rs.getInt("subscription_id"),
                rs.getInt("user_id"),
                startDate,
                endDate,
                rs.getDouble("amount_paid"),
                rs.getString("payment_status"),
                rs.getString("payment_gateway_ref"),
                createdAt,
                rs.getInt("subscription_plan_id")
        );
    }
}
