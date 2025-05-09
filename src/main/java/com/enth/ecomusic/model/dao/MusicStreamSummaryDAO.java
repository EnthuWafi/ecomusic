package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.util.DBConnection;
import com.enth.ecomusic.model.MusicStreamSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicStreamSummaryDAO {
	// INSERT or UPDATE (Upsert)
	public boolean upsertStreamCount(int musicId, Date streamDate) {
		String updateSql = "MERGE INTO MusicStreamSummary mss "
				+ "USING dual ON (mss.music_id = ? AND mss.stream_date = ?) " + "WHEN MATCHED THEN "
				+ "  UPDATE SET mss.stream_count = mss.stream_count + 1 " + "WHEN NOT MATCHED THEN "
				+ "  INSERT (music_id, stream_date, stream_count) " + "  VALUES (?, ?, 1)"; 

		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(updateSql)) {
			stmt.setInt(1, musicId);
			stmt.setDate(2, streamDate);
			stmt.setInt(3, musicId); 
			stmt.setDate(4, streamDate);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error upserting stream count: " + e.getMessage());
			return false;
		}
	}

	// GET summary for a music ID
	public List<MusicStreamSummary> getSummaryByMusicId(int musicId) {
		List<MusicStreamSummary> summaries = new ArrayList<>();
		String sql = "SELECT * FROM MusicStreamSummary WHERE music_id = ? ORDER BY stream_date DESC";

		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, musicId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MusicStreamSummary summary = new MusicStreamSummary(rs.getInt("music_stream_summary_id"),
							rs.getInt("music_id"), rs.getDate("stream_date"), rs.getInt("stream_count"));
					summaries.add(summary);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching stream summary: " + e.getMessage());
		}

		return summaries;
	}
}
