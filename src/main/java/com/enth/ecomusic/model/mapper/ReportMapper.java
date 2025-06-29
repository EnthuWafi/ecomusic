package com.enth.ecomusic.model.mapper;

import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.ReportKPIDTO;

public interface ReportMapper {
	ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);
	
}
