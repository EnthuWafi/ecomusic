package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.StreamRangeDTO;
import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.FileStreamingUtil;

/**
 * Servlet implementation class MusicImageStreamServlet
 */
@WebServlet("/stream/image/music/*")
public class MusicImageStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MusicService musicService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		GenreCacheService genreCacheService = (GenreCacheService) this.getServletContext().getAttribute("genreCacheService");
		MoodCacheService moodCacheService = (MoodCacheService) this.getServletContext().getAttribute("moodCacheService");
		this.musicService = new MusicService(genreCacheService, moodCacheService);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MusicImageStreamServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo(); // e.g., "/42"
		int musicId = CommonUtil.extractIdFromPath(pathInfo);
		
		String requestedSize = request.getParameter("size"); 

		if (musicId == -1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid music ID.");
			return;
		}
		
		boolean isThumbnailRequest = "thumb".equalsIgnoreCase(requestedSize);
		
		MusicDTO music = musicService.getMusicDTOById(musicId);

		String basePath = AppConfig.get("musicImageFilePath");
		
		File imageFile = null;
		if (isThumbnailRequest) {
			imageFile = new File(basePath + "thumb_" + music.getImageUrl());
		}
		else {
			imageFile = new File(basePath + music.getImageUrl());
		}

	    if (!imageFile.exists()) {
	        response.sendRedirect(request.getContextPath() + "/assets/images/default-music.jpg");
	        return;
	    }

	    String mimeType = getServletContext().getMimeType(imageFile.getName());
	    if (mimeType == null) {
	        mimeType = "application/octet-stream";
	    }

	    String rangeHeader = request.getHeader("Range");

	    StreamRangeDTO range = FileStreamingUtil.parseRangeHeader(rangeHeader, imageFile.length());
	    FileStreamingUtil.streamFile(imageFile, range, response, mimeType);
	}

}
