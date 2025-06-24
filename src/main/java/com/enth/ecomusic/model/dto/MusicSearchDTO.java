package com.enth.ecomusic.model.dto;

public class MusicSearchDTO {
	private int musicId;
	private String title;

	

	public MusicSearchDTO(int musicId, String title) {
		super();
		this.musicId = musicId;
		this.title = title;
	}



	public int getMusicId() {
		return musicId;
	}



	public String getTitle() {
		return title;
	}
	
}
