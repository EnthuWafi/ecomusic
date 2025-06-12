package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.entity.UserSubscription;

@Mapper
public interface SubscriptionMapper {
	SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);
	
	SubscriptionDTO toDTO(UserSubscription subscription);
	
	SubscriptionPlanDTO toDTO(SubscriptionPlan subscriptionPlan);
}
