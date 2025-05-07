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

/**
 * Servlet implementation class SearchMusicServlet
 */
@WebServlet("/music/search")
public class SearchMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MusicDAO musicDAO;

    @Override
    public void init() {
        musicDAO = new MusicDAO();
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("q"); // e.g., ?q=eminem
        List<Music> musicList;

        if (query != null && !query.trim().isEmpty()) {
            musicList = musicDAO.searchMusicByTitleOrArtist(query.trim());
        } else {
            musicList = musicDAO.getAllMusic(); // fallback to full list
        }

        request.setAttribute("musicList", musicList);
        request.setAttribute("searchQuery", query);
        request.setAttribute("pageTitle", "Search Results");
        request.setAttribute("contentPage", "/WEB-INF/views/common/browse-music.jsp");

        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
