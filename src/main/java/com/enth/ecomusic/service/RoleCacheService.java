package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.RoleDAO;
import com.enth.ecomusic.model.entity.Role;
import com.enth.ecomusic.model.enums.RoleType;

public class RoleCacheService {
	private RoleDAO roleDAO;
    private Map<Integer, Role> roleById;
    private Map<RoleType, Role> roleByType;

    public RoleCacheService() {
         refresh();
    }
    
    public void refresh() {
    	if (roleDAO == null) {
    		roleDAO = new RoleDAO();
    	}
    	List<Role> roles = roleDAO.getAllRoles();

        this.roleById = new ConcurrentHashMap<>(roles.stream()
                .collect(Collectors.toMap(role -> role.getRoleId(), role -> role)));

        this.roleByType = new ConcurrentHashMap<>(roles.stream()
                .filter(role -> RoleType.fromString(role.getRoleName()) != null)
                .collect(Collectors.toMap(
                        role -> RoleType.fromString(role.getRoleName()),
                        role -> role
                )));
    }

    public Role getById(int id) {
        return roleById.get(id);
    }

    public Role getByType(RoleType type) {
		return roleByType.get(type);
	}
    
    public List<Role> getAll() {
		return List.copyOf(roleById.values());
	}
    

}