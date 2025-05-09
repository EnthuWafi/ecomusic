package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.PlaylistMusic;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMusicDAO {
	// ADD music to playlist
	public boolean addMusicToPlaylist(PlaylistMusic playlistMusic) {
		String sql = "INSERT INTO PlaylistMusic (playlist_id, music_id) VALUES (?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistMusic.getPlaylistId());
			stmt.setInt(2, playlistMusic.getMusicId());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error adding music to playlist: " + e.getMessage());
			return false;
		}
	}

	// GET all music in a playlist
	public List<PlaylistMusic> getMusicByPlaylistId(int playlistId) {
		List<PlaylistMusic> list = new ArrayList<>();

		String sql = "SELECT pm.*, m.* FROM PlaylistMusic pm " + "JOIN Music m ON pm.music_id = m.music_id "
				+ "WHERE pm.playlist_id = ? ORDER BY pm.added_at";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					PlaylistMusic pm = new PlaylistMusic(rs.getInt("playlist_id"), rs.getInt("music_id"),
							rs.getDate("added_at"));

					Music music = new Music(rs.getInt("music_id"), rs.getInt("artist_id"), rs.getString("title"),
							rs.getString("genre"), rs.getString("description"), rs.getDate("upload_date"),
							rs.getString("audio_file_url"), rs.getString("image_url"),
							rs.getInt("premium_content") != 0);

					pm.setMusic(music);

					list.add(pm);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving playlist music: " + e.getMessage());
		}

		return list;
	}

	// REMOVE one music entry from playlist
	public boolean removeMusicFromPlaylist(int playlistId, int musicId) {
		String sql = "DELETE FROM PlaylistMusic WHERE playlist_id = ? AND music_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistId);
			stmt.setInt(2, musicId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error removing music from playlist: " + e.getMessage());
			return false;
		}
	}

	// CLEAR playlist
	public boolean clearPlaylist(int playlistId) {
		String sql = "DELETE FROM PlaylistMusic WHERE playlist_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error clearing playlist: " + e.getMessage());
			return false;
		}
	}
}
