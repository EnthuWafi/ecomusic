package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class MusicImageStreamServlet
 */
@WebServlet("/stream/image/music/*")
public class MusicImageStreamServlet extends HttpServlet {
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

		if (musicId == -1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid music ID.");
			return;
		}
		
		Music music = musicDAO.getMusicById(musicId);

		// Get image base path from context
		String basePath = getServletContext().getAttribute("musicImageFilePath").toString();
		String imagePath = basePath + music.getImageUrl();
		
		File imageFile = new File(imagePath);
		
		//no image found?
		if (!imageFile.exists()) {
			response.sendRedirect(request.getContextPath() + "/assets/images/default.jpg");
		    return;
		}

		String mimeType = getServletContext().getMimeType(imageFile.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setContentLengthLong(imageFile.length());

		try (FileInputStream fis = new FileInputStream(imageFile); OutputStream out = response.getOutputStream()) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}

}
