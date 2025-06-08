package com.enth.ecomusic.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum VisibilityType {
	PUBLIC("public"),
	PRIVATE("private");
	
	private final String value;
	private static final Map<String, VisibilityType> VALUE_TO_ENUM = new HashMap<>();
	
	static {
		for (VisibilityType visibility : values()) {
			VALUE_TO_ENUM.put(visibility.value.toLowerCase(), visibility);
		}
	}
	
	VisibilityType(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static VisibilityType fromString(String value) {
		VisibilityType visibilityType = VALUE_TO_ENUM.get(value.toLowerCase());
		if (visibilityType == null) {
			throw new IllegalArgumentException("Unknown visibility type: " + value);
		}
		return visibilityType;
	}
}
