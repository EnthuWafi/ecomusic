package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.PlaylistMusicDTO;
import com.enth.ecomusic.model.entity.Playlist;
import com.enth.ecomusic.model.entity.PlaylistMusic;

@Mapper(uses = MusicMapper.class)
public interface PlaylistMapper {
	PlaylistMapper INSTANCE = Mappers.getMapper(PlaylistMapper.class);
	
	@Mapping(source = "playlistMusic", target = "playlistMusic")
	PlaylistDTO toDTO(Playlist playlist);
	
	@Mapping(source = "music", target = "musicDetail")
	PlaylistMusicDTO toDTO(PlaylistMusic playlistMusic);
}
