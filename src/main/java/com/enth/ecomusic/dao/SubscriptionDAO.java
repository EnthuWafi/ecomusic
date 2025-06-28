package com.enth.ecomusic.dao;

import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {

    // CREATE
    public boolean insertSubscription(UserSubscription sub) {
        String sql = "INSERT INTO Subscriptions (subscription_id, user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref, subscription_plan_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getSubscriptionId());
            stmt.setInt(2, sub.getUserId());
            stmt.setDate(3, sub.getStartDate() != null ? Date.valueOf(sub.getStartDate()) : null);
            stmt.setDate(4, sub.getEndDate() != null ? Date.valueOf(sub.getEndDate()) : null);
            stmt.setDouble(5, sub.getAmountPaid());
            stmt.setString(6, sub.getPaymentStatus());
            stmt.setString(7, sub.getPaymentGatewayRef());
            stmt.setInt(8, sub.getSubscriptionPlanId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Create (Transactional)
    public boolean insertSubscription(UserSubscription sub, Connection conn) {
        String sql = "INSERT INTO Subscriptions (subscription_id, user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref, subscription_plan_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sub.getSubscriptionId());
            stmt.setInt(2, sub.getUserId());
            stmt.setDate(3, sub.getStartDate() != null ? Date.valueOf(sub.getStartDate()) : null);
            stmt.setDate(4, sub.getEndDate() != null ? Date.valueOf(sub.getEndDate()) : null);
            stmt.setDouble(5, sub.getAmountPaid());
            stmt.setString(6, sub.getPaymentStatus());
            stmt.setString(7, sub.getPaymentGatewayRef());
            stmt.setInt(8, sub.getSubscriptionPlanId());

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
    
    public UserSubscription getSubscriptionByPaymentGatewayRef(String paymentGatewayRef) {
        String sql = "SELECT * FROM Subscriptions WHERE payment_gateway_ref = ?";
        try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paymentGatewayRef);
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
 
    public UserSubscription getLatestSubscriptionByUserIdAndPlanType(int userId, PlanType plan) {
    	String sql = """
    			SELECT * FROM (
			        SELECT s.* FROM Subscriptions 
			        JOIN SubscriptionPlans sp ON s.subscription_plan_id = sp.subscription_plan_id
			        WHERE user_id = ? AND sp.plan_type = ?
			        ORDER BY created_at DESC
			    ) WHERE ROWNUM = 1
    			""";
    	
    	return DAOUtil.executeSingleQuery(sql, this::mapResultSetToSubscription, userId, plan.getValue());
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

    //update
	public boolean updateSubscriptionEndDate(int subscriptionId, Connection conn) {
		String sql = "UPDATE Subscriptions SET end_date = CURRENT_TIMESTAMP WHERE subscription_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subscriptionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}

	public boolean updateSubscriptionAmountPaid(int subscriptionId, double amountPaid, Connection conn) {
		String sql = "UPDATE Subscriptions SET amount_paid = ? WHERE subscription_id = ?";
		
		return DAOUtil.executeUpdate(sql, amountPaid, subscriptionId);
	}
	
	public int countActiveSubscription() {
		String sql = "SELECT COUNT(*) FROM Subscriptions WHERE end_date = null";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
		
		return count != null ? count : 0;
	}
	
	public int countAllSubscription() {
		String sql = "SELECT COUNT(*) FROM Subscriptions";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
		
		return count != null ? count : 0;
	}
	
	public double getTotalAmountPaid() {
		String sql = "SELECT SUM(amount_paid) FROM Subscriptions";
		
		Double amountPaid = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToDouble);
		
		return amountPaid != null ? amountPaid : 0;
	}
}
