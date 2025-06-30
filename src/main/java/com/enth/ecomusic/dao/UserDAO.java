package com.enth.ecomusic.dao;

import java.sql.*;
import java.util.*;

import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
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
	
	public List<User> getAllUserWithOffsetLimit(int offset, int limit) {
		String query = """
				SELECT *
		        FROM (
		            SELECT u.*, ROW_NUMBER() OVER (ORDER BY u.created_at DESC) AS rnum
		            FROM Users u
		        )
		        WHERE rnum BETWEEN ? AND ?
				""";

		List<Object> params = new ArrayList<>();
		
		params.add(offset + 1);
		params.add(offset + limit);

		return DAOUtil.executeQuery(query, this::mapUserFromResultSet, params.toArray());
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
	
	public boolean updateUserRole(int userId, int roleId) {
		String sql = "UPDATE Users SET role_id = ? WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection(); 
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, roleId);
			stmt.setInt(2, userId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUserSetPremium(int userId, boolean premium, Connection conn) {
		String sql = "UPDATE Users SET is_premium = ? WHERE user_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, (premium ? 1 : 0));
			stmt.setInt(2, userId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUserSetArtist(int userId, boolean artist, Connection conn) {
		String sql = "UPDATE Users SET is_artist = ? WHERE user_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, (artist ? 1 : 0));
			stmt.setInt(2, userId);
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
	
	public int countAllUser() {
		String sql = "SELECT COUNT(*) FROM Users";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
		
		return count != null ? count : 0;
	}
	
	public int countUserByRoleId(int roleId) {
		String sql = "SELECT COUNT(*) FROM Users WHERE role_id = ? AND (is_artist = 0 AND is_premium = 0)";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, roleId);
		
		return count != null ? count : 0;
	}
	
	public int countAllArtist() {
		String sql = "SELECT COUNT(*) FROM Users WHERE is_artist = 1";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
		
		return count != null ? count : 0;
	}
	

	public int countAllPremium() {
		String sql = "SELECT COUNT(*) FROM Users WHERE is_premium = 1";
		
		Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
		
		return count != null ? count : 0;
	}
	
	public int countRegisteredUserToday() {
	    String sql = "SELECT COUNT(*) FROM Users WHERE TRUNC(created_at) = TRUNC(SYSDATE)";
	    Integer count = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);
	    return count != null ? count : 0;
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
			(rs.getInt("is_premium") == 1 ? true : false),
			(rs.getInt("is_artist") == 1 ? true : false),
			rs.getTimestamp("updated_at").toLocalDateTime(),
			rs.getTimestamp("created_at").toLocalDateTime()
		);
		return user;
	}
}