package com.enth.ecomusic.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enth.ecomusic.model.enums.RoleType;

public class UserDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String firstName;
	private String lastName;
	private String username;
	private String bio;
	private String email;
	private int roleId;
	private String roleName;
	private String imageUrl;
	private LocalDateTime createdAt;
	
	
	
	public UserDTO(int userId, String firstName, String lastName, String username, String bio, String email, int roleId,
			String roleName, String imageUrl, LocalDateTime createdAt) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.roleId = roleId;
		this.roleName = roleName;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}
	
	

	public UserDTO() {
		super();
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	//role checking
	public boolean hasRole(RoleType roleType) {
	    return roleType.getValue().equalsIgnoreCase(this.roleName);
	}

	public boolean isAdmin() {
	    return hasRole(RoleType.ADMIN);
	}

	public boolean isSuperAdmin() {
	    return hasRole(RoleType.SUPERADMIN);
	}

	public boolean isArtist() {
	    return hasRole(RoleType.ARTIST);
	}
	
	public boolean isUser() {
	    return hasRole(RoleType.USER);
	}
	
	public boolean isPremiumUser() {
	    return hasRole(RoleType.PREMIUMUSER);
	}
	
}
