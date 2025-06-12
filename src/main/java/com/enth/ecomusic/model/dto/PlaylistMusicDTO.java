package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;

public class PlaylistMusicDTO {

	private int playlistId;
	private int musicId;
	private LocalDateTime addedAt;
	private int position;
	private String artistUsername;
	private String artistImageUrl;
	
	private MusicDTO music;

	public PlaylistMusicDTO(int playlistId, int musicId, LocalDateTime addedAt, int position, String artistUsername,
			String artistImageUrl, MusicDTO music) {
		super();
		this.playlistId = playlistId;
		this.musicId = musicId;
		this.addedAt = addedAt;
		this.position = position;
		this.artistUsername = artistUsername;
		this.artistImageUrl = artistImageUrl;
		this.music = music;
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

	public String getArtistUsername() {
		return artistUsername;
	}

	public String getArtistImageUrl() {
		return artistImageUrl;
	}

	public MusicDTO getMusic() {
		return music;
	}

	
	
}
