package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.MoodDAO;
import com.enth.ecomusic.model.entity.Mood;

public class MoodCacheService {

	private MoodDAO moodDAO;
    private Map<Integer, Mood> moodById;

    public MoodCacheService() {
        refresh();
    }

    public void refresh() {
    	if (moodDAO == null) {
    	    this.moodDAO = new MoodDAO();
    	}
        List<Mood> moods = moodDAO.getAllMoods();
        this.moodById = new ConcurrentHashMap<>(moods.stream()
                .collect(Collectors.toMap(Mood::getMoodId, mood -> mood)));
    }

    public Mood getById(int id) {
        return moodById.get(id);
    }

    public List<Mood> getAll() {
        return List.copyOf(moodById.values());
    }
}