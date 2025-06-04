package com.enth.ecomusic.model.dao;

import java.sql.*;
import java.util.*;

import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.util.DBConnection;

public class UserDAO {

	// CREATE
	public boolean insertUser(User user) {
		String sql = "INSERT INTO Users (first_name, last_name, bio, username, email, image_url, password, role_id) "
				   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getBio());
			stmt.setString(4, user.getUsername());
			stmt.setString(5, user.getEmail());
			stmt.setString(6, user.getImageUrl());
			stmt.setString(7, user.getPassword());
			stmt.setInt(8, user.getRoleId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// READ
	public User getUserById(int id) {
		String sql = "SELECT * FROM Users WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapUserFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserByUsernameOrEmail(String identifier) {
		String sql = "SELECT * FROM Users WHERE email = ? OR username = ?";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, identifier);
			stmt.setString(2, identifier);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapUserFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM Users";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				users.add(mapUserFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	// UPDATE
	public boolean updateUser(User user) {
		String sql = "UPDATE Users SET first_name = ?, last_name = ?, bio = ?, username = ?, "
				   + "email = ?, image_url = ?, password = ?, role_id = ? WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getBio());
			stmt.setString(4, user.getUsername());
			stmt.setString(5, user.getEmail());
			stmt.setString(6, user.getImageUrl());
			stmt.setString(7, user.getPassword());
			stmt.setInt(8, user.getRoleId());
			stmt.setInt(9, user.getUserId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// DELETE
	public boolean deleteUser(int id) {
		String sql = "DELETE FROM Users WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// HELPER
	private User mapUserFromResultSet(ResultSet rs) throws SQLException {
		User user = new User(
			rs.getInt("user_id"),
			rs.getString("first_name"),
			rs.getString("last_name"),
			rs.getString("username"),
			rs.getString("bio"),
			rs.getString("email"),
			rs.getString("password"),
			rs.getInt("role_id"),
			rs.getString("image_url"),
			rs.getTimestamp("created_at").toLocalDateTime()
		);
		// Lazy load: user.setRole(null) by default
		return user;
	}
}