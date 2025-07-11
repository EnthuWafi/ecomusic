package com.enth.ecomusic.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.NumberFormatUtil;

public class PlayHistoryDTO {

	private int playId;
    private int userId;
    private int musicId;
    private LocalDateTime playedAt;
    private long listenDuration;
    private boolean wasSkipped;
    
    private MusicDTO music;

	public PlayHistoryDTO(int playId, int userId, int musicId, LocalDateTime playedAt, long listenDuration,
			boolean wasSkipped, MusicDTO music) {
		super();
		this.playId = playId;
		this.userId = userId;
		this.musicId = musicId;
		this.playedAt = playedAt;
		this.listenDuration = listenDuration;
		this.wasSkipped = wasSkipped;
		this.music = music;
	}

	public int getPlayId() {
		return playId;
	}

	public int getUserId() {
		return userId;
	}

	public int getMusicId() {
		return musicId;
	}

	public LocalDateTime getPlayedAt() {
		return playedAt;
	}
	
	public Date getPlayedAtDate() {
		return CommonUtil.toDate(playedAt);
	}

	public long getListenDuration() {
		return listenDuration;
	}
	
	public String getListenDurationString() {
		return NumberFormatUtil.formatDuration(listenDuration);
	}

	public boolean isWasSkipped() {
		return wasSkipped;
	}

	public MusicDTO getMusic() {
		return music;
	}
    
    
}
