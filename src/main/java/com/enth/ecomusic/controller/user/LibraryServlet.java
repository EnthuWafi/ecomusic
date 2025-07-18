package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.LikeDTO;
import com.enth.ecomusic.model.dto.PlaylistCountDTO;
import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.LikeService;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class LibraryServlet
 */
@WebServlet("/user/library")
public class LibraryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlaylistService playlistService;
	private LikeService likeService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.playlistService = ctx.getPlaylistService();
		this.likeService = ctx.getLikeService();
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
		
		if (currentUser == null ) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		
		List<PlaylistCountDTO> playlists = playlistService.getUserPlaylistCountByUserId(currentUser.getUserId(), currentUser);
		int likeCount = likeService.getCountLikedSongByUserId(currentUser.getUserId());
		
		int limit = 3;
		int offset = 0;
		
		List<LikeDTO> likeList = likeService.getLikedSongsForUser(currentUser.getUserId(), offset, limit, currentUser.getUserId());
		
        request.setAttribute("pageTitle", "Library");
        request.setAttribute("playlists", playlists);
        request.setAttribute("likeCount", likeCount);
        request.setAttribute("likes", likeList);
        request.setAttribute("contentPage", "/WEB-INF/views/user/library.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
