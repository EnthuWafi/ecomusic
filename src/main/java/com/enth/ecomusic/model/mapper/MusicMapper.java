package com.enth.ecomusic.model.mapper;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Music;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MusicMapper {
    MusicMapper INSTANCE = Mappers.getMapper(MusicMapper.class);
    
    @Mapping(source = "genre.name", target = "genreName")
    @Mapping(source = "mood.name", target = "moodName")
    MusicDTO toDTO(Music music);
    
    Music toMusic(MusicDTO musicDTO);
    
    @Mapping(source = "music", target = "music")
    @Mapping(source = "music.artist.username", target = "artistUsername")
    @Mapping(source = "music.artist.imageUrl", target = "artistImageUrl")
    MusicDetailDTO toDetailDTO(Music music);
}