package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.GenreDAO;
import com.enth.ecomusic.model.entity.Genre;

public class GenreCacheService {
	private Map<Integer, Genre> genreById;

    public GenreCacheService() {
        refresh();
    }

    public void refresh() {
        GenreDAO genreDAO = new GenreDAO();
        List<Genre> genres = genreDAO.getAllGenres();
        this.genreById = new ConcurrentHashMap<>(genres.stream()
                .collect(Collectors.toMap(Genre::getGenreId, genre -> genre)));
    }

    public Genre getById(int id) {
        return genreById.get(id);
    }

    public List<Genre> getAll() {
        return List.copyOf(genreById.values());
    }
}
