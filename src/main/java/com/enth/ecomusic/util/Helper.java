package com.enth.ecomusic.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

public class Helper {
	//Hashing
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean checkPassword(String password, String storedHash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(storedHash);
    }
    
    // Path-ing
    
    public static int extractIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isBlank()) {
            return -1;
        }
        
        String[] parts = pathInfo.split("/");
        
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i] != null && !parts[i].isBlank()) {
                try {
                    return Integer.parseInt(parts[i]);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        
        return -1;
    }
    
    // Flash toast
    @SuppressWarnings("unchecked")
    public static void addMessage(HttpSession session, String type, String message) {
        List<Map<String, String>> messages = (List<Map<String, String>>) session.getAttribute("flash_messages");
        if (messages == null) {
            messages = new ArrayList<>();
        }
        Map<String, String> msg = new HashMap<>();
        msg.put("type", type); // e.g., error, success, info, warning
        msg.put("message", message);
        messages.add(msg);
        session.setAttribute("flash_messages", messages);
    }
    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> clearMessages(HttpSession session) {
        List<Map<String, String>> messages = (List<Map<String, String>>) session.getAttribute("flash_messages");
        session.removeAttribute("flash_messages");
        return messages != null ? messages : new ArrayList<>();
    }
}
