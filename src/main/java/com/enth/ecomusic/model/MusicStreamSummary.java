package com.enth.ecomusic.model;

import java.util.Date;

public class MusicStreamSummary {
	private int musicStreamSummaryId;
	private int musicId;
	private Date streamDate;
	private int streamCount;

	public MusicStreamSummary(int musicStreamSummaryId, int musicId, Date streamDate, int streamCount) {
		super();
		this.musicStreamSummaryId = musicStreamSummaryId;
		this.musicId = musicId;
		this.streamDate = streamDate;
		this.streamCount = streamCount;
	}

	public MusicStreamSummary(int musicId, Date streamDate, int streamCount) {
		super();
		this.musicId = musicId;
		this.streamDate = streamDate;
		this.streamCount = streamCount;
	}

	public int getMusicStreamSummaryId() {
		return musicStreamSummaryId;
	}

	public void setMusicStreamSummaryId(int musicStreamSummaryId) {
		this.musicStreamSummaryId = musicStreamSummaryId;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public Date getStreamDate() {
		return streamDate;
	}

	public void setStreamDate(Date streamDate) {
		this.streamDate = streamDate;
	}

	public int getStreamCount() {
		return streamCount;
	}

	public void setStreamCount(int streamCount) {
		this.streamCount = streamCount;
	}

}
