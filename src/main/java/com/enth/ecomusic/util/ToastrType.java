package com.enth.ecomusic.util;

public enum ToastrType {
	SUCCESS("success"),
    INFO("info"),
    WARNING("warning"),
    ERROR("error");

    private final String value;

    ToastrType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
