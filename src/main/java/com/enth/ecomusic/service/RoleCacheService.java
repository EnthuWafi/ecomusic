package com.enth.ecomusic.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.enth.ecomusic.dao.RoleDAO;
import com.enth.ecomusic.model.entity.Role;
import com.enth.ecomusic.model.enums.RoleType;

public class RoleCacheService {
	private RoleDAO roleDAO;
    private final Map<Integer, Role> roleById = new ConcurrentHashMap<>();
    private final Map<RoleType, Role> roleByType = new ConcurrentHashMap<>();

    public RoleCacheService() {
         refresh();
    }
    
    public synchronized void refresh() {
    	if (roleDAO == null) {
    		roleDAO = new RoleDAO();
    	}
    	List<Role> roles = roleDAO.getAllRoles();

    	Map<Integer, Role> roleById = new ConcurrentHashMap<>(roles.stream()
                .collect(Collectors.toMap(role -> role.getRoleId(), role -> role)));

    	Map<RoleType, Role>  roleByType = new ConcurrentHashMap<>(roles.stream()
                .filter(role -> RoleType.fromString(role.getRoleName()) != null)
                .collect(Collectors.toMap(
                        role -> RoleType.fromString(role.getRoleName()),
                        role -> role
                )));
    	
    	this.roleById.clear();
    	this.roleById.putAll(roleById);
    	
    	this.roleByType.clear();
    	this.roleByType.putAll(roleByType);
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