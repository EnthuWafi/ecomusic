package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.LikeDTO;
import com.enth.ecomusic.model.entity.Like;

@Mapper(uses = MusicMapper.class)
public interface LikeMapper {
	LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);
	
	@Mapping(source = "music", target = "music")
	LikeDTO toDTO(Like like);
	
}
