package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;

public class PlaylistMusicDTO {

	private int playlistId;
	private int musicId;
	private LocalDateTime addedAt;
	private int position;
	
	private MusicDetailDTO musicDetail;

	
	
	public PlaylistMusicDTO(int playlistId, int musicId, LocalDateTime addedAt, int position, MusicDetailDTO musicDetail) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
		this.addedAt = addedAt;
		this.position = position;
		this.musicDetail = musicDetail;
	}

	public int getPlaylistId() {
		return playlistId;
	}

	public int getMusicId() {
		return musicId;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public int getPosition() {
		return position;
	}

	public MusicDetailDTO getMusicDetail() {
		return musicDetail;
	}
	
	
}
