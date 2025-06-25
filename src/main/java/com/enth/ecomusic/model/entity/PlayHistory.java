package com.enth.ecomusic.model.entity;

import java.time.LocalDateTime;

public class PlayHistory {

	private int playId;
	private Integer userId;
	private int musicId;
	private LocalDateTime playedAt;
	private long listenDuration;
	private boolean wasSkipped;

	private Music music;

	public PlayHistory(int playId, int userId, int musicId, LocalDateTime playedAt, long listenDuration,
			boolean wasSkipped) {
		super();
		this.playId = playId;
		this.userId = userId;
		this.musicId = musicId;
		this.playedAt = playedAt;
		this.listenDuration = listenDuration;
		this.wasSkipped = wasSkipped;
	}

	public PlayHistory() {
		super();
	}

	public int getPlayId() {
		return playId;
	}

	public void setPlayId(int playId) {
		this.playId = playId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public LocalDateTime getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(LocalDateTime playedAt) {
		this.playedAt = playedAt;
	}

	public long getListenDuration() {
		return listenDuration;
	}

	public void setListenDuration(long listenDuration) {
		this.listenDuration = listenDuration;
	}

	public boolean isWasSkipped() {
		return wasSkipped;
	}

	public void setWasSkipped(boolean wasSkipped) {
		this.wasSkipped = wasSkipped;
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

}
