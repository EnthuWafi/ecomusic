package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.enth.ecomusic.model.enums.VisibilityType;

public class PlaylistDTO {
	 private int playlistId;
	    private int userId;
	    private String name;
	    private LocalDateTime createdAt;
	    private VisibilityType visibility;
	    //experimental
	    private List<PlaylistMusicDTO> musicList;
		public PlaylistDTO(int playlistId, int userId, String name, LocalDateTime createdAt, VisibilityType visibility,
				List<PlaylistMusicDTO> musicList) {
			super();
			this.playlistId = playlistId;
			this.userId = userId;
			this.name = name;
			this.createdAt = createdAt;
			this.visibility = visibility;
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
		public VisibilityType getVisibility() {
			return visibility;
		}
		public List<PlaylistMusicDTO> getMusicList() {
			return musicList;
		}
	    
	    
		
}
