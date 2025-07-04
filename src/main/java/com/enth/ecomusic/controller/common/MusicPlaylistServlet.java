package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;


/**
 * Servlet implementation class MusicPlaylistServlet
 */
@WebServlet("/playlist/play/*")
public class MusicPlaylistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MusicPlaylistServlet() {
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
		String pathInfo = request.getPathInfo();

		if (StringUtils.isNotEmpty(pathInfo) && pathInfo.matches("/\\d+")) {
			request.setAttribute("pageTitle", "Listen to Playlist Music");
			request.setAttribute("contentPage", "/WEB-INF/views/common/play-playlist.jsp");
			request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

}
