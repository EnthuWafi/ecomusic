package com.enth.ecomusic.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.fatboyindustrial.gsonjavatime.Converters;

public class JsonUtil {
    private static final Gson GSON = Converters.registerAll(new GsonBuilder()).create();;

    private JsonUtil() {  }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}
