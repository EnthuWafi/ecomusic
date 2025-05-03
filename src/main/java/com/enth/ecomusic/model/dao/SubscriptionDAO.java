package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.Subscription;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {

    private Connection conn;

    public SubscriptionDAO() {
        this.conn = DBConnection.getConnection();
    }

    // CREATE
    public boolean insertSubscription(Subscription sub) {
        String sql = "INSERT INTO Subscriptions (subscription_id, user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getSubscriptionId());
            stmt.setInt(2, sub.getUserId());
            stmt.setDate(3, (java.sql.Date) sub.getStartDate());
            stmt.setDate(4, (java.sql.Date) sub.getEndDate());
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
    public Subscription getSubscriptionById(int id) {
        String sql = "SELECT * FROM Subscriptions WHERE subscription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
    public List<Subscription> getSubscriptionsByUserId(int userId) {
        List<Subscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Subscriptions WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
    public boolean updateSubscription(Subscription sub) {
        String sql = "UPDATE Subscriptions SET user_id = ?, start_date = ?, end_date = ?, amount_paid = ?, "
                   + "payment_status = ?, payment_gateway_ref = ? WHERE subscription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getUserId());
            stmt.setDate(2, (java.sql.Date) sub.getStartDate());
            stmt.setDate(3, (java.sql.Date) sub.getEndDate());
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
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method
    private Subscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        return new Subscription(
                rs.getInt("subscription_id"),
                rs.getInt("user_id"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getDouble("amount_paid"),
                rs.getString("payment_status"),
                rs.getString("payment_gateway_ref")
        );
    }
}
