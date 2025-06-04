package com.enth.ecomusic.model.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.entity.UserMusicDailyStats;
import com.enth.ecomusic.util.DBConnection;

public class UserMusicDailyStatsDAO {
	public List<UserMusicDailyStats> getAllStats() {
		List<UserMusicDailyStats> statsList = new ArrayList<>();
		String sql = "SELECT * FROM UserMusicDailyStats";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			try (ResultSet rs = stmt.executeQuery()){
				while (rs.next()) {
					UserMusicDailyStats stats = mapToUserMusicDailyStats(rs);
					statsList.add(stats);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statsList;
	}

	/**
	 * Uses Oracle MERGE statement to insert new or update existing daily stats.
	 */
	public boolean upsertStats(UserMusicDailyStats userMusicDailyStats, boolean wasSkipped, int playDuration) {
		String sql = """

				MERGE INTO UserMusicDailyStats target
				USING (
				  SELECT
				    ? AS user_id, ? AS music_id, ? AS stat_date
				  FROM dual
				) src
				  ON (
				    target.user_id   = src.user_id 
				    AND target.music_id  = src.music_id
				    AND target.stat_date = src.stat_date
				  )
				WHEN MATCHED THEN
				  UPDATE SET
				    total_plays       = total_plays + 1,
				    total_skips       = total_skips + ?,
				    total_listen_time = total_listen_time + ?
				WHEN NOT MATCHED THEN
				  INSERT (
				    user_id, music_id, stat_date,
				    total_plays, total_skips, total_listen_time
				  ) VALUES ( ?, ?, ?, 1, ?, ? );
				  
				  """;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			// src SELECT bind parameters
			stmt.setInt(1, userMusicDailyStats.getUserId());
			stmt.setInt(2, userMusicDailyStats.getMusicId());
			stmt.setDate(3, Date.valueOf(userMusicDailyStats.getStatDate()));
			// matched: how many skips to add (1 if skipped, 0 otherwise)
			stmt.setInt(4, wasSkipped ? 1 : 0);
			stmt.setInt(5, playDuration);
			// not matched: insert parameters
			stmt.setInt(6, userMusicDailyStats.getUserId());
			stmt.setInt(7, userMusicDailyStats.getMusicId());
			stmt.setDate(8, Date.valueOf(userMusicDailyStats.getStatDate()));
			stmt.setInt(9, wasSkipped ? 1 : 0);
			stmt.setInt(10, playDuration);

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public UserMusicDailyStats getStats(int userId, int musicId, LocalDate statDate) {
		String sql = "SELECT * FROM UserMusicDailyStats WHERE user_id = ? AND music_id = ? AND stat_date = ?";
		UserMusicDailyStats stats = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			stmt.setInt(2, musicId);
			stmt.setDate(3, Date.valueOf(statDate));

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					stats = mapToUserMusicDailyStats(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stats;
	}

	public List<UserMusicDailyStats> getStatsByUserId(int userId) {
		List<UserMusicDailyStats> statsList = new ArrayList<>();
		String sql = """
				SELECT * FROM UserMusicDailyStats 
				WHERE user_id = ?
				ORDER BY stat_date DESC
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					statsList.add(mapToUserMusicDailyStats(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return statsList;
	}

	public boolean deleteStats(int userId, int musicId, LocalDate statDate) {
		String sql = "DELETE FROM UserMusicDailyStats WHERE user_id = ? AND music_id = ? AND stat_date = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			stmt.setInt(2, musicId);
			stmt.setDate(3, Date.valueOf(statDate));
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private UserMusicDailyStats mapToUserMusicDailyStats(ResultSet rs) throws SQLException {
		return new UserMusicDailyStats(rs.getInt("user_id"), rs.getInt("music_id"),
				rs.getDate("stat_date").toLocalDate(), rs.getInt("total_plays"), rs.getInt("total_skips"),
				rs.getLong("total_listen_time"));
	}

	public int sumViewsByMusicId(int musicId) {
		// TODO Auto-generated method stub
		String sql = "SELECT SUM(total_plays) FROM UserMusicDailyStats WHERE music_id = ?";
        int sum = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, musicId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sum = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return sum;
	}
}
