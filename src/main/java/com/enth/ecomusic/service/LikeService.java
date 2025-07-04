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

    public boolean unlikeSong(int userId, int musicId, UserDTO currentUser) {
    	if (!canUnlikeSong(userId, currentUser)) {
    		return false;
    	}
    	return likeDAO.removeLike(userId, musicId);
    }

    public boolean isSongLikedByUser(int userId, int musicId) {
    	return likeDAO.isSongLikedByUser(userId, musicId);
    }

    public List<LikeDTO> getLikedSongsForUser(int userId, int offset, int limit, int currentUserId) {
    	List<Like> likeList = likeDAO.getLikedSongsByUserId(userId, offset, limit, currentUserId);
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
