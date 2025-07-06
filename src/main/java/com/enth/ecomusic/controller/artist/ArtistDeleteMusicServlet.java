package com.enth.ecomusic.controller.artist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ArtistDeleteMusicServlet
 */
@WebServlet("/artist/music/delete/*")
public class ArtistDeleteMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MusicService musicService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ArtistDeleteMusicServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/artist/music");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		String pathInfo = request.getPathInfo(); 
		Integer musicId = CommonUtil.extractIdFromPath(pathInfo);
		
		Music music = musicService.getMusicById(musicId);

		if (music == null) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Music not found");
			response.sendRedirect(request.getContextPath() + "/artist/music");
			return;
		}

		// Attempt to delete
		boolean success = musicService.deleteMusic(music, currentUser);

		if (success) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Music deleted successfully");
			response.sendRedirect(request.getContextPath() + "/artist/music");
		} else {
			CommonUtil.addMessage(session, ToastrType.ERROR, "You are not allowed to delete this music");
			response.sendRedirect(request.getContextPath() + "/artist/music");
		}
	}

}
