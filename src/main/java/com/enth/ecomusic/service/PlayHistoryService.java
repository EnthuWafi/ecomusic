package com.enth.ecomusic.service;

import java.util.List;
import java.util.stream.Collectors;

import com.enth.ecomusic.dao.PlayHistoryDAO;
import com.enth.ecomusic.model.dto.PlayHistoryDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.entity.PlayHistory;
import com.enth.ecomusic.model.mapper.MusicMapper;
import com.enth.ecomusic.model.mapper.PlayHistoryMapper;

public class PlayHistoryService {

	private final PlayHistoryDAO playHistoryDAO;
	private final MusicService musicService;

	public PlayHistoryService(MusicService musicService) {
		this.playHistoryDAO = new PlayHistoryDAO();
		this.musicService = musicService;
	}

	public boolean recordPlay(PlayHistory playHistory, UserDTO currentUser) {
		if (!canRecordPlay(currentUser)) {
			return false;
		}
		
		Music music = musicService.getMusicById(playHistory.getMusicId());
		if (music == null) {
			return false;
		}
		if (!musicService.canAccessMusic(MusicMapper.INSTANCE.toDTO(music), currentUser)) return false;
		
		return playHistoryDAO.logPlay(playHistory);
	}

	public List<PlayHistoryDTO> getRecentPlays(int userId, int offset, int limit, UserDTO currentUser) {
	    List<PlayHistory> recentPlays = playHistoryDAO.getRecentPlaysByUserId(userId, offset, limit, currentUser.getUserId());
	    return recentPlays.stream().map(play -> {
	    	loadMusic(play);
	    	return PlayHistoryMapper.INSTANCE.toDTO(play);
	    			}).collect(Collectors.toList());
	}

	private void loadMusic(PlayHistory play) {
		if (play != null) {
			play.setMusic(musicService.getMusicById(play.getMusicId()));
		}
	}

	public boolean clearPlayHistoryForUser(int userId) {
		return playHistoryDAO.deletePlayHistoryByUserId(userId);
	}
	
	
	public boolean canRecordPlay(UserDTO user) {
	    if (user == null) return true; // Anonymous users can record play
	    if (user.isAdmin() || user.isSuperAdmin()) return false; // Admins can't
	    return true; // Regular users can
	}
	
	public long sumListenDurationByUserId(int userId) {
		return playHistoryDAO.sumListenDurationByUserId(userId);
	}


}
