package com.enth.ecomusic.util;

import java.util.Map;

public class FileTypeUtil {
	private static final Map<String, String> IMAGE_MIME_TO_EXT = Map.ofEntries(
	        Map.entry("image/jpeg", ".jpg"),
	        Map.entry("image/png", ".png"),
	        Map.entry("image/gif", ".gif"),
	        Map.entry("image/webp", ".webp")
	    );
	
	private static final Map<String, String> AUDIO_MIME_TO_EXT = Map.ofEntries(
	        Map.entry("audio/mpeg", ".mp3"),
	        Map.entry("audio/wav", ".wav"),
	        Map.entry("audio/ogg", ".ogg"),
	        Map.entry("audio/x-wav", ".wav"),
	        Map.entry("audio/x-m4a", ".m4a")
	    );
	    
	    public static String getImageExtension(String mimeType) {
	        return IMAGE_MIME_TO_EXT.getOrDefault(mimeType, ".img");
	    }
	    public static String getAudioExtension(String mimeType) {
	        return AUDIO_MIME_TO_EXT.get(mimeType);
	    }
}
