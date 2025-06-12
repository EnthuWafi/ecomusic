package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.entity.PlaylistMusic;
import com.enth.ecomusic.model.mapper.ResultSetMapper;
import com.enth.ecomusic.util.DAOUtil;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMusicDAO {

    // ADD music to playlist (unchanged)
    public boolean addMusicToPlaylist(PlaylistMusic playlistMusic) {
        String sql = "INSERT INTO PlaylistMusic (playlist_id, music_id, position) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistMusic.getPlaylistId());
            stmt.setInt(2, playlistMusic.getMusicId());
            stmt.setInt(3, playlistMusic.getPosition());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding music to playlist: " + e.getMessage());
            return false;
        }
    }

    // GET all PlaylistMusic entries for a playlist 
    public PlaylistMusic getPlaylistMusic(int playlistId, int musicId) {
    	String sql = "SELECT * FROM PlaylistMusic WHERE playlist_id = ? AND music_id = ?";
    	
    	return DAOUtil.executeSingleQuery(sql, this::mapResultSetToPlaylistMusic, playlistId, musicId);
    }
    
    public List<PlaylistMusic> getPlaylistMusicListByPlaylistId(int playlistId) {
        List<PlaylistMusic> list = new ArrayList<>();
        String sql = "SELECT * FROM PlaylistMusic WHERE playlist_id = ? ORDER BY position";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PlaylistMusic pm = mapResultSetToPlaylistMusic(rs);
                    list.add(pm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving playlist music: " + e.getMessage());
        }

        return list;
    }

    // REMOVE one music entry from playlist
    public boolean removeMusicFromPlaylist(int playlistId, int musicId, Connection conn) {
        String sql = "DELETE FROM PlaylistMusic WHERE playlist_id = ? AND music_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing music from playlist: " + e.getMessage());
            return false;
        }
    }

    // CLEAR playlist (unchanged)
    public boolean clearPlaylist(int playlistId) {
        String sql = "DELETE FROM PlaylistMusic WHERE playlist_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error clearing playlist: " + e.getMessage());
            return false;
        }
    }
    
    
    /**
     * Shifts the positions of all songs within a given range by a specific amount.
     * This is the core of the reordering logic.
     *
     * @param playlistId    The playlist to modify.
     * @param startPosition The starting position of the range (inclusive).
     * @param endPosition   The ending position of the range (inclusive).
     * @param shiftAmount   The amount to add to the position (e.g., +1 to move down, -1 to move up).
     */
    public boolean shiftPositions(int playlistId, int startPosition, int endPosition, int shiftAmount, Connection conn) {
        String sql = "UPDATE PlaylistMusic SET position = position + ? " +
                     "WHERE playlist_id = ? AND position >= ? AND position <= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftAmount);
            stmt.setInt(2, playlistId);
            stmt.setInt(3, startPosition);
            stmt.setInt(4, endPosition);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error shifting playlist: " + e.getMessage());
            return false;
        }
    }
    /**
     * Sets the position of a specific song directly.
     * This is the final step in the reordering process.
     */
    public boolean setSongPosition(PlaylistMusic playlistMusic, int newPosition, Connection conn) {
        String sql = "UPDATE PlaylistMusic SET position = ? WHERE playlist_id = ? AND music_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newPosition);
            stmt.setInt(2, playlistMusic.getPlaylistId());
            stmt.setInt(3, playlistMusic.getMusicId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error setting position: " + e.getMessage());
            return false;
        }
    }
    
    public Integer getHighestPositionForPlaylist(int playlistId) {
    	String sql = "SELECT MAX(position) FROM PlaylistMusic WHERE playlist_id = ?";
    	
    	Integer result = DAOUtil.executeSingleQuery(sql, ResultSetMapper::mapToInt, playlistId);
    	
    	return (result != null) ? result : 0;
    }

    
    private PlaylistMusic mapResultSetToPlaylistMusic(ResultSet rs) throws SQLException {
    	return new PlaylistMusic(rs.getInt("playlist_id"), rs.getInt("music_id"), 
    			rs.getTimestamp("added_at").toLocalDateTime(), rs.getInt("position"));  
    }


}

