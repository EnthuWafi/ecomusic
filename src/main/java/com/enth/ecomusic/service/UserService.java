package com.enth.ecomusic.service;

import java.util.List;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.model.Role;
import com.enth.ecomusic.model.RoleType;
import com.enth.ecomusic.model.dao.UserDAO;

public class UserService {

	private final UserDAO userDAO;
	private final RoleCacheService roleCacheService;
	
	public UserService() {
		this.userDAO = new UserDAO();
		this.roleCacheService = new RoleCacheService();
	}
	
	public UserService(RoleCacheService roleCacheService) {
		this.userDAO = new UserDAO();
		this.roleCacheService = roleCacheService;
	}

	// Register new user
	public boolean registerUser(User user) {
		return userDAO.insertUser(user);
	}
	
	public boolean registerUserWithRoleName(User user, RoleType roleType) {
		Role role = roleCacheService.getByType(roleType);
		if (role != null) {
			user.setRoleId(role.getRoleId());
			return userDAO.insertUser(user);
		}
		return false;
	}

	// Fetch with lazy-loaded Role
	public User getUserById(int userId) {
		User user = userDAO.getUserById(userId);
		if (user != null) {
			user.setRole(fetchRole(user));
		}
		return user;
	}

	public User getUserByUsernameOrEmail(String identifier) {
		User user = userDAO.getUserByUsernameOrEmail(identifier);
		if (user != null) {
			user.setRole(fetchRole(user));
		}
		return user;
	}

	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		return users.stream()
			.peek(user -> user.setRole(fetchRole(user)))
			.collect(Collectors.toList());
	}

	public boolean updateUser(User user) {
		return userDAO.updateUser(user);
	}
	
	public boolean updateUserWithRoleName(User user, RoleType roleType) {
		Role role = roleCacheService.getByType(roleType);
		if (role != null) {
			user.setRoleId(role.getRoleId());
			return userDAO.updateUser(user);
		}
		return false;
	}


	public boolean deleteUser(int userId) {
		return userDAO.deleteUser(userId);
	}
	

	// Lazy load role
	private Role fetchRole(User user) {
		try {
			int roleId = user.getRoleId();
			return roleCacheService.getById(roleId);
		} catch (NumberFormatException e) {
			System.err.println("Invalid role_id format for user: " + user.getUsername());
			return null;
		}
	}
}