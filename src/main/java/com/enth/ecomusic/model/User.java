package com.enth.ecomusic.model;

import java.util.Date;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String userType;
    private Date createdAt;

    
    public User() {
    	super();
	}
    
	public User(int userId, String name, String email, String password, String userType, Date createdAt) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.createdAt = createdAt;
	}
	
	
	public User(String name, String email, String password, String userType) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.userType = userType;
	}
	
	// Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}