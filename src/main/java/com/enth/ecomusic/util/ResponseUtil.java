package com.enth.ecomusic.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtil {
	public static void sendJson(HttpServletResponse response, Object data) throws IOException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("data", data);

        writeJson(response, HttpServletResponse.SC_OK, responseBody);
    }

    public static void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("error", message);

        writeJson(response, statusCode, responseBody);
    }

    private static void writeJson(HttpServletResponse response, int statusCode, Map<String, Object> body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.toJson(body));
    }
}
