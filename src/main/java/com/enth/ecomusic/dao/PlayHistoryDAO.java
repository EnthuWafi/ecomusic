package com.enth.ecomusic.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.entity.PlayHistory;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

public class PlayHistoryDAO {

	public boolean logPlay(PlayHistory playHistory) {
		String sql = "INSERT INTO PlayHistory (user_id, music_id, listen_duration, was_skipped) VALUES (?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setObject(1, playHistory.getUserId(), java.sql.Types.INTEGER);
			stmt.setInt(2, playHistory.getMusicId());
			stmt.setLong(3, playHistory.getListenDuration());
			stmt.setInt(4, playHistory.isWasSkipped() ? 1 : 0);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Error logging play: " + e.getMessage());
			return false;
		}
	}

	public PlayHistory getPlayHistoryById(int playId) {
		String sql = "SELECT * FROM PlayHistory WHERE play_id = ?";
		PlayHistory ph = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, playId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					ph = mapToPlayHistory(rs);
					;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ph;
	}

	public List<PlayHistory> getPlayHistoryByUserId(int userId) {
		List<PlayHistory> historyList = new ArrayList<>();
		String sql = "SELECT * FROM PlayHistory WHERE user_id = ? ORDER BY played_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					historyList.add(mapToPlayHistory(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historyList;
	}

	public List<PlayHistory> getPlayHistoryByMusicId(int musicId) {
		List<PlayHistory> historyList = new ArrayList<>();
		String sql = "SELECT * FROM PlayHistory WHERE music_id = ? ORDER BY played_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, musicId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					historyList.add(mapToPlayHistory(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historyList;
	}

	public boolean deletePlayHistory(int playId) {
		String sql = "DELETE FROM PlayHistory WHERE play_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, playId);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Failed to delete play history: " + e.getMessage());
			return false;
		}
	}

	private PlayHistory mapToPlayHistory(ResultSet rs) throws SQLException {
		return new PlayHistory(rs.getInt("play_id"), rs.getInt("user_id"), rs.getInt("music_id"),
				rs.getTimestamp("played_at").toLocalDateTime(), rs.getLong("listen_duration"),
				rs.getInt("was_skipped") == 1);
	}

	public int countPlaysByMusicId(int musicId) {

		String sql = "SELECT COUNT(*) FROM PlayHistory WHERE music_id = ?";

		Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, musicId);

		return result != null ? result : 0;

	}

	public long sumListenDurationByUserId(int userId) {
		String sql = "SELECT SUM(listen_duration) FROM PlayHistory WHERE user_id = ?";

		Long result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToLong, userId);

		return result != null ? result : 0;
	}
	
	
	public boolean deletePlayHistoryByUserId(int userId) {
		String sql = "DELETE FROM PlayHistory WHERE user_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Failed to delete play history by user_id: " + e.getMessage());
			return false;
		}
	}

	public List<PlayHistory> getRecentPlaysByUserId(int userId, int offset, int limit, int currentUserId) {
		String query = """
				SELECT * FROM (
				    SELECT inner_query.*, ROW_NUMBER() OVER (ORDER BY inner_query.played_at DESC) AS rn
				    FROM (
				        SELECT p.*
				        FROM (
				            SELECT p.*, ROW_NUMBER() OVER (PARTITION BY p.music_id ORDER BY p.played_at DESC) AS rnum
				            FROM PlayHistory p
				            JOIN Music m ON m.music_id = p.music_id
				            WHERE p.user_id = ? AND (m.visibility = 'public' OR p.user_id = ?)
				        ) p
				        WHERE p.rnum = 1
				    ) inner_query
				)
				WHERE rn > ? AND rn <= ?
				""";

		int start = offset;
	    int end = offset + limit;

	    List<Object> params = new ArrayList<>();
	    params.add(userId);
	    params.add(currentUserId);
	    params.add(start);
	    params.add(end);

		return DAOUtil.executeQuery(query, this::mapToPlayHistory, params.toArray());
	}

}
