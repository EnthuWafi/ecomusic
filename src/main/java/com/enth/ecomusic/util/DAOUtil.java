package com.enth.ecomusic.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOUtil {
	public interface RowMapper<T> {
		T map(ResultSet rs) throws SQLException;
	}

	public static <T> List<T> executePaginatedQuery(String fullRowNumberQuery, // Query must have rnum to work
			List<Object> params, int page, int pageSize, RowMapper<T> mapper) {
		List<T> result = new ArrayList<>();
		String finalQuery = "SELECT * FROM ( " + fullRowNumberQuery + " ) WHERE rnum > ? AND rnum <= ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(finalQuery)) {
			int i = 1;
			if (params != null) {
				for (Object param : params) {
					stmt.setObject(i++, param);
				}
			}

			int lower = (page - 1) * pageSize;
			int upper = lower + pageSize;
			stmt.setInt(i++, lower);
			stmt.setInt(i, upper);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					result.add(mapper.map(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Paginated query failed: " + e.getMessage());
		}

		return result;
	}

}
