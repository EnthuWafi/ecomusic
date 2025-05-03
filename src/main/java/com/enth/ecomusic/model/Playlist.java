package com.enth.ecomusic.model;

import java.util.Date;
import java.util.List;

public class Playlist {
    private int playlistId;
    private int userId;
    private String name;
    private Date createdAt;
    //experimental
    private List<PlaylistMusic> musicList;

    
    public Playlist(int playlistId, int userId, String name, Date createdAt) {
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

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
	public List<PlaylistMusic> getMusicList() {
		return musicList;
	}
	public void setMusicList(List<PlaylistMusic> musicList) {
		this.musicList = musicList;
	}
    
    
}
