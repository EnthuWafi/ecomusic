package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class AudioStreamServlet
 */
@WebServlet("/stream/audio/*")
public class AudioStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MusicDAO musicDAO;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		musicDAO = new MusicDAO();
	}
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AudioStreamServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo(); // e.g., "/5" or "/edit/5" or null
		int musicId = CommonUtil.extractIdFromPath(pathInfo);
		
		if (musicId == -1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid music ID.");
			return;
		}
		
		Music music = musicDAO.getMusicById(musicId);

		String basePath = getServletContext().getAttribute("audioFilePath").toString();
		String filePath = basePath + music.getAudioFileUrl();

		File file = new File(filePath);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}


	    // Optional: permission check
	    // if (!userHasAccess(req.getUserPrincipal(), songId)) {
	    //     response.sendError(HttpServletResponse.SC_FORBIDDEN);
	    //     return;
	    // }

	    long length = file.length();
	    String rangeHeader = request.getHeader("Range");

	    try (RandomAccessFile input = new RandomAccessFile(file, "r");
	         OutputStream out = response.getOutputStream()) {

	        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
	            // Handle Range Request
	            String[] parts = rangeHeader.substring(6).split("-");
	            long start = Long.parseLong(parts[0]);
	            long end = (parts.length > 1 && !parts[1].isEmpty()) ? Long.parseLong(parts[1]) : length - 1;

	            if (end >= length) end = length - 1;
	            long contentLength = end - start + 1;

	            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
	            response.setHeader("Accept-Ranges", "bytes");
	            response.setContentType("audio/mpeg");
	            response.setContentLengthLong(contentLength);

	            input.seek(start);
	            byte[] buffer = new byte[4096];
	            long remaining = contentLength;
	            while (remaining > 0) {
	                int read = input.read(buffer, 0, (int) Math.min(buffer.length, remaining));
	                if (read == -1) break;
	                out.write(buffer, 0, read);
	                remaining -= read;
	            }

	        } else {
	            // Handle Full File
	            response.setContentType("audio/mpeg");
	            response.setContentLengthLong(length);
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = input.read(buffer)) != -1) {
	                out.write(buffer, 0, bytesRead);
	            }
	        }
	    }
	}


}
