package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Like;
import com.enth.ecomusic.service.LikeService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class LikeAPIServlet
 */
@WebServlet("/api/like/*")
public class LikeAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LikeService likeService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LikeAPIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.likeService = ctx.getLikeService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Music ID is required in the path");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int musicId = Integer.parseInt(pathParts[0]);
				getLikeRecord(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void getLikeRecord(int musicId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}
		
		boolean liked = likeService.isSongLikedByUser(currentUser.getUserId(), musicId);
		
		if (liked) {
			ResponseUtil.sendJson(response, true);
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot retrieve a like music record");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Music ID is required in the path");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int musicId = Integer.parseInt(pathParts[0]);
				createLikeRecord(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void createLikeRecord(int musicId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		Like like = new Like();
		like.setMusicId(musicId);
		like.setUserId(currentUser.getUserId());

		boolean success = likeService.likeSong(like, currentUser);

		if (success) {
			ResponseUtil.sendJson(response, "Successfully created a like record");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot create a like music record");
		}

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Music ID is required");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int musicId = Integer.parseInt(pathParts[0]);
				deleteLike(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void deleteLike(int musicId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		boolean success = likeService.unlikeSong(currentUser.getUserId(), musicId, currentUser);
		if (success) {
			ResponseUtil.sendJson(response, "Successfully deleted a like record");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot delete a like music record");
		}
	}
}
