package com.enth.ecomusic.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Playlist implements Serializable{
	private static final long serialVersionUID = 1L;
    private int playlistId;
    private int userId;
    private String name;
    private LocalDateTime createdAt;
    //experimental
    private List<PlaylistMusic> musicList;

    public Playlist() {
		super();
	}
	public Playlist(int playlistId, int userId, String name, LocalDateTime createdAt) {
		super();
		this.playlistId = playlistId;
		this.userId = userId;
		this.name = name;
		this.createdAt = createdAt;
	}
	// Getters & Setters
    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
	public List<PlaylistMusic> getMusicList() {
		return musicList;
	}
	public void setMusicList(List<PlaylistMusic> musicList) {
		this.musicList = musicList;
	}
    
    
}
