package com.ecomusic.model;

import java.util.Date;

public class PlaylistMusic {
    private int playlistId;
    private int musicId;
    private Date addedAt;

    // Getters & Setters
    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getMusicId() { return musicId; }
    public void setMusicId(int musicId) { this.musicId = musicId; }

    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }
}