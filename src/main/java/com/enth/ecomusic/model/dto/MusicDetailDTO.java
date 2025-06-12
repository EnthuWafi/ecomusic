package com.enth.ecomusic.model.dto;

public class MusicDetailDTO {

	/**
	 * 
	 */

	private String artistUsername;
	private String artistImageUrl;

	private MusicDTO music;

	
	// Getters and Setters

	public MusicDetailDTO(String artistUsername, String artistImageUrl, MusicDTO music) {
		super();
		this.artistUsername = artistUsername;
		this.artistImageUrl = artistImageUrl;
		this.music = music;
	}


	public String getArtistUsername() {
		return artistUsername;
	}


	public String getArtistImageUrl() {
		return artistImageUrl;
	}


	public MusicDTO getMusic() {
		return music;
	}
	
	
}
