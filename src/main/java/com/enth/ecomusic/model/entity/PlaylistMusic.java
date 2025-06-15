package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;

public class PlaylistMusic{

	private int playlistId;
	private int musicId;
	private LocalDateTime addedAt;
	private int position;
	
	private Music music;
	
	

	public PlaylistMusic(int playlistId, int musicId, LocalDateTime addedAt, int position) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
		this.addedAt = addedAt;
		this.position = position;
	}



	public PlaylistMusic(int playlistId, int musicId) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
	}

	public PlaylistMusic() {
		super();
	}


	// Getters & Setters
	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}
	
	

}