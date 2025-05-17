package com.enth.ecomusic.model;

import java.util.HashMap;
import java.util.Map;

public enum RoleType {
	SUPERADMIN("superadmin"),
	ADMIN("admin"),
	ARTIST("artist"),
	USER("user");
	
    private final String value;


	private static final Map<String, RoleType> VALUE_TO_ENUM = new HashMap<>();

	static {
		for (RoleType role : values()) {
			VALUE_TO_ENUM.put(role.value.toLowerCase(), role);
		}
	}
    
    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static RoleType fromString(String value) {
		RoleType role = VALUE_TO_ENUM.get(value.toLowerCase());
		if (role == null) {
			throw new IllegalArgumentException("Unknown role type: " + value);
		}
		return role;
	}
}
