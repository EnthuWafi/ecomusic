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

			stmt.setInt(1, playHistory.getUserId());
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
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(*) FROM PlayHistory WHERE music_id = ?";

		Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, musicId);

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

	public List<PlayHistory> getRecentPlaysByUserId(int userId, int limit) {
		String query = """
				SELECT *
				FROM (
				    SELECT p.*, ROW_NUMBER() OVER (ORDER BY p.played_at DESC) AS rnum
				    FROM PlayHistory p
				    WHERE p.user_id = ?
				)
				WHERE rnum <= ?
				""";

		List<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(limit);

		return DAOUtil.executeQuery(query, this::mapToPlayHistory, params.toArray());
	}

}
