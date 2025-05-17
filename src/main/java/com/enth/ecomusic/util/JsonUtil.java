package com.enth.ecomusic.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    // build once, reuse forever
    private static final Gson GSON = new Gson();

    private JsonUtil() {  }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}
