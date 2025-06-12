package com.enth.ecomusic.service;

import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.dao.PlayHistoryDAO;
import com.enth.ecomusic.model.dto.PlayHistoryDTO;
import com.enth.ecomusic.model.entity.PlayHistory;
import com.enth.ecomusic.model.mapper.PlayHistoryMapper;

public class PlayHistoryService {
	
	 private final PlayHistoryDAO playHistoryDAO;

	    public PlayHistoryService() {
	        this.playHistoryDAO = new PlayHistoryDAO();
	    }

	    /**
	     * Records a song play for a user.
	     * This operation adds a new entry to the play history.
	     *
	     * @param playHistory The PlayHistory object containing userId, musicId, and timestamp.
	     * @return true if the play was recorded successfully, false otherwise.
	     */
	    public boolean recordPlay(PlayHistory playHistory) {
	        return playHistoryDAO.logPlay(playHistory);
	    }

	    /**
	     * Retrieves a list of play history records for a given user.
	     *
	     * @param userId The ID of the user whose play history is to be retrieved.
	     * @return A list of PlayHistory objects for the user, or an empty list if none found.
	     * Returns null if an error occurs.
	     */
	    public List<PlayHistoryDTO> getPlayHistoryForUser(int userId) {
	        List<PlayHistory> playHistory = playHistoryDAO.getPlayHistoryByUserId(userId);
	        List<PlayHistoryDTO> playHistoryDTO = new ArrayList<>();
	        for (PlayHistory play : playHistory) {
	        	playHistoryDTO.add(PlayHistoryMapper.INSTANCE.toDTO(play));
	        }
	        return playHistoryDTO;
	    }

	    /**
	     * Clears all play history records for a given user.
	     * This operation is transactional to ensure all records are removed atomically.
	     *
	     * @param userId The ID of the user whose play history is to be cleared.
	     * @return true if the play history was cleared successfully, false otherwise.
	     */
	    public boolean clearPlayHistoryForUser(int userId) {
	        return playHistoryDAO.deletePlayHistoryByUserId(userId);
	    }
}
