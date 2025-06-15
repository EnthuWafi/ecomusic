package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.enth.ecomusic.dao.MoodDAO;
import com.enth.ecomusic.model.entity.Mood;

public class MoodCacheService {

	private MoodDAO moodDAO;
	private final Map<Integer, Mood> moodById = new ConcurrentHashMap<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	public MoodCacheService() {
		refresh();
	}

	public void refresh() {
		writeLock.lock();
		try {
			if (moodDAO == null) {
				this.moodDAO = new MoodDAO();
			}
			List<Mood> moods = moodDAO.getAllMoods();
			Map<Integer, Mood> moodById = new ConcurrentHashMap<>(
					moods.stream().collect(Collectors.toMap(Mood::getMoodId, mood -> mood)));

			this.moodById.clear();
			this.moodById.putAll(moodById);
		} finally {
			writeLock.unlock();
		}
	}

	public Mood getById(int id) {
		readLock.lock();
		try {
			return moodById.get(id);
		} finally {
			readLock.unlock();
		}
	}

	public List<Mood> getAll() {
		readLock.lock();
		try {
			return List.copyOf(moodById.values());
		} finally {
			readLock.unlock();
		}
	}
}