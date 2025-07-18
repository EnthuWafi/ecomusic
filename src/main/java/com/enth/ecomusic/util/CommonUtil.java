package com.enth.ecomusic.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CommonUtil {

	private CommonUtil() {}
	
	public static int parseIntLimitParam(String raw, int def, int max) {
	    if (StringUtils.isNumeric(raw)) {
	        int val = Integer.parseInt(raw);
	        return Math.min(val, max);
	    }
	    return def;
	}
	
	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public static String clobToString(Clob clobObject) throws SQLException, IOException {
		if (clobObject == null) {
			return null;
		}
		try (Reader reader = clobObject.getCharacterStream(); StringWriter writer = new StringWriter()) {
			IOUtils.copy(reader, writer);
			return writer.toString();
		}
	}

	// Hashing
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

	public static Integer extractIdFromPath(String pathInfo) {
		if (pathInfo == null || pathInfo.isBlank()) {
			return -1;
		}

		String[] parts = pathInfo.split("/");

		for (int i = parts.length - 1; i >= 0; i--) {
			if (parts[i] != null && !parts[i].isBlank()) {
				try {
					return Integer.parseInt(parts[i]);
				} catch (NumberFormatException e) {
				}
			}
		}

		return -1;
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();

		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		// Append port if it's not the default 80 (http) or 443 (https)
		if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath);

		return url.toString();
	}

	// Flash toast
	@SuppressWarnings("unchecked")
	public static void addMessage(HttpSession session, ToastrType type, String message) {
		List<Map<String, String>> messages = (List<Map<String, String>>) session.getAttribute("flash_messages");
		if (messages == null) {
			messages = new ArrayList<>();
		}
		Map<String, String> msg = new HashMap<>();
		msg.put("type", type.getValue()); // e.g., error, success, info, warning
		msg.put("message", message);
		messages.add(msg);
		session.setAttribute("flash_messages", messages);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> extractMessages(HttpSession session) {
		List<Map<String, String>> messages = (List<Map<String, String>>) session.getAttribute("flash_messages");
		if (messages != null) {
			session.removeAttribute("flash_messages");
	        return messages;
	    }
		return new ArrayList<>();
	}

}
