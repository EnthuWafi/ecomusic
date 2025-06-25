package com.enth.ecomusic.service;

import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.dao.LikeDAO;
import com.enth.ecomusic.model.dto.LikeDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Like;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.mapper.LikeMapper;
import com.enth.ecomusic.model.mapper.MusicMapper;

public class LikeService {

    private final LikeDAO likeDAO;
    private final MusicService musicService;

    public LikeService(MusicService musicService) {
        this.likeDAO = new LikeDAO();
        this.musicService = musicService;

    }
    
    /**
     * Records a user's like for a specific song.
     * This operation checks if the like already exists before adding to prevent duplicates.
     *
     * @param like The Like object containing userId and musicId.
     * @return true if the like was added successfully (or already existed), false if an error occurred.
     */
    public boolean likeSong(Like like, UserDTO currentUser) {
    	if (!canLikeSong(currentUser)) {
    		return false;
    	}
		Music music = musicService.getMusicById(like.getMusicId());
		if (music == null) {
			return false;
		}
		if (!musicService.canAccessMusic(MusicMapper.INSTANCE.toDTO(music), currentUser)) return false;
    	
    	return likeDAO.addLike(like);
     
    }

    /**
     * Removes a user's like from a specific song.
     *
     * @param userId The ID of the user.
     * @param musicId The ID of the song.
     * @return true if the like was successfully removed (or didn't exist), false if an error occurred.
     */
    public boolean unlikeSong(int userId, int musicId, UserDTO currentUser) {
    	if (!canUnlikeSong(userId, currentUser)) {
    		return false;
    	}
    	return likeDAO.removeLike(userId, musicId);
    }

    /**
     * Checks if a user has liked a specific song.
     *
     * @param userId The ID of the user.
     * @param musicId The ID of the song.
     * @return true if the song is liked by the user, false otherwise.
     */
    public boolean isSongLikedByUser(int userId, int musicId) {
    	return likeDAO.isSongLikedByUser(userId, musicId);
    }

    /**
     * Retrieves a list of all songs liked by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Like objects representing liked songs, or an empty list if none found.
     * Returns null if an error occurs.
     */
    public List<LikeDTO> getLikedSongsForUser(int userId) {
    	List<Like> likeList = likeDAO.getLikedSongsByUserId(userId);
    	List<LikeDTO> likeDTOList = new ArrayList<>();
    	for (Like like : likeList) {
    		like.setMusic(musicService.getMusicById(like.getMusicId()));
    		likeDTOList.add(LikeMapper.INSTANCE.toDTO(like));
    	}
        
        
        return likeDTOList;
    }
    
    public Integer getCountLikedSongByUserId(int userId) {
    	return likeDAO.countLikeByUserId(userId);
    }
    
    public Integer getCountLikedByMusicId(int musicId) {
    	return likeDAO.countLikeByMusicId(musicId);
    }
    
    private boolean canLikeSong(UserDTO user) {
    	return user != null && !(user.isAdmin() || user.isSuperAdmin());
    }
    
    private boolean canUnlikeSong(int targetUserId, UserDTO user) {
    	return user != null && targetUserId == user.getUserId();
    }

}
