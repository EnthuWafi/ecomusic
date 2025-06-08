package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MusicDAO {
	private static final double TEXT_SCORE_WEIGHT = 0.5;
	private static final double LIKE_COUNT_WEIGHT = 0.3;
	private static final double TOTAL_PLAYS_WEIGHT = 0.15;
	private static final double FRESHNESS_WEIGHT = -0.05;

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
		String sql = "SELECT * FROM Music";

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

	public List<Music> getAllValidMusic() {
		List<Music> musicList = new ArrayList<>();
		String sql = "SELECT m.* FROM Music m WHERE m.visibility = 'public'";

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
	public List<MusicDetailDTO> getRelevantPaginatedMusicWithDetail(int page, int pageSize) {
		String query = """
				WITH RankedData AS (

				   SELECT
				       m.music_id, m.artist_id, m.title, m.audio_file_url,
				       m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
				       u.username AS artist_username, u.image_url AS artist_image_url,
				       m.like_count_cache, m.total_plays_cache, m.visibility,
				       g.name AS genre_name, mo.name As mood_name,
				       (
				        m.like_count_cache * %.2f +
				        m.total_plays_cache * %.2f +
				        (SYSDATE - m.upload_date) * %.2f
				    ) AS relevance_score
				   FROM Music m
				   JOIN Users u ON m.artist_id = u.user_id
				   JOIN genres g ON m.genre_id = g.genre_id
				   JOIN moods mo ON m.mood_id = mo.mood_id
				   WHERE m.visiblility = 'public'

				   )
				SELECT
				    r.*,
				    ROW_NUMBER() OVER (ORDER BY r.relevance_score DESC) AS rnum
				FROM
				    RankedData r
				""";

		query = query.formatted(LIKE_COUNT_WEIGHT, TOTAL_PLAYS_WEIGHT, FRESHNESS_WEIGHT);

		return DAOUtil.executePaginatedQuery(query, ResultSetMapper::mapToMusicDetailDTO, page, pageSize);
	}

	public List<MusicDetailDTO> getRelevantPaginatedMusicWithDetailByKeyword(String keyword, List<Integer> genreIds,
			List<Integer> moodIds, int page, int pageSize) {
		String genreInClause = genreIds.isEmpty() ? "null"
				: genreIds.stream().map(g -> "?").collect(Collectors.joining(", "));
		String moodInClause = moodIds.isEmpty() ? "null"
				: moodIds.stream().map(g -> "?").collect(Collectors.joining(", "));

		String query = """
				WITH RankedData AS (

				   SELECT
				       m.music_id, m.artist_id, m.title, m.audio_file_url,
				       m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
				       u.username AS artist_username, u.image_url AS artist_image_url,
				       m.like_count_cache, m.total_plays_cache, m.visibility,
				       g.name AS genre_name, mo.name As mood_name,
				       (
				        SCORE(1) * %.2f +
				        m.like_count_cache * %.2f +
				        m.total_plays_cache * %.2f +
				        (SYSDATE - m.upload_date) * %.2f
				    	) AS relevance_score
				   FROM Music m
				   JOIN Users u ON m.artist_id = u.user_id
				   JOIN genres g ON m.genre_id = g.genre_id
				   JOIN moods mo ON m.mood_id = mo.mood_id

				   WHERE m.visibility = 'public'
				   AND (CONTAINS(m.title, ?, 1) > 0)
				   AND (? IS NULL OR m.genre_id IN (%s))
				   AND (? IS NULL OR m.mood_id IN (%s))

				)
				SELECT
				    r.*,
				    ROW_NUMBER() OVER (ORDER BY r.relevance_score DESC) AS rnum
				FROM
				    RankedData r
				""";

		query = query.formatted(TEXT_SCORE_WEIGHT, LIKE_COUNT_WEIGHT, TOTAL_PLAYS_WEIGHT, FRESHNESS_WEIGHT,
				genreInClause, moodInClause);

		List<Object> params = new ArrayList<>();

		params.add(DAOUtil.buildOracleTextQuery(keyword));

		params.add(genreIds.isEmpty() ? null : 1);
		params.addAll(genreIds);

		params.add(moodIds.isEmpty() ? null : 1);
		params.addAll(moodIds);

		return DAOUtil.executePaginatedQuery(query, ResultSetMapper::mapToMusicDetailDTO, page, pageSize,
				params.toArray());
	}

	public List<MusicDTO> getPaginatedMusicByArtistId(int artistId, int page, int pageSize) {
		String query = """
				   SELECT
				       m.music_id, m.artist_id, m.title, m.audio_file_url,
				       m.image_url, m.premium_content, m.genre_id, m.mood_id, m.upload_date,
				       m.like_count_cache, m.total_plays_cache, m.visibility,
				       g.name AS genre_name, mo.name As mood_name,
				       ROW_NUMBER() OVER (ORDER BY m.upload_date DESC) AS rnum
				   FROM Music m
				   JOIN genres g ON m.genre_id = g.genre_id
				   JOIN moods mo ON m.mood_id = mo.mood_id

				   WHERE  m.visibility = 'public' AND m.artist_id = ?
				""";
		return DAOUtil.executePaginatedQuery(query, ResultSetMapper::mapToMusicDTO, page, pageSize, artistId);
	}

	// count
	public int countMusicByKeyword(String keyword, List<Integer> genreIds, List<Integer> moodIds) {
		String genreInClause = genreIds.isEmpty() ? "null"
				: genreIds.stream().map(g -> "?").collect(Collectors.joining(", "));
		String moodInClause = moodIds.isEmpty() ? "null"
				: moodIds.stream().map(g -> "?").collect(Collectors.joining(", "));

		String sql = """
				SELECT COUNT(*) FROM Music m
				JOIN Users u ON m.artist_id = u.user_id
				JOIN roles r ON u.role_id = r.role_id AND r.role_name = 'artist'
				WHERE (CONTAINS(m.title, ?, 1) > 0)
				AND (? IS NULL OR m.genre_id IN (%s))
				AND (? IS NULL OR m.mood_id IN (%s))
				""";

		sql = sql.formatted(genreInClause, moodInClause);

		List<Object> params = new ArrayList<>();

		params.add(DAOUtil.buildOracleTextQuery(keyword));

		params.add(genreIds.isEmpty() ? null : 1);
		params.addAll(genreIds);

		params.add(moodIds.isEmpty() ? null : 1);
		params.addAll(moodIds);

		Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, params.toArray());

		return (result != null) ? result : 0;
	}

	// count
	public int countMusic() {
		String sql = "SELECT COUNT(*) FROM Music m " + "JOIN Users u ON m.artist_id = u.user_id "
				+ "JOIN roles r ON u.role_id = r.role_id AND r.role_name = 'artist'";

		Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt);

		return (result != null) ? result : 0;
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
		int musicId = rs.getInt("music_id");
		int artistId = rs.getInt("artist_id");
		String title = rs.getString("title");
		String description = rs.getString("description");
		LocalDateTime uploadDate = rs.getTimestamp("upload_date").toLocalDateTime();
		String audioFileUrl = rs.getString("audio_file_url");
		String imageUrl = rs.getString("image_url");
		boolean premiumContent = rs.getBoolean("premium_content");
		int genreId = rs.getInt("genre_id");
		int moodId = rs.getInt("mood_id");
		int likeCount = rs.getInt("like_count_cache");
		int totalPlayCount = rs.getInt("total_plays_cache");

		VisibilityType visibility = VisibilityType.fromString(rs.getString("visibility"));

		return new Music(musicId, artistId, title, description, uploadDate, audioFileUrl, imageUrl, premiumContent,
				genreId, moodId, likeCount, totalPlayCount, visibility);
	}

}
