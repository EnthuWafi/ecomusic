package com.enth.ecomusic.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.entity.Mood;
import com.enth.ecomusic.util.DBConnection;

public class MoodDAO {

    public List<Mood> getAllMoods() {
        List<Mood> moods = new ArrayList<>();

        String sql = "SELECT mood_id, name FROM moods";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mood mood = new Mood();
                mood.setMoodId(rs.getInt("mood_id"));
                mood.setName(rs.getString("name"));
                moods.add(mood);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moods;
    }
}
