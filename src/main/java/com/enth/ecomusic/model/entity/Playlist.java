package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.enth.ecomusic.model.enums.VisibilityType;

public class Playlist{
    private int playlistId;
    private int userId;
    private String name;
    private LocalDateTime createdAt;
    private VisibilityType visibility;
    //experimental
    private List<PlaylistMusic> musicList;

    public Playlist() {
		super();
	}
    
	public Playlist(int playlistId, int userId, String name, LocalDateTime createdAt, VisibilityType visibility) {
		super();
		this.playlistId = playlistId;
		this.userId = userId;
		this.name = name;
		this.createdAt = createdAt;
		this.visibility = visibility;
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
   
    

	public VisibilityType getVisibility() {
		return visibility;
	}

	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}

	public List<PlaylistMusic> getMusicList() {
		return musicList;
	}
	public void setMusicList(List<PlaylistMusic> musicList) {
		this.musicList = musicList;
	}
    
    
}
