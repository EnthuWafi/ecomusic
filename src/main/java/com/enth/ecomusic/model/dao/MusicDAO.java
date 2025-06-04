package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicDAO {
	// CREATE
	public boolean insertMusic(Music music) {
		String sql = "INSERT INTO Music (artist_id, title, genre_id, mood_id, description, audio_file_url, image_url, premium_content) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, music.getArtistId());
			stmt.setString(2, music.getTitle());
			stmt.setInt(3, music.getGenreId());
			stmt.setInt(4, music.getMoodId());
			stmt.setString(5, music.getDescription());
			stmt.setString(6, music.getAudioFileUrl());
			stmt.setString(7, music.getImageUrl());
			stmt.setInt(8, music.isPremiumContent() ? 1 : 0);

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

	// READ by artist id
	public List<Music> getAllMusicByArtistId(int artistId) {
		List<Music> musicList = new ArrayList<>();
		String sql = "SELECT * FROM Music WHERE artist_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, artistId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				musicList.add(mapResultSetToMusic(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all music by artist: " + e.getMessage());
		}

		return musicList;
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

	// READ all paginated
	public List<MusicDetailDTO> getPaginatedMusicWithDetail(int page, int pageSize) {
		String query = """
			    SELECT
		        m.music_id, m.artist_id, m.title, m.description, m.audio_file_url,
		        m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
		        u.username AS artist_username, u.image_url AS artist_image_url,
		        NVL(l.like_count, 0) AS like_count,
		        NVL(s.total_plays, 0) AS total_plays,
		        ROW_NUMBER() OVER (ORDER BY m.upload_date DESC) AS rnum
		    FROM Music m
		    JOIN Users u ON m.artist_id = u.user_id

		    LEFT JOIN (
		        SELECT music_id, COUNT(*) AS like_count
		        FROM Likes
		        GROUP BY music_id
		    ) l ON m.music_id = l.music_id

		    LEFT JOIN (
		        SELECT music_id, SUM(total_plays) AS total_plays
		        FROM UserMusicDailyStats
		        GROUP BY music_id
		    ) s ON m.music_id = s.music_id

			""";

		return DAOUtil.executePaginatedQuery(query, null, page, pageSize, ResultSetMapper::mapToMusicDetailDTO);
	}

	public List<MusicDetailDTO> getPaginatedMusicWithDetailByKeyword(String keyword, int page, int pageSize) {
		String query = """
			    SELECT
			        m.music_id, m.artist_id, m.title, m.description, m.audio_file_url,
			        m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
			        u.username AS artist_username, u.image_url AS artist_image_url,
			        NVL(l.like_count, 0) AS like_count,
			        NVL(s.total_plays, 0) AS total_plays,
			        ROW_NUMBER() OVER (ORDER BY m.upload_date DESC) AS rnum
			    FROM Music m
			    JOIN Users u ON m.artist_id = u.user_id

			    LEFT JOIN (
			        SELECT music_id, COUNT(*) AS like_count
			        FROM Likes
			        GROUP BY music_id
			    ) l ON m.music_id = l.music_id

			    LEFT JOIN (
			        SELECT music_id, SUM(total_plays) AS total_plays
			        FROM UserMusicDailyStats
			        GROUP BY music_id
			    ) s ON m.music_id = s.music_id

			    WHERE m.title LIKE ?
				""";
		return DAOUtil.executePaginatedQuery(query, List.of("%" + keyword + "%"), page, pageSize,
				ResultSetMapper::mapToMusicDetailDTO);
	}

	public List<MusicDetailDTO> getPaginatedMusicWithDetailByArtistId(int artistId, int page, int pageSize) {
		String query = """
			    SELECT
		        m.music_id, m.artist_id, m.title, m.description, m.audio_file_url,
		        m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
		        u.username AS artist_username, u.image_url AS artist_image_url,
		        NVL(l.like_count, 0) AS like_count,
		        NVL(s.total_plays, 0) AS total_plays,
		        ROW_NUMBER() OVER (ORDER BY m.upload_date DESC) AS rnum
		    FROM Music m
		    JOIN Users u ON m.artist_id = u.user_id
		    
		    LEFT JOIN (
		        SELECT music_id, COUNT(*) AS like_count
		        FROM Likes
		        GROUP BY music_id
		    ) l ON m.music_id = l.music_id

		    LEFT JOIN (
		        SELECT music_id, SUM(total_plays) AS total_plays
		        FROM UserMusicDailyStats
		        GROUP BY music_id
		    ) s ON m.music_id = s.music_id

		    WHERE m.artist_id = ?
			""";
		return DAOUtil.executePaginatedQuery(query, List.of(artistId), page, pageSize, ResultSetMapper::mapToMusicDetailDTO);
	}

	// count
	public int countMusicByKeyword(String keyword) {
		String sql = "SELECT COUNT(*) FROM Music WHERE MUSIC.title LIKE ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "%" + keyword + "%");

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			;

		} catch (SQLException e) {
			System.err.println("Error counting music: " + e.getMessage());
		}

		return 0;
	}

	// count
	public int countMusic() {
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

	public int countMusicByArtist(int artistId) {
		String sql = "SELECT COUNT(*) FROM Music WHERE artist_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, artistId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			;

		} catch (SQLException e) {
			System.err.println("Error counting music: " + e.getMessage());
		}

		return 0;
	}

	// UPDATE
	public boolean updateMusic(Music music) {
		String sql = "UPDATE Music SET artist_id = ?, title = ?, genre_id = ?, mood_id = ?, description = ?, "
				+ "audio_file_url = ?, image_url = ?, premium_content = ? WHERE music_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, music.getArtistId());
			stmt.setString(2, music.getTitle());
			stmt.setInt(3, music.getGenreId());
			stmt.setInt(4, music.getMoodId());
			stmt.setString(5, music.getDescription());
			stmt.setString(6, music.getAudioFileUrl());
			stmt.setString(7, music.getImageUrl());
			stmt.setInt(8, music.isPremiumContent() ? 1 : 0);
			stmt.setInt(9, music.getMusicId());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating music: " + e.getMessage());
			return false;
		}
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

	// Helper method to map ResultSet to Music object
	private Music mapResultSetToMusic(ResultSet rs) throws SQLException {
		return new Music(rs.getInt("music_id"), rs.getInt("artist_id"), rs.getString("title"), rs.getInt("genre_id"),
				rs.getInt("mood_id"), rs.getString("description"), rs.getTimestamp("upload_date").toLocalDateTime(),
				rs.getString("audio_file_url"), rs.getString("image_url"), rs.getInt("premium_content") == 1);
	}
}
