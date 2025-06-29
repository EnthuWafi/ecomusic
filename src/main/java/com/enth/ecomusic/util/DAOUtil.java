package com.enth.ecomusic.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAOUtil {
	
	private DAOUtil() {}
	
	public interface RowMapper<T> {
		T map(ResultSet rs) throws SQLException;
	}

	/**
     * Executes a paginated query that is expected to return a list of objects.
     *
     * @param fullRowNumberQuery The SQL query to execute with ROW_NUMBER() AS rnum.
     * @param mapper The RowMapper to map the ResultSet to an object.
     * @param page The page number.
     * @param pageSize The page size.
     * @param params The parameters to be set in the PreparedStatement.
     * @return A List of objects of type T.
     */
	public static <T> List<T> executePaginatedQuery(String fullRowNumberQuery, // Query must have rnum to work
			RowMapper<T> mapper, int page, int pageSize, Object... params) {
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

			int offset = (page - 1) * pageSize;
			int limit = page * pageSize;
			stmt.setInt(i++, offset);
			stmt.setInt(i, limit);

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
	
	/**
     * Executes a query that is expected to return a list of objects.
     *
     * @param sql The SQL query to execute.
     * @param mapper The RowMapper to map the ResultSet to an object.
     * @param params The parameters to be set in the PreparedStatement.
     * @return A List of objects of type T.
     */
    public static <T> List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
        }
        return result;
    }

    /**
     * Executes a query that is expected to return a single object.
     *
     * @param sql The SQL query to execute.
     * @param mapper The RowMapper to map the ResultSet to an object.
     * @param params The parameters to be set in the PreparedStatement.
     * @return An object of type T, or null if not found.
     */
    public static <T> T executeSingleQuery(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException e) {
        	System.err.println("SQL: " + sql);
            System.err.println("Single query failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Executes an update operation (INSERT, UPDATE, DELETE).
     *
     * @param sql The SQL statement to execute.
     * @param params The parameters to be set in the PreparedStatement.
     * @return The number of rows affected by the operation.
     */
    public static boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }
    
    public static String buildOracleTextQuery(String userInput) {
	    if (userInput == null || userInput.isBlank()) return null;
	    
	    return Arrays.stream(userInput.trim().split("\\s+"))
	        .map(word -> word + "%")
	        .collect(Collectors.joining(" OR "));
	}



}
