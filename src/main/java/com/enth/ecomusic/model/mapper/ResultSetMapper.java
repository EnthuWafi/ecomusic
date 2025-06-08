package com.enth.ecomusic.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Genre;
import com.enth.ecomusic.model.entity.Mood;
import com.enth.ecomusic.model.entity.Music;

//direct mapping
public class ResultSetMapper {
	public static MusicDTO mapToMusicDTO(ResultSet rs) throws SQLException {
		
		Music music = new Music();
		music.setMusicId(rs.getInt("music_id"));
		music.setArtistId(rs.getInt("artist_id"));
		music.setTitle(rs.getString("title"));
		music.setAudioFileUrl(rs.getString("audio_file_url"));
		music.setImageUrl(rs.getString("image_url"));
		music.setPremiumContent(rs.getBoolean("premium_content"));
		music.setGenreId(rs.getInt("genre_id"));
		music.setMoodId(rs.getInt("mood_id"));
		music.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
		
		music.setLikeCount(rs.getInt("like_count_cache"));
		music.setTotalPlayCount(rs.getInt("total_plays_cache"));
		
		Genre genre = new Genre();
		genre.setName(rs.getString("genre_name"));
		Mood mood = new Mood();
		mood.setName(rs.getString("mood_name"));
		
		music.setGenre(genre);
		music.setMood(mood);
		
		MusicDTO dto = MusicMapper.INSTANCE.toDTO(music);
		
		return dto;
	}

	public static MusicDetailDTO mapToMusicDetailDTO(ResultSet rs) throws SQLException {
		MusicDetailDTO dto = new MusicDetailDTO(rs.getString("artist_username"), rs.getString("artist_image_url"), mapToMusicDTO(rs));
		
		return dto;
	}
	
	public static Integer mapToInt(ResultSet rs) throws SQLException {
		return rs.getInt(1);
	}
}
