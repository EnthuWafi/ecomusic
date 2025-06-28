package com.enth.ecomusic.model.mapper;

import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.ReportDTO;

public interface ReportMapper {
	ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);
	
	ReportDTO toDTO(int totalUser, int musicCount, int activeSubscriptionCount, int revenueCount, int registeredUsersToday, int musicUploadedToday);
	
}
