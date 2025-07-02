package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.service.PlayHistoryService;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ChannelServlet
 */
@WebServlet("/channel/*")
public class ChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private MusicService musicService;
	private PlayHistoryService playService;
	private PlaylistService playlistService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.userService = ctx.getUserService();
		this.musicService = ctx.getMusicService();
		this.playService = ctx.getPlayHistoryService();
		this.playlistService = ctx.getPlaylistService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChannelServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// This is going to display info about this user channel

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				showChannel(userId, request, response);
			} else if (pathParts.length == 2 && "music".equals(pathParts[1])) {
				int userId = Integer.parseInt(pathParts[0]);
				showChannelMusic(userId, request, response);
			} else if (pathParts.length == 2 && "playlist".equals(pathParts[1])) {
				int userId = Integer.parseInt(pathParts[0]);
				showChannelPlaylist(userId, request, response);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path does not exist");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}

	}

	private void showChannelPlaylist(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		UserDTO user = userService.getUserDTOById(userId);

		if (user == null) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "User with that channel does not exist");
			response.sendRedirect(request.getContextPath());
			return;
		}

		if (user.isAdmin() || user.isSuperAdmin()) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Admin does not have a channel");
			response.sendRedirect(request.getContextPath());
			return;
		}

		int currentUserId = 0;
		if (currentUser != null) {
			currentUserId = currentUser.getUserId();
		}
		
		List<PlaylistDTO> playlistList = playlistService.getUserPlaylistByUserId(user.getUserId(), currentUser);
		int musicCount = musicService.getMusicCountByArtist(user.getUserId(), currentUserId);
		int totalPlayCount = musicService.getTotalPlayCountByArtist(user.getUserId());
		int playlistCount = playlistService.getPlaylistCountByArtist(user.getUserId(), currentUserId);

		request.setAttribute("pageTitle", user.getUsername() + "'s Channel");

		request.setAttribute("artist", user);
		request.setAttribute("musicCount", musicCount);
		request.setAttribute("playsCount", totalPlayCount);

		request.setAttribute("contentPage", "/WEB-INF/views/common/channel-playlist.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);

	}

	private void showChannelMusic(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		UserDTO user = userService.getUserDTOById(userId);

		if (user == null) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "User with that channel does not exist");
			response.sendRedirect(request.getContextPath());
			return;
		}

		if (user.isAdmin() || user.isSuperAdmin()) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Admin does not have a channel");
			response.sendRedirect(request.getContextPath());
			return;
		}

		int currentUserId = 0;
		if (currentUser != null) {
			currentUserId = currentUser.getUserId();
		}
		
		List<MusicDTO> recentMusic = musicService.getPaginatedMusicDTOByArtistId(currentUserId, user.getUserId(), 1, 5);
		int musicCount = musicService.getMusicCountByArtist(user.getUserId(), currentUserId);
		int totalPlayCount = musicService.getTotalPlayCountByArtist(user.getUserId());
		int playlistCount = playlistService.getPlaylistCountByArtist(user.getUserId(), currentUserId);

		request.setAttribute("pageTitle", user.getUsername() + "'s Channel");

		request.setAttribute("artist", user);
		request.setAttribute("musicCount", musicCount);
		request.setAttribute("playsCount", totalPlayCount);
		request.setAttribute("musicList", recentMusic);
		request.setAttribute("playlistCount", playlistCount);

		request.setAttribute("contentPage", "/WEB-INF/views/common/channel-music.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);

	}

	private void showChannel(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		UserDTO user = userService.getUserDTOById(userId);

		if (user == null) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "User with that channel does not exist");
			response.sendRedirect(request.getContextPath());
			return;
		}

		if (user.isAdmin() || user.isSuperAdmin()) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Admin does not have a channel");
			response.sendRedirect(request.getContextPath());
			return;
		}

		int currentUserId = 0;
		if (currentUser != null) {
			currentUserId = currentUser.getUserId();
		}

		List<MusicDTO> recentMusic = musicService.getPaginatedMusicDTOByArtistId(currentUserId, user.getUserId(), 1, 5);
		int musicCount = musicService.getMusicCountByArtist(user.getUserId(), currentUserId);
		int totalPlayCount = musicService.getTotalPlayCountByArtist(user.getUserId());
		int playlistCount = playlistService.getPlaylistCountByArtist(user.getUserId(), currentUserId);

		//get fans data
		
		
		
		request.setAttribute("pageTitle", user.getUsername() + "'s Channel");

		request.setAttribute("artist", user);
		request.setAttribute("musicCount", musicCount);
		request.setAttribute("playsCount", totalPlayCount);
		request.setAttribute("musicList", recentMusic);
		request.setAttribute("playlistCount", playlistCount);

		request.setAttribute("contentPage", "/WEB-INF/views/common/channel.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}

}
