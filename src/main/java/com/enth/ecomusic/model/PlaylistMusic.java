package com.enth.ecomusic.model;

import java.util.Date;

public class PlaylistMusic {
	private int playlistId;
	private int musicId;
	private Date addedAt;
	// experimental
	private Music music;

	public PlaylistMusic(int playlistId, int musicId, Date addedAt) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
		this.addedAt = addedAt;
	}

	public PlaylistMusic(int playlistId, int musicId) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
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

	public Date getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(Date addedAt) {
		this.addedAt = addedAt;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

}