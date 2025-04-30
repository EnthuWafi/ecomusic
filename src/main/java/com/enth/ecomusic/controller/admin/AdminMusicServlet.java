package com.enth.ecomusic.controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;

@WebServlet("/admin/music/*")
public class AdminMusicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MusicDAO musicDAO;

	@Override
	public void init() {
		musicDAO = new MusicDAO(); 
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Music> musicList = musicDAO.getAllMusic();
		request.setAttribute("musicList", musicList);
		request.setAttribute("pageTitle", "Music List");
		request.setAttribute("contentPage", "/WEB-INF/views/admin/viewMusicList.jsp");

		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
