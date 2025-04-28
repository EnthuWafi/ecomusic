package com.enth.ecomusic.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;

@WebServlet("/music")
public class MusicServlet extends HttpServlet {
	
	private MusicDAO musicDAO;
	
	@Override
    public void init() {
        musicDAO = new MusicDAO();  // Initialize DAO
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String action = request.getParameter("action");

        if ("add".equals(action)) {
            // call addMusic()
			request.setAttribute("pageTitle", "Add Music");
			request.setAttribute("contentPage", "/WEB-INF/views/addMusic.jsp");
			
        } else if ("delete".equals(action)) {
            // call deleteMusic()
        }
        // ... more actions
        else {

			List<Music> musicList = musicDAO.getAllMusic();
			request.setAttribute("musicList", musicList);
			request.setAttribute("pageTitle", "Music List");
			request.setAttribute("contentPage", "/WEB-INF/views/musicList.jsp");
			
        }
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String description = request.getParameter("description");
        String audioFileUrl = request.getParameter("audioFileUrl");
        boolean premiumContent = Boolean.parseBoolean(request.getParameter("premiumContent")); 
        //TODO: Artist later
        // Create Music object 
        Music newMusic = new Music(1, title, genre, description, audioFileUrl, premiumContent);
        
        // Insert music into database
        boolean success = musicDAO.insertMusic(newMusic);
        
        // Redirect based on success or failure
        if (success) {
            response.sendRedirect("music");  // Redirect to music list page
        } else {
            request.setAttribute("errorMessage", "Failed to add music.");
            request.setAttribute("pageTitle", "Add Music");
            request.setAttribute("contentPage", "/WEB-INF/views/addMusic.jsp");
            request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
        }
	}
}
