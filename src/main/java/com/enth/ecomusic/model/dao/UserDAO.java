package com.enth.ecomusic.model.dao;

import java.sql.*;
import java.util.*;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.util.DBConnection;

public class UserDAO {

	private Connection conn;

	public UserDAO() {
		this.conn = DBConnection.getConnection();
	}

	// CREATE
	public boolean insertUser(User user) {
		String sql = "INSERT INTO Users (first_name, last_name, bio, username, email, image_url, password, user_type) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getBio());
			stmt.setString(4, user.getUsername());
			stmt.setString(5, user.getEmail());
			stmt.setString(6, user.getImageUrl());
			stmt.setString(7, user.getPassword());
			stmt.setString(8, user.getUserType());
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// READ (get by ID)
	public User getUserById(int id) {
		String sql = "SELECT * FROM Users WHERE user_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("bio"), rs.getString("username"), rs.getString("email"), rs.getString("password"),
						rs.getString("user_type"), rs.getString("image_url"), rs.getDate("created_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// READ ALL
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM Users";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				users.add(new User(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("bio"), rs.getString("username"), rs.getString("email"), rs.getString("password"),
						rs.getString("user_type"), rs.getString("image_url"), rs.getDate("created_at")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	// READ (get by username or email)
	public User getUserByUsernameOrEmail(String email) {
		String sql = "SELECT * FROM Users WHERE email = ? OR username = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("bio"), rs.getString("username"), rs.getString("email"), rs.getString("password"),
						rs.getString("user_type"), rs.getString("image_url"), rs.getDate("created_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// UPDATE
	public boolean updateUser(User user) {
		String sql = "UPDATE Users SET first_name = ?, last_name = ?, bio = ?, "
				+ "username = ?, email = ?, image_url = ?, password = ?, user_type = ? "
				+ "WHERE user_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getBio());
			stmt.setString(4, user.getUsername());
			stmt.setString(5, user.getEmail());
			stmt.setString(6, user.getImageUrl());
			stmt.setString(7, user.getPassword());
			stmt.setString(8, user.getUserType());
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
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
