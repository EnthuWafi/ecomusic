package com.enth.ecomusic.model.entity;

import java.io.Serializable;

public class Mood{
    /**
	 * 
	 */
	private int moodId;
    private String name;

    public Mood() {
    	super();
    }
    
    
    public Mood(int moodId, String name) {
		super();
		this.moodId = moodId;
		this.name = name;
	}


	// Getters and Setters
    public int getMoodId() { return moodId; }
    public void setMoodId(int moodId) { this.moodId = moodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
