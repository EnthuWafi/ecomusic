package com.enth.ecomusic.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.entity.Genre;
import com.enth.ecomusic.util.DBConnection;

public class GenreDAO {

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();

        String sql = "SELECT genre_id, name FROM genres";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setGenreId(rs.getInt("genre_id"));
                genre.setName(rs.getString("name"));
                genres.add(genre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genres;
    }
}
