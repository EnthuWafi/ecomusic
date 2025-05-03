package com.enth.ecomusic.model;

import java.util.Date;

public class User {
	private int userId;
	private String firstName;
	private String lastName;
	private String username;
	private String bio;
	private String email;
	private String password;
	private String userType;
	private String imageUrl;
	private Date createdAt;

	public User() {
		super();
	}

	public User(int userId, String firstName, String lastName, String username, String bio, String email,
			String password, String userType, String imageUrl, Date createdAt) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}

	public User(String firstName, String lastName, String username, String bio, String email, String password,
			String userType, String imageUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.imageUrl = imageUrl;
	}
	
	

	public User(String firstName, String lastName, String username, String bio, String email, String password,
			String userType) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.bio = bio;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.imageUrl = "/WEB-INF/assets/images/default.jpg"; // default profile
	}

	// Getters & Setters

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

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}