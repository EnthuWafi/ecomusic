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
import com.enth.ecomusic.model.entity.Playlist;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.model.mapper.PlaylistMapper;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class PlaylistServlet
 */
@WebServlet("/user/playlist/*")
public class PlaylistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PlaylistService playlistService;

	@Override
	public void init() throws ServletException {
		
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.playlistService = ctx.getPlaylistService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlaylistServlet() {
		super();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo(); // can be null or like / or /123 or /123/edit or /create
		if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
			viewPlaylistList(request, response);
			return;
		}

		String[] pathParts = pathInfo.split("/");
		try {
			if (pathParts.length == 2 && "create".equals(pathParts[1])) {
				createPlaylist(request, response);

			} else if (pathParts.length == 2) {
				int playlistId = Integer.parseInt(pathParts[1]);
				viewPlaylist(request, response, playlistId);

			} else if (pathParts.length == 3 && "edit".equals(pathParts[2])) {
				int playlistId = Integer.parseInt(pathParts[1]);
				editPlaylist(request, response, playlistId);

			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid playlist ID");
		}
	}

	private void createPlaylist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setAttribute("pageTitle", "Create Playlist");
		request.setAttribute("contentPage", "/WEB-INF/views/user/playlist-create.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);

	}

	private void editPlaylist(HttpServletRequest request, HttpServletResponse response, int playlistId)
			throws ServletException, IOException {
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		PlaylistDTO playlist = playlistService.getPlaylistByPlaylistId(playlistId, currentUser);

		request.setAttribute("playlist", playlist);
		request.setAttribute("pageTitle", "Edit Playlist");
		request.setAttribute("contentPage", "/WEB-INF/views/user/playlist-edit.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);

	}

	private void viewPlaylist(HttpServletRequest request, HttpServletResponse response, int playlistId)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		PlaylistDTO playlist = playlistService.getPlaylistWithMusicByPlaylistId(playlistId, currentUser);

		request.setAttribute("playlist", playlist);
		request.setAttribute("pageTitle", "Edit Playlist");
		request.setAttribute("contentPage", "/WEB-INF/views/user/playlist-view.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);

	}

	private void viewPlaylistList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		List<PlaylistDTO> playlistList = playlistService.getUserPlaylistWithMusicByUserId(currentUser.getUserId(), currentUser);

		request.setAttribute("playlists", playlistList);
		request.setAttribute("pageTitle", "All Playlist");
		request.setAttribute("contentPage", "/WEB-INF/views/user/view-playlist-list.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action");
			return;
		}

		String[] pathParts = pathInfo.split("/");

		try {
			if (pathParts.length == 2 && "create".equals(pathParts[1])) {
				createPlaylistPost(request, response);

			} else if (pathParts.length == 3 && "edit".equals(pathParts[2])) {
				int playlistId = Integer.parseInt(pathParts[1]);
				editPlaylistPost(request, response, playlistId);

			} else if (pathParts.length == 3 && "delete".equals(pathParts[2])) {
				int playlistId = Integer.parseInt(pathParts[1]);
				deletePlaylistPost(request, response, playlistId);

			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid playlist ID");
		}
	}

	private void createPlaylistPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		String name = request.getParameter("name");
		VisibilityType visibility = VisibilityType.fromString(request.getParameter("visibility"));

		if (name == null || name.trim().isEmpty()) {
			request.setAttribute("error", "Playlist name is required.");
			response.sendRedirect(request.getContextPath() + "/user/playlist/create");
			return;
		}
		
		if (visibility == null) {
		    request.setAttribute("error", "Invalid visibility type.");
		    response.sendRedirect(request.getContextPath() + "/user/playlist/create");
		    return;
		}

		Playlist newPlaylist = new Playlist();
		newPlaylist.setName(name.trim());
		newPlaylist.setVisibility(visibility);
		newPlaylist.setUserId(currentUser.getUserId());

		if (playlistService.addPlaylist(newPlaylist, currentUser)) {
			CommonUtil.addMessage(request.getSession(), ToastrType.SUCCESS, "Playlist added successfully");
			response.sendRedirect(request.getContextPath() + "/user/playlist");
		} else {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Playlist failed to be added");
			response.sendRedirect(request.getContextPath() + "/user/playlist/create");
		}

	}

	private void editPlaylistPost(HttpServletRequest request, HttpServletResponse response, int playlistId)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		PlaylistDTO existingPlaylist = playlistService.getPlaylistByPlaylistId(playlistId, currentUser);

		String name = request.getParameter("name");
		VisibilityType visibility = VisibilityType.fromString(request.getParameter("visibility"));

		if (name == null || name.trim().isEmpty()) {
			request.setAttribute("error", "Playlist name is required.");
			response.sendRedirect(request.getContextPath() + "/user/playlist/" + playlistId + "/edit");
			return;
		}
		if (visibility == null) {
		    request.setAttribute("error", "Invalid visibility type.");
		    response.sendRedirect(request.getContextPath() + "/user/playlist/" + playlistId + "/edit");
		    return;
		}

		Playlist newPlaylist = PlaylistMapper.INSTANCE.toPlaylist(existingPlaylist);
		newPlaylist.setName(name.trim());
		newPlaylist.setVisibility(visibility);

		if (playlistService.updatePlaylist(newPlaylist, currentUser)) {
			CommonUtil.addMessage(request.getSession(), ToastrType.SUCCESS, "Playlist modified successfully");
			response.sendRedirect(request.getContextPath() + "/user/playlist/" + playlistId);
		} else {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Playlist failed to be modified");
			response.sendRedirect(request.getContextPath() + "/user/playlist/" + playlistId);
		}
	}

	private void deletePlaylistPost(HttpServletRequest request, HttpServletResponse response, int playlistId)
			throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		
		if (playlistService.removePlaylist(playlistId, currentUser)) {
			CommonUtil.addMessage(request.getSession(), ToastrType.SUCCESS, "Playlist removed successfully");
			response.sendRedirect(request.getContextPath() + "/user/playlist");
		} else {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Playlist failed to be removed");
			response.sendRedirect(request.getContextPath() + "/user/playlist/" + playlistId);
		}
		
		

	}

	
}
