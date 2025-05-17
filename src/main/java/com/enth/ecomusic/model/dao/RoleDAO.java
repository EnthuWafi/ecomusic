package com.enth.ecomusic.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.Role;
import com.enth.ecomusic.util.DBConnection;

public class RoleDAO {
	public Role getRoleById(int id) {
		String sql = "SELECT * FROM Roles WHERE role_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Role(rs.getInt("role_id"), rs.getString("role_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Role getRoleByName(String name) {
		String sql = "SELECT * FROM Roles WHERE role_name = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Role(rs.getInt("role_id"), rs.getString("role_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Role> getAllRoles() {
		List<Role> roles = new ArrayList<>();
		String sql = "SELECT * FROM Roles";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				roles.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roles;
	}
}
