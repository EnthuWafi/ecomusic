package com.enth.ecomusic.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;

public class ResultSetMapper {
	public static MusicDTO mapToMusicDTO(ResultSet rs) throws SQLException {
		MusicDTO dto = new MusicDTO();
		dto.setMusicId(rs.getInt("music_id"));
		dto.setTitle(rs.getString("title"));
		dto.setAudioFileUrl(rs.getString("audio_file_url"));
		dto.setImageUrl(rs.getString("image_url"));
		dto.setPremiumContent(rs.getBoolean("premium_content"));
		dto.setGenreId(rs.getInt("genre_id"));
		dto.setMoodId(rs.getInt("mood_id"));
		dto.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
		return dto;
	}

	public static MusicDetailDTO mapToMusicDetailDTO(ResultSet rs) throws SQLException {
		MusicDetailDTO dto = new MusicDetailDTO();
		
		dto.setMusic(mapToMusicDTO(rs));
		dto.setArtistUsername(rs.getString("artist_username"));
		dto.setArtistImageUrl(rs.getString("artist_image_url"));
		dto.setLikes(rs.getInt("like_count"));
		dto.setViews(rs.getInt("total_plays"));
		return dto;
	}
	
	public static Integer mapToInt(ResultSet rs) throws SQLException {
		return rs.getInt(1);
	}
}
