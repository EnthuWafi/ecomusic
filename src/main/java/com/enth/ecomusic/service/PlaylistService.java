package com.enth.ecomusic.service;

import com.enth.ecomusic.dao.PlaylistDAO;
import com.enth.ecomusic.dao.PlaylistMusicDAO;
import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.PlaylistMusicDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.entity.Playlist;
import com.enth.ecomusic.model.entity.PlaylistMusic;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.model.mapper.PlaylistMapper;
import com.enth.ecomusic.model.transaction.TransactionTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

	private final PlaylistDAO playlistDAO;
	private final PlaylistMusicDAO playlistMusicDAO;
	private final MusicService musicService;
	private final UserService userService;

	public PlaylistService(MusicService musicService, UserService userService) {
		this.playlistDAO = new PlaylistDAO();
		this.playlistMusicDAO = new PlaylistMusicDAO();
		this.musicService = musicService;
		this.userService = userService;
	}

	private void loadMusicForPlaylist(Playlist playlist) {
		List<PlaylistMusic> playlistMusicList = playlistMusicDAO
				.getPlaylistMusicListByPlaylistId(playlist.getPlaylistId());

		for (PlaylistMusic pm : playlistMusicList) {
			Music music = musicService.getMusicById(pm.getMusicId());		
			if (music != null) {
				User user = userService.getUserById(music.getArtistId());
				music.setArtist(user);
				pm.setMusic(music);
			}
		}

		playlist.setMusicList(playlistMusicList);
	}

	// Method to get a playlist with music by playlistId
	public PlaylistDTO getPlaylistWithMusicByPlaylistId(int playlistId, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);

		if (!canAccessPublicPlaylist(playlist, currentUser)) {
			return null;
		}
		
		loadMusicForPlaylist(playlist);
		PlaylistDTO playlistDTO = PlaylistMapper.INSTANCE.toDTO(playlist);
		return playlistDTO;
	}

	// Method to get all playlists for a user with associated music
	public List<PlaylistDTO> getUserPlaylistWithMusicByUserId(int userId, UserDTO currentUser) {
		List<Playlist> playlists = playlistDAO.getPlaylistsByUserId(userId);
		List<PlaylistDTO> playlistDTOList = new ArrayList<>();
		for (Playlist playlist : playlists) {
			if (canAccessPublicPlaylist(playlist, currentUser)) {
				loadMusicForPlaylist(playlist);
				playlistDTOList.add(PlaylistMapper.INSTANCE.toDTO(playlist));
			}	
		}
		return playlistDTOList;
	}

	/**
	 * Adds a new, empty playlist for a user.
	 * 
	 * @param playlist The playlist object to add, typically containing userId and
	 *                 name.
	 * @return The created playlist, possibly with the new playlistId.
	 */
	public boolean addPlaylist(Playlist playlist, UserDTO currentUser) {
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}
		
		return playlistDAO.insertPlaylist(playlist);
	}

	/**
	 * Removes an entire playlist. The ON DELETE CASCADE constraint in the database
	 * will handle removing associated songs from the PlaylistMusic table.
	 * 
	 * @param playlistId The ID of the playlist to remove.
	 */
	public boolean removePlaylist(int playlistId, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);
		
		if (!canDeletePlaylist(playlist, currentUser)) {
			return false;
		}
		
		
		return playlistDAO.deletePlaylist(playlistId);
	}

	/**
	 * Updates a playlist's details (e.g., its name).
	 * 
	 * @param playlist The playlist object with updated information.
	 */
	public boolean updatePlaylist(Playlist playlist, UserDTO currentUser) {
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}
		
		return playlistDAO.updatePlaylist(playlist);
	}

	/**
	 * Adds a song to a specific playlist. The song will be added to the last
	 * position.
	 */
	public boolean addSongToPlaylist(PlaylistMusic playlistMusic, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistMusic.getPlaylistId());
		
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}
		
		
		Integer highestPosition = playlistMusicDAO.getHighestPositionForPlaylist(playlistMusic.getPlaylistId());

        // Set the new song's position to one more than the highest position
        playlistMusic.setPosition(highestPosition + 1);

        return playlistMusicDAO.addMusicToPlaylist(playlistMusic);
	}

	/**
	 * Removes a single song from a playlist.
	 * 
	 * @param playlistId The ID of the playlist.
	 * @param musicId    The ID of the song to remove.
	 */
	public boolean removeSongFromPlaylist(int playlistId, int musicId, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);
		
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}
		
		try (TransactionTemplate transaction = new TransactionTemplate()) {

			PlaylistMusic playlistMusic = playlistMusicDAO.getPlaylistMusic(playlistId, musicId);
			boolean removedSuccessfully = playlistMusicDAO.removeMusicFromPlaylist(playlistId, musicId, transaction.getConnection());

			if (playlistMusic == null) {
				throw new SQLException(
						"Transaction failed: Music ID " + musicId + " not found in Playlist ID " + playlistId);
			}
			if (!removedSuccessfully) {
				throw new SQLException(
						"Transaction failed: Music ID " + musicId + " failed to be removed!");
			}

			int start = playlistMusic.getPosition() + 1;
			int end = Integer.MAX_VALUE;
			int shift = -1;
			if (!playlistMusicDAO.shiftPositions(playlistId, start, end, shift, transaction.getConnection())) {
				throw new SQLException("Failed to playlist after song removal.");
			}
			transaction.commit();
			return true;

		} catch (SQLException e) {
			System.err.println("Playlist transaction failed and will be rolled back: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		

	}

	/**
	 * Removes all songs from a playlist, leaving the playlist empty.
	 * 
	 * @param playlistId The ID of the playlist to clear.
	 */
	public boolean clearPlaylist(int playlistId, UserDTO currentUser) {		
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);
		
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}
		return playlistMusicDAO.clearPlaylist(playlistId);
	}

	/**
	 * Updates the position of a song within a playlist.
	 * 
	 * @param playlistId  The ID of the playlist.
	 * @param musicId     The ID of the song to move.
	 * @param newPosition The new position for the song.
	 */
	public boolean updatePlaylistSongPosition(int playlistId, int musicId, int newPosition, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);
		
		if (!canModifyPlaylist(playlist, currentUser)) {
			return false;
		}

		try (TransactionTemplate transaction = new TransactionTemplate()) {

			PlaylistMusic playlistMusic = playlistMusicDAO.getPlaylistMusic(playlistId, musicId);

			if (playlistMusic == null) {
				throw new SQLException(
						"Transaction failed: Music ID " + musicId + " not found in Playlist ID " + playlistId);
			}

			int oldPosition = playlistMusic.getPosition();

			if (oldPosition == newPosition) {
				return true;
			}

			if (newPosition > oldPosition) {
				// --- MOVING DOWN (e.g., from pos 2 to 5) ---
				// We shift songs between oldPos+1 and newPos UP by 1.
				int start = oldPosition + 1;
				int end = newPosition;
				int shift = -1;
				if (!playlistMusicDAO.shiftPositions(playlistId, start, end, shift, transaction.getConnection())) {
					throw new SQLException("Failed to shift songs up during reorder.");
				}
			} else {
				// --- MOVING UP (e.g., from pos 5 to 2) ---
				// We shift songs between newPos and oldPos-1 DOWN by 1.
				int start = newPosition;
				int end = oldPosition - 1;
				int shift = +1;
				if (!playlistMusicDAO.shiftPositions(playlistId, start, end, shift, transaction.getConnection())) {
					throw new SQLException("Failed to shift songs down during reorder.");
				}
			}

			boolean moveSuccess = playlistMusicDAO.setSongPosition(playlistMusic, newPosition,
					transaction.getConnection());
			if (!moveSuccess) {
				throw new SQLException("Failed to move the target song to its new position.");
			}
			
			transaction.commit();
			return true;

		} catch (SQLException e) {
			System.err.println("Playlist transaction failed and will be rolled back: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		

	}

	/**
	 * Get playlist without loading playlist music
	 * @param playlistId
	 * @return
	 */
	public PlaylistDTO getPlaylistByPlaylistId(int playlistId, UserDTO currentUser) {
		Playlist playlist = playlistDAO.getPlaylistById(playlistId);
		
		if (!canAccessPublicPlaylist(playlist, currentUser)) {
			return null;
		}
		// TODO Auto-generated method stub
		return PlaylistMapper.INSTANCE.toDTO(playlist);
	}
	
	public PlaylistMusicDTO getPlaylistMusic(int playlistId, int musicId) {
		PlaylistMusic playlistMusic = playlistMusicDAO.getPlaylistMusic(playlistId, musicId);
		
		return PlaylistMapper.INSTANCE.toDTO(playlistMusic);
	}

	
	public boolean canAccessPublicPlaylist(Playlist playlist, UserDTO user) {
        return playlist != null &&
               (playlist.getVisibility() == VisibilityType.PUBLIC || (user != null && playlist.getUserId() == user.getUserId()));
    }

    public boolean canModifyPlaylist(Playlist playlist, UserDTO user) {
        return playlist != null &&
               user != null &&
               playlist.getUserId() == user.getUserId();
    }
    
    public boolean canDeletePlaylist(Playlist playlist, UserDTO user) {
        return playlist != null &&
               user != null &&
               (playlist.getUserId() == user.getUserId() || user.hasRole(RoleType.ADMIN));
    }

}
