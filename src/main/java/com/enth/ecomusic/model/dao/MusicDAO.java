package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicDAO {
	private Connection conn;

	public MusicDAO() {
		this.conn = DBConnection.getConnection();
	}

	// CREATE
	public boolean insertMusic(Music music) {
		String sql = "INSERT INTO Music (artist_id, title, genre, description, audio_file_url, image_url, premium_content) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, music.getArtistId());
			stmt.setString(2, music.getTitle());
			stmt.setString(3, music.getGenre());
			stmt.setString(4, music.getDescription());
			stmt.setString(5, music.getAudioFileUrl());
			stmt.setString(6, music.getImageUrl());
			stmt.setInt(7, music.isPremiumContent() ? 1 : 0);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error inserting music: " + e.getMessage());
			return false;
		}
	}

	// READ by ID
	public Music getMusicById(int musicId) {
		String sql = "SELECT * FROM Music WHERE music_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToMusic(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching music by ID: " + e.getMessage());
		}
		return null;
	}

	// READ all
	public List<Music> getAllMusic() {
		List<Music> musicList = new ArrayList<>();
		String sql = "SELECT * FROM Music ORDER BY upload_date DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				musicList.add(mapResultSetToMusic(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all music: " + e.getMessage());
		}

		return musicList;
	}

	// READ all related music
	public List<Music> getAllRelatedMusic(int musicId, int limit) {
		List<Music> musicList = new ArrayList<>();

		String sql = "SELECT * FROM ("
				+ " SELECT m1.* FROM Music m1 "
				+ " JOIN Music m2 ON m1.genre = m2.genre "
				+ " WHERE m1.music_id = ? AND m1.music_id != m2.music_id "
				+ " ORDER BY m1.upload_date DESC"
				+ ") WHERE ROWNUM <= ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
			stmt.setInt(2, limit);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					musicList.add(mapResultSetToMusic(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching related music: " + e.getMessage());
		}

		return musicList;
	}

	// UPDATE
	public boolean updateMusic(Music music) {
		String sql = "UPDATE Music SET artist_id = ?, title = ?, genre = ?, description = ?, "
				+ "audio_file_url = ?, image_url = ?, premium_content = ? WHERE music_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, music.getArtistId());
			stmt.setString(2, music.getTitle());
			stmt.setString(3, music.getGenre());
			stmt.setString(4, music.getDescription());
			stmt.setString(5, music.getAudioFileUrl());
			stmt.setString(6, music.getImageUrl());
			stmt.setInt(7, music.isPremiumContent() ? 1 : 0);
			stmt.setInt(8, music.getMusicId());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating music: " + e.getMessage());
			return false;
		}
	}

	// DELETE
	public boolean deleteMusic(int musicId) {
		String sql = "DELETE FROM Music WHERE music_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting music: " + e.getMessage());
			return false;
		}
	}

	// 🔧 Helper method to map ResultSet to Music object
	private Music mapResultSetToMusic(ResultSet rs) throws SQLException {
		return new Music(rs.getInt("music_id"), rs.getInt("artist_id"), rs.getString("title"), rs.getString("genre"),
				rs.getString("description"), rs.getDate("upload_date"), rs.getString("audio_file_url"),
				rs.getString("image_url"), rs.getInt("premium_content") == 1);
	}
}
