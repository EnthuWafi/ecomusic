package com.enth.ecomusic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.entity.Like;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

public class LikeDAO {


    public boolean addLike(Like like) {
        String sql = "INSERT INTO Likes (user_id, music_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, like.getUserId());
            stmt.setInt(2, like.getMusicId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeLike(int userId, int musicId) {
        String sql = "DELETE FROM Likes WHERE user_id = ? AND music_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, musicId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Like> getLikedSongsByUserId(int userId, int offset, int limit, int currentUserId) {
        List<Like> likedList = new ArrayList<>();
        String sql = """
				SELECT * FROM (
		            SELECT l.*, ROW_NUMBER() OVER (ORDER BY l.liked_at DESC) AS rnum
		            FROM Likes l
		            JOIN Music m ON m.music_id = l.music_id
		            WHERE l.user_id = ? AND (m.visibility = 'public' OR l.user_id = ?)
        		)
				WHERE rnum > ? AND rnum <= ?
				""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	int start = offset;
    	    int end = offset + limit;
        	
            stmt.setInt(1, userId);
            stmt.setInt(2, currentUserId);
            stmt.setInt(3, start);
            stmt.setInt(4, end);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likedList.add(new Like(
                        userId,
                        rs.getInt("music_id"),
                        rs.getTimestamp("liked_at").toLocalDateTime()
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return likedList;
    }

    /**
     * Returns the number of likes for a given music_id.
     */
    public int countLikeByMusicId(int musicId) {
        String sql = "SELECT COUNT(*) FROM Likes WHERE music_id = ?";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, musicId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Returns the number of likes made by a given user_id.
     */
    public int countLikeByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM Likes WHERE user_id = ?";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

	public boolean isSongLikedByUser(int userId, int musicId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM Likes WHERE user_id = ? AND music_id = ?";
		
		Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, userId, musicId);
		
		return result != null ? true : false;
	}
}
