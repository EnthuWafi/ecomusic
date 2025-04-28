package com.enth.ecomusic.model.dao;

import java.sql.*;
import java.util.*;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.util.DBConnection;

public class MusicDAO {
	private Connection conn;
	
	public MusicDAO() {
		this.conn = DBConnection.getConnection();
	}
	
	//create
	public boolean insertMusic(Music music) {
		String sql = "INSERT INTO MUSIC (ARTIST_ID, TITLE, GENRE, DESCRIPTION, AUDIO_FILE_URL, PREMIUM_CONTENT) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			//stmt.setString(1, user.getName());
			stmt.setInt(1, music.getArtistId());
			stmt.setString(2, music.getTitle());
	        stmt.setString(3, music.getGenre());
	        stmt.setString(4, music.getDescription());
	        stmt.setString(5, music.getAudioFileUrl());
	        stmt.setInt(6, music.isPremiumContent() ? 1 : 0);
			
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//get 
	public Music getMusicById(int musicId) {
	    Music music = null;
	    String sql = "SELECT * FROM MUSIC WHERE MUSIC_ID = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, musicId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                music = new Music(
	                    rs.getInt("MUSIC_ID"),
	                    rs.getInt("ARTIST_ID"),
	                    rs.getString("TITLE"),
	                    rs.getString("GENRE"),
	                    rs.getString("DESCRIPTION"),
	                    rs.getDate("UPLOAD_DATE"),
	                    rs.getString("AUDIO_FILE_URL"),
	                    rs.getInt("PREMIUM_CONTENT") == 1 // Convert int to boolean
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return music;
	}
	
	//get all
	public List<Music> getAllMusic() {
	    List<Music> musicList = new ArrayList<>();
	    String sql = "SELECT * FROM MUSIC";

	    try (PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Music music = new Music(
	                rs.getInt("MUSIC_ID"),
	                rs.getInt("ARTIST_ID"),
	                rs.getString("TITLE"),
	                rs.getString("GENRE"),
	                rs.getString("DESCRIPTION"),
	                rs.getDate("UPLOAD_DATE"),
	                rs.getString("AUDIO_FILE_URL"),
	                (rs.getInt("PREMIUM_CONTENT") == 1) // Convert int to boolean
	            );
	            musicList.add(music);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return musicList;
	}
}
