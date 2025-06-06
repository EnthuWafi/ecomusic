package com.enth.ecomusic.model.dto;

import java.io.Serializable;

public class MusicDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String artistUsername;
	private String artistImageUrl;

	private int likes;
	private int views;

	private MusicDTO music;

	
	// Getters and Setters

	public MusicDetailDTO(String artistUsername, String artistImageUrl, int likes, int views, MusicDTO music) {
		super();
		this.artistUsername = artistUsername;
		this.artistImageUrl = artistImageUrl;
		this.likes = likes;
		this.views = views;
		this.music = music;
	}
	
	public MusicDetailDTO() {
		super();
	}

	public MusicDTO getMusic() {
		return music;
	}

	public void setMusic(MusicDTO music) {
		this.music = music;
	}

	
	public String getArtistUsername() {
		return artistUsername;
	}

	public void setArtistUsername(String artistUsername) {
		this.artistUsername = artistUsername;
	}

	public String getArtistImageUrl() {
		return artistImageUrl;
	}

	public void setArtistImageUrl(String artistImageUrl) {
		this.artistImageUrl = artistImageUrl;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

}
