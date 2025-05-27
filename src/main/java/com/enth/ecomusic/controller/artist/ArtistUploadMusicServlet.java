package com.enth.ecomusic.controller.artist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;

import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ArtistUploadMusicServlet
 */
@WebServlet("/artist/music/upload")
public class ArtistUploadMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistUploadMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Forward to the upload form JSP
        request.setAttribute("pageTitle", "Upload Music");
        request.setAttribute("contentPage", "/WEB-INF/views/artist/upload-music.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		HttpSession session = request.getSession();
		
	    int artistId = (int) request.getSession().getAttribute("userId"); // Or however you track login
	    String title = request.getParameter("title");
	    String genre = request.getParameter("genre");
	    String description = request.getParameter("description");
	    boolean isPremium = request.getParameter("premium") != null;

	    Part audioPart = request.getPart("file");

	    MusicService musicService = new MusicService();
	    boolean success = musicService.uploadMusic(artistId, title, genre, description, audioPart, isPremium);

	    if (success) {
	    	CommonUtil.addMessage(session, ToastrType.SUCCESS, "Music successfully uploaded");
	        response.sendRedirect(request.getContextPath() + "/artist/music");
	    } else {
	    	CommonUtil.addMessage(session, ToastrType.ERROR, "Music failed to upload!");
	    	response.sendRedirect(request.getContextPath() + "/artist/music/upload");
	    }
	}

}
