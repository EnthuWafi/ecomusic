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
	
	private boolean premium;
	private boolean artist;
	
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	
	


	public UserDTO(int userId, String firstName, String lastName, String username, String bio, String email, int roleId,
			String roleName, String imageUrl, boolean premium, boolean artist, LocalDateTime updatedAt,
			LocalDateTime createdAt) {
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
		this.premium = premium;
		this.artist = artist;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
	}



	public int getUserId() {
		return userId;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public String getUsername() {
		return username;
	}



	public String getBio() {
		return bio;
	}



	public String getEmail() {
		return email;
	}



	public int getRoleId() {
		return roleId;
	}



	public String getRoleName() {
		return roleName;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
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
	    return artist;
	}
	
	public boolean isUser() {
	    return hasRole(RoleType.USER);
	}


	public boolean isPremium() {
		return premium;
	}
	
	
	
}
