package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.enth.ecomusic.dao.GenreDAO;
import com.enth.ecomusic.model.entity.Genre;

public class GenreCacheService {
	private GenreDAO genreDAO;
	private final Map<Integer, Genre> genreById = new ConcurrentHashMap<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	public GenreCacheService() {
		refresh();
	}

	public void refresh() {
		writeLock.lock();
		try {
			if (genreDAO == null) {
				genreDAO = new GenreDAO();
			}
			List<Genre> genres = genreDAO.getAllGenres();
			Map<Integer, Genre> genreById = new ConcurrentHashMap<>(
					genres.stream().collect(Collectors.toMap(Genre::getGenreId, genre -> genre)));

			this.genreById.clear();
			this.genreById.putAll(genreById);
		} finally {
			writeLock.unlock();
		}
	}

	public Genre getById(int id) {
		readLock.lock();
		try {
			return genreById.get(id);
		} finally {
			readLock.unlock();
		}
	}

	public List<Genre> getAll() {
		readLock.lock();
		try {
			return List.copyOf(genreById.values());
		} finally {
			readLock.unlock();
		}
	}
}
