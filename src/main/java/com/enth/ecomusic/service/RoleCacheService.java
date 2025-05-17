package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.Role;
import com.enth.ecomusic.model.RoleType;
import com.enth.ecomusic.model.dao.RoleDAO;

public class RoleCacheService {
	private final RoleDAO roleDAO;
    private final Map<Integer, Role> roleById;
    private final Map<RoleType, Role> roleByType;

    public RoleCacheService() {
    	this.roleDAO = new RoleDAO();
    	
        List<Role> roles = roleDAO.getAllRoles();
        this.roleById = roles.stream().collect(Collectors.toMap(
            Role::getRoleId,
            role -> role
        ));
        
        this.roleByType = roles.stream()
    			.filter(role -> RoleType.fromString(role.getRoleName()) != null)
    			.collect(Collectors.toMap(
    				role -> RoleType.fromString(role.getRoleName()),
    				role -> role
    			));
    }

    public Role getById(int id) {
        return roleById.get(id);
    }

    public Role getByType(RoleType type) {
		return roleByType.get(type);
	}

}