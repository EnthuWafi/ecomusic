package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.PlayHistoryDTO;
import com.enth.ecomusic.model.entity.PlayHistory;

@Mapper(uses = MusicMapper.class)
public interface PlayHistoryMapper {
	PlayHistoryMapper INSTANCE = Mappers.getMapper(PlayHistoryMapper.class);
	
	@Mapping(source = "music", target = "music")
	PlayHistoryDTO toDTO(PlayHistory playHistory);
}
