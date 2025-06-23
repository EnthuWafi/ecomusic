package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;

public class User{

	private int userId;
	private String firstName;
	private String lastName;
	private String username;
	private String bio;
	private String email;
	private String password;
	private int roleId; 
	private String imageUrl;
	
	private boolean isPremium;
	private boolean isArtist;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	
	private Role role;
	
	public User() {
		super();
	}

	public User(int userId, String firstName, String lastName, String username, String bio, String email,
			String password, int roleId, String imageUrl, boolean isPremium, boolean isArtist, LocalDateTime updatedAt,
			LocalDateTime createdAt) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.roleId = roleId;
		this.imageUrl = imageUrl;
		this.isPremium = isPremium;
		this.isArtist = isArtist;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
	}



	public User(String firstName, String lastName, String username, String bio, String email, String password,
			int roleId, String imageUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.roleId = roleId;
		this.imageUrl = imageUrl;
	}
	
	public User(String firstName, String lastName, String username, String bio, String email, String password,
			int roleId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.roleId = roleId;
	}

	//minimal constructor
	public User(String firstName, String lastName, String username, String bio, String email, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	public boolean isArtist() {
		return isArtist;
	}

	public void setArtist(boolean isArtist) {
		this.isArtist = isArtist;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	

	
}