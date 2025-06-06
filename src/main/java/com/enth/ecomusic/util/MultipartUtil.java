package com.enth.ecomusic.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

import jakarta.servlet.http.Part;

public class MultipartUtil {
	
	private MultipartUtil() {}
	/** 
     * Read the entire Part as a UTF-8 String (or return "" on error/null). 
     */
    public static String getString(Part part) {
        if (part == null) {
            return "";
        }
        try {
            return IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Parse the Part’s content (as text) into an int. 
     * Falls back to defaultValue if null/empty/invalid.
     */
    public static int getInt(Part part, int defaultValue) {
        return NumberUtils.toInt(getString(part), defaultValue);
    }

    /**
     * If you ever need a boolean checkbox “premiumContent” style:
     * returns true if part exists and its text content equalsIgnoreCase("true")
     */
    public static boolean getBoolean(Part part) {
        String val = getString(part);
        return "true".equalsIgnoreCase(val);
    }
}
