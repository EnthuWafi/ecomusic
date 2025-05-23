package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicDAO {
	// CREATE
	public boolean insertMusic(Music music) {
		String sql = "INSERT INTO Music (artist_id, title, genre, description, audio_file_url, image_url, premium_content) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				musicList.add(mapResultSetToMusic(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all music: " + e.getMessage());
		}

		return musicList;
	}

	// READ all
	public List<Music> getPaginatedMusic(int page, int pageSize) {
		List<Music> musicList = new ArrayList<>();
		String sql = "SELECT * FROM (" + "SELECT MUSIC.*, ROW_NUMBER() OVER (ORDER BY MUSIC.music_id) AS rnum "
				+ "FROM MUSIC)" + "WHERE rnum > ? AND rnum <= ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {
			int lower = (page - 1) * pageSize;
			int upper = lower + pageSize;
			stmt.setInt(1, lower);
			stmt.setInt(2, upper);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					musicList.add(mapResultSetToMusic(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching paginated music: " + e.getMessage());
		}

		return musicList;
	}

	// READ all related music
	public List<Music> getAllRelatedMusic(int musicId) {
		List<Music> musicList = new ArrayList<>();

		String sql = "SELECT * FROM (" + " SELECT m1.* FROM Music m1 " + " JOIN Music m2 ON m1.genre = m2.genre "
				+ " WHERE m1.music_id = ? AND m1.music_id != m2.music_id " + " ORDER BY m1.upload_date DESC"
				+ ") WHERE ROWNUM <= 5";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
//			stmt.setInt(2, limit);

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

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

	// count
	public int countProducts() {
		String sql = "SELECT COUNT(*) FROM Music";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error counting music: " + e.getMessage());
		}

		return 0;
	}

	// DELETE
	public boolean deleteMusic(int musicId) {
		String sql = "DELETE FROM Music WHERE music_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting music: " + e.getMessage());
			return false;
		}
	}

	public List<Music> searchMusicByTitleOrArtist(String keyword) {
		List<Music> results = new ArrayList<>();
		String sql = "SELECT m.* FROM Music m " + "INNER JOIN User u ON u.user_id = m.artist_id "
				+ "WHERE LOWER(m.title) LIKE ? " + "OR LOWER(u.username) LIKE ? " + "ORDER BY m.upload_date DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			String likeQuery = "%" + keyword.toLowerCase() + "%";
			stmt.setString(1, likeQuery);
			stmt.setString(2, likeQuery);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					results.add(mapResultSetToMusic(rs));
				}
			}

		} catch (SQLException e) {
			System.err.println("Error searching music: " + e.getMessage());
		}

		return results;
	}

	// Helper method to map ResultSet to Music object
	private Music mapResultSetToMusic(ResultSet rs) throws SQLException {
		return new Music(rs.getInt("music_id"), rs.getInt("artist_id"), rs.getString("title"), rs.getString("genre"),
				rs.getString("description"), rs.getDate("upload_date"), rs.getString("audio_file_url"),
				rs.getString("image_url"), rs.getInt("premium_content") == 1);
	}
}
