package com.enth.ecomusic.controller.user;

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
 * Servlet implementation class UserMusicServlet
 */
@WebServlet("/music/*")
public class UserMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MusicDAO musicDAO;

	@Override
	public void init() {
		musicDAO = new MusicDAO(); 
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Music> musicList = musicDAO.getAllMusic();
		
		request.setAttribute("musicList", musicList);
		request.setAttribute("pageTitle", "Music Library");
		request.setAttribute("contentPage", "/WEB-INF/views/common/musicLibrary.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
