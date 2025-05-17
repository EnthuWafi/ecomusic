package com.enth.ecomusic.service;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.Playlist;
import com.enth.ecomusic.model.PlaylistMusic;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.model.dao.PlaylistDAO;
import com.enth.ecomusic.model.dao.PlaylistMusicDAO;

import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private PlaylistDAO playlistDAO;
    private PlaylistMusicDAO playlistMusicDAO;
    private MusicDAO musicDAO;

    public PlaylistService() {
        this.playlistDAO = new PlaylistDAO();
        this.playlistMusicDAO = new PlaylistMusicDAO();
        this.musicDAO = new MusicDAO();
    }

    // Helper method to load music for a playlist
    private void loadMusicForPlaylist(Playlist playlist) {
        List<PlaylistMusic> playlistMusicList = playlistMusicDAO.getPlaylistMusicByPlaylistId(playlist.getPlaylistId());
        
        for (PlaylistMusic pm : playlistMusicList) {
            Music music = musicDAO.getMusicById(pm.getMusicId());
            if (music != null) {
                pm.setMusic(music);
            }
        }
        
        playlist.setMusicList(playlistMusicList); 
    }

    // Method to get a playlist with music by playlistId
    public Playlist getPlaylistWithMusicByPlaylistId(int playlistId) {
        Playlist playlist = playlistDAO.getPlaylistById(playlistId);
        if (playlist != null) {
            loadMusicForPlaylist(playlist);
        }
        return playlist;
    }

    // Method to get all playlists for a user with associated music
    public List<Playlist> getUserPlaylistsWithMusicByUserId(int userId) {
        List<Playlist> playlists = playlistDAO.getPlaylistsByUserId(userId);
        for (Playlist playlist : playlists) {
            loadMusicForPlaylist(playlist); //Passes each playlist into loadMusicForPlaylist
        }
        return playlists;
    }

}
