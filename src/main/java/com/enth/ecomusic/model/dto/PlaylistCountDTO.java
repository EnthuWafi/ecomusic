package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.util.CommonUtil;

public class PlaylistCountDTO {
	private int playlistId;
	private int userId;
	private String name;
	private LocalDateTime createdAt;
	private VisibilityType visibility;
	private int playlistCount;
	// experimental
	private List<PlaylistMusicDTO> musicList;
	public PlaylistCountDTO(int playlistId, int userId, String name, LocalDateTime createdAt, VisibilityType visibility,
			int playlistCount, List<PlaylistMusicDTO> musicList) {
		super();
		this.playlistId = playlistId;
		this.userId = userId;
		this.name = name;
		this.createdAt = createdAt;
		this.visibility = visibility;
		this.playlistCount = playlistCount;
		this.musicList = musicList;
	}
	public int getPlaylistId() {
		return playlistId;
	}
	public int getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public Date getCreatedAtDate() {
		return CommonUtil.toDate(createdAt);
	}
	public VisibilityType getVisibility() {
		return visibility;
	}
	public int getPlaylistCount() {
		return playlistCount;
	}
	public List<PlaylistMusicDTO> getMusicList() {
		return musicList;
	}
	
	
}
