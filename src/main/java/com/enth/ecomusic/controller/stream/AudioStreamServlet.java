package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.StreamRangeDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.FileStreamingService;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class AudioStreamServlet
 */
@WebServlet("/stream/audio/*")
public class AudioStreamServlet extends HttpServlet {
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
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		MusicDTO music = musicService.getMusicDTOById(musicId, currentUser);

		if (music == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		String basePath = getServletContext().getAttribute("audioFilePath").toString();
		String filePath = basePath + music.getAudioFileUrl();

		File file = new File(filePath);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Optional: permission check
		// if (!userHasAccess(req.getUserPrincipal(), songId)) {
		// response.sendError(HttpServletResponse.SC_FORBIDDEN);
		// return;
		// }
		String mimeType = getServletContext().getMimeType(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		String rangeHeader = request.getHeader("Range");

		StreamRangeDTO range = fileStreamingService.parseRangeHeader(rangeHeader, file.length());
		
		response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(mimeType);
        response.setContentLengthLong(range.getContentLength());

        if (range.getStart() > 0 || range.getEnd() < range.getTotalLength() - 1) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + range.getStart() + "-" + range.getEnd() + "/" + range.getTotalLength());
        }
        
        try (OutputStream out = response.getOutputStream()) {
            fileStreamingService.streamFile(file, range, out);
        }
	}

}
