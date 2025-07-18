package com.enth.ecomusic.model.entity;

public class Genre {
    /**
	 * 
	 */
	private int genreId;
    private String name;

    public Genre() {
		super();
	}
	public Genre(int genreId, String name) {
		super();
		this.genreId = genreId;
		this.name = name;
	}
	// Getters and Setters
    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
