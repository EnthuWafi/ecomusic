package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class AudioStreamServlet
 */
@WebServlet("/stream/*")
public class AudioStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		int id = CommonUtil.extractIdFromPath(pathInfo);

		String basePath = getServletContext().getAttribute("audioFilePath").toString();
		String filePath = basePath + id + ".mp3";

		File file = new File(filePath);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// auth check here
		// if (!userHasAccess(req.getUserPrincipal(), songId)) {
		// resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		// return;
		// }

		response.setContentType("audio/mpeg");
		response.setContentLengthLong(file.length());

		try (FileInputStream fis = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
			fis.transferTo(out);
		}
	}


}
