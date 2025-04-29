package com.enth.ecomusic.controller.artist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.util.Helper;

/**
 * Servlet implementation class ArtistMusicServlet
 */
@WebServlet("/artist/music/*")
public class ArtistMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MusicDAO musicDAO;

	@Override
	public void init() {
		musicDAO = new MusicDAO(); 
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String genre = request.getParameter("genre");
		String description = request.getParameter("description");
		String audioFileUrl = request.getParameter("audioFileUrl");
		boolean premiumContent = Boolean.parseBoolean(request.getParameter("premiumContent"));
		// TODO: Artist later
		Music newMusic = new Music(1, title, genre, description, audioFileUrl, premiumContent);

		// Insert music into database
		boolean success = musicDAO.insertMusic(newMusic);

		// Redirect based on success or failure
		if (success) {
			Helper.addMessage(request.getSession(), "success", "Music uploaded!");
			response.sendRedirect(request.getContextPath() + "/artist/music"); // Redirect to music list page
		}
		else {
			Helper.addMessage(request.getSession(), "error", "Music not uploaded!");
			response.sendRedirect(request.getContextPath() + "/artist/music");
		}
	}

}
