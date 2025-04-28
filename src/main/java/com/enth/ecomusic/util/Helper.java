package com.enth.ecomusic.util;

import java.security.MessageDigest;

public class Helper {
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
    
}
