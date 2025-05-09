package com.enth.ecomusic.model.dao;

import com.enth.ecomusic.model.Playlist;
import com.enth.ecomusic.model.PlaylistMusic;
import com.enth.ecomusic.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
	private PlaylistMusicDAO playlistMusicDAO;

	public PlaylistDAO() {
		this.playlistMusicDAO = new PlaylistMusicDAO();
	}

	// CREATE
	public boolean insertPlaylist(Playlist playlist) {
		String sql = "INSERT INTO Playlists (user_id, name) VALUES (?, ?)";
		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlist.getUserId());
			stmt.setString(2, playlist.getName());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error inserting playlist: " + e.getMessage());
			return false;
		}
	}

	// READ by ID
	public Playlist getPlaylistById(int playlistId) {
		String sql = "SELECT * FROM Playlists WHERE playlist_id = ?";
		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Playlist playlist = mapResultSetToPlaylist(rs);

					List<PlaylistMusic> musicList = playlistMusicDAO.getMusicByPlaylistId(playlistId);
					playlist.setMusicList(musicList);

					return playlist;

				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching playlist by ID: " + e.getMessage());
		}
		return null;
	}

	// READ all playlists for a user
	public List<Playlist> getPlaylistsByUserId(int userId) {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "SELECT * FROM Playlists WHERE user_id = ? ORDER BY created_at DESC";
		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
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
		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
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
		try (Connection conn = DBConnection.getConnection();PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playlistId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting playlist: " + e.getMessage());
			return false;
		}
	}

	// Helper method
	private Playlist mapResultSetToPlaylist(ResultSet rs) throws SQLException {
		return new Playlist(rs.getInt("playlist_id"), rs.getInt("user_id"), rs.getString("name"),
				rs.getDate("created_at"));
	}

}
