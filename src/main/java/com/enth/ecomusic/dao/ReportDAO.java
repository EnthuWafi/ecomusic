package com.enth.ecomusic.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.dto.ChartDTO;
import com.enth.ecomusic.model.dto.ChartDatasetDTO;
import com.enth.ecomusic.util.DBConnection;

public class ReportDAO {
	public ChartDTO getChartByDateGroup(String table, String dateColumn, String label, String columnToAggregate,
			boolean isSum, LocalDate start, LocalDate end, String dateFormat) {
		String aggregation = isSum ? "SUM" : "COUNT";

		String sql = String.format("""
				    SELECT
				        TO_CHAR(%s, '%s') AS time_label,
				        %s(%s) AS value
				    FROM
				        %s
				    WHERE
				        %s BETWEEN ? AND ?
				    GROUP BY
				        TO_CHAR(%s, '%s')
				    ORDER BY
				        time_label
				""", dateColumn, dateFormat, aggregation, columnToAggregate, table, dateColumn, dateColumn, dateFormat);

		List<String> labels = new ArrayList<>();
		List<Integer> values = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setDate(1, Date.valueOf(start));
			stmt.setDate(2, Date.valueOf(end));

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					labels.add(rs.getString("time_label"));
					values.add(rs.getInt("value"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ChartDatasetDTO dataset = new ChartDatasetDTO(label, values);
		return new ChartDTO(labels, List.of(dataset));
	}
	
	public ChartDTO getChartByArtistAndDate(
	        int artistId,
	        LocalDate start,
	        LocalDate end,
	        String dateFormat
	) {

	    String sql = """
	        SELECT
	            TO_CHAR(p.played_at, '%s') AS time_label,
	            COUNT(*) AS value
	        FROM
	            PlayHistory p
	        JOIN
	            music m ON m.music_id = p.music_id
	        WHERE
	            m.artist_id = ?
	            AND p.played_at BETWEEN ? AND ?
	        GROUP BY
	            TO_CHAR(p.played_at, '%s')
	        ORDER BY
	            time_label
	        """.formatted(dateFormat, dateFormat);

	    List<String> labels = new ArrayList<>();
	    List<Integer> values = new ArrayList<>();

	    try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, artistId);
	        stmt.setDate(2, Date.valueOf(start));
	        stmt.setDate(3, Date.valueOf(end));

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                labels.add(rs.getString("time_label"));
	                values.add(rs.getInt("value"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    ChartDatasetDTO dataset = new ChartDatasetDTO("Artist Plays", values);
	    return new ChartDTO(labels, List.of(dataset));
	}


}
