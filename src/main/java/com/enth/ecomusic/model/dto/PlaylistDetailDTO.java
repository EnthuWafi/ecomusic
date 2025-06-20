package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.util.CommonUtil;

public class PlaylistDetailDTO {
	private int playlistId;
	private int userId;
	private String name;
	private LocalDateTime createdAt;
	private VisibilityType visibility;
	
	private String ownerName;
	private String ownerImageUrl;
	
	private List<PlaylistMusicDTO> musicList;

	public PlaylistDetailDTO(int playlistId, int userId, String name, LocalDateTime createdAt, VisibilityType visibility,
			String ownerName, String ownerImageUrl, List<PlaylistMusicDTO> musicList) {
		super();
		this.playlistId = playlistId;
		this.userId = userId;
		this.name = name;
		this.createdAt = createdAt;
		this.visibility = visibility;
		this.ownerName = ownerName;
		this.ownerImageUrl = ownerImageUrl;
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

	public List<PlaylistMusicDTO> getMusicList() {
		return musicList;
	}
	
	public boolean isPublic() {
		return visibility == VisibilityType.PUBLIC;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getOwnerImageUrl() {
		return ownerImageUrl;
	}
	
	

}
