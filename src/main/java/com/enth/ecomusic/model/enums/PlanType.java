package com.enth.ecomusic.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum PlanType {
	LISTENER("listener"),
    CREATOR("creator");

    private final String value;

    private static final Map<String, PlanType> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (PlanType plan : values()) {
            VALUE_TO_ENUM.put(plan.value.toLowerCase(), plan);
        }
    }

    PlanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PlanType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Plan type cannot be null");
        }
        PlanType plan = VALUE_TO_ENUM.get(value.toLowerCase());
        if (plan == null) {
            throw new IllegalArgumentException("Unknown plan type: " + value);
        }
        return plan;
    }
}
