package com.enth.ecomusic.model.mapper;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MusicMapper {
    MusicMapper INSTANCE = Mappers.getMapper(MusicMapper.class);
    
    @Mapping(target = "genreName", ignore=true)
    @Mapping(target = "moodName", ignore=true)
    MusicDTO toDTO(Music music);
    
    @Mapping(target = "music", source = "music")
    @Mapping(source = "artist.username", target = "artistUsername")
    @Mapping(source = "artist.imageUrl", target = "artistImageUrl")
    @Mapping(source = "like_count", target = "likes")
    @Mapping(source = "view_count", target = "views")
    MusicDetailDTO toDetailDTO(Music music, User artist, int like_count, int view_count);
}