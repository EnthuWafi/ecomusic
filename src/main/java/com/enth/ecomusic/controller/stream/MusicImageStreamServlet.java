package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.StreamRangeDTO;
import com.enth.ecomusic.service.FileStreamingService;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class MusicImageStreamServlet
 */
@WebServlet("/stream/image/music/*")
public class MusicImageStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MusicService musicService;
	private FileStreamingService fileStreamingService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
		
		this.fileStreamingService = new FileStreamingService();
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
	    
	    StreamRangeDTO range = fileStreamingService.parseRangeHeader(rangeHeader, imageFile.length());
	    
		response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(mimeType);
        response.setContentLengthLong(range.getContentLength());

        if (range.getStart() > 0 || range.getEnd() < range.getTotalLength() - 1) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + range.getStart() + "-" + range.getEnd() + "/" + range.getTotalLength());
        }
        
        try (OutputStream out = response.getOutputStream()) {
            fileStreamingService.streamFile(imageFile, range, out);
        }
	}

}
