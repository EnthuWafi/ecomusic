package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.PlaylistDetailDTO;
import com.enth.ecomusic.model.dto.PlaylistMusicDTO;
import com.enth.ecomusic.model.entity.Playlist;
import com.enth.ecomusic.model.entity.PlaylistMusic;

@Mapper(uses = MusicMapper.class)
public interface PlaylistMapper {
	PlaylistMapper INSTANCE = Mappers.getMapper(PlaylistMapper.class);
	
	@Mapping(source = "musicList", target = "musicList")
	PlaylistDTO toDTO(Playlist playlist);
	
	@Mapping(source = "music", target = "music")
	@Mapping(source = "music.artist.username", target = "artistUsername")
    @Mapping(source = "music.artist.imageUrl", target = "artistImageUrl")
	PlaylistMusicDTO toDTO(PlaylistMusic playlistMusic);

	@Mapping(source = "musicList", target = "musicList")
	Playlist toPlaylist(PlaylistDTO existingPlaylist);
	
	@Mapping(source = "music", target = "music")
	PlaylistMusic toPlaylistMusic(PlaylistMusicDTO playlistMusic);
	
}
