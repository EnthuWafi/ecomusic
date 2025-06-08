package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.entity.Playlist;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    // CREATE
    public boolean insertPlaylist(Playlist playlist) {
        String sql = "INSERT INTO Playlists (user_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlist.getUserId());
            stmt.setString(2, playlist.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting playlist: " + e.getMessage());
            return false;
        }
    }

    // READ by ID (NO MUSIC)
    public Playlist getPlaylistById(int playlistId) {
        String sql = "SELECT * FROM Playlists WHERE playlist_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPlaylist(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching playlist by ID: " + e.getMessage());
        }
        return null;
    }

    // READ all playlists for a user (NO MUSIC)
    public List<Playlist> getPlaylistsByUserId(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM Playlists WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    playlists.add(mapResultSetToPlaylist(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching playlists for user: " + e.getMessage());
        }
        return playlists;
    }

    // UPDATE
    public boolean updatePlaylistName(int playlistId, String newName) {
        String sql = "UPDATE Playlists SET name = ? WHERE playlist_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, playlistId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating playlist name: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deletePlaylist(int playlistId) {
        String sql = "DELETE FROM Playlists WHERE playlist_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting playlist: " + e.getMessage());
            return false;
        }
    }

    // Helper
    private Playlist mapResultSetToPlaylist(ResultSet rs) throws SQLException {
        // Extracting values from ResultSet, ensuring order and types match the constructor signature
        int playlistId = rs.getInt("playlist_id");
        int userId = rs.getInt("user_id");
        String name = rs.getString("name");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();    
        VisibilityType visibility = VisibilityType.fromString(rs.getString("visibility"));
       
        return new Playlist(
            playlistId,
            userId,
            name,
            createdAt,
            visibility 
        );
    }
}

