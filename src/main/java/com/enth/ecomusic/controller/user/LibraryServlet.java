package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class LibraryServlet
 */
@WebServlet("/user/library")
public class LibraryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlaylistService playlistService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.playlistService = ctx.getPlaylistService();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LibraryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		List<PlaylistDTO> playlists = playlistService.getUserPlaylistWithMusicByUserId(currentUser.getUserId(), currentUser);
		
        request.setAttribute("pageTitle", "Library");
        request.setAttribute("playlists", playlists);
        request.setAttribute("contentPage", "/WEB-INF/views/user/library.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
