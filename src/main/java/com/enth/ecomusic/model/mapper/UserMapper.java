package com.enth.ecomusic.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.User;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "roleName", ignore=true)
	UserDTO toDTO(User user);
}
