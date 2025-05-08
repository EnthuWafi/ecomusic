package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.util.CommonUtil;

@WebServlet("/music")
public class BrowseMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MusicDAO musicDAO;

	@Override
	public void init() {
		musicDAO = new MusicDAO();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BrowseMusicServlet() {
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
		
        int page = 1;
        int pageSize = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Music> musicList = musicDAO.getPaginatedMusic(page, pageSize);
        int totalRecords = musicDAO.countProducts(); // Implement countProducts()
        int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

		request.setAttribute("musicList", musicList);
		request.setAttribute("pageTitle", "Browse Music");
		request.setAttribute("contentPage", "/WEB-INF/views/common/browse-music.jsp");

		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}
}
