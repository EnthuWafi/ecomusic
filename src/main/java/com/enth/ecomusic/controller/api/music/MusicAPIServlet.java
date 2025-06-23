package com.enth.ecomusic.controller.api.music;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.MusicSearchDTO;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class MusicAPIServlet
 */
@WebServlet("/api/music/*")
public class MusicAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MusicService musicService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MusicAPIServlet() {
		super();
		// TODO Auto-generated constructor stub
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
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Music ID is required");
			return;
		}

		String[] pathParts = pathInfo.split("/");

		try {
			if ((pathParts.length == 2) && ("search".equals(pathParts[1]))) {
				handleFetchSearch(request, response);
			} else if (pathParts.length == 2) {
				handleFetchMusic(pathParts[1], request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchSearch(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("q");
		String limitStr = StringUtils.defaultString(request.getParameter("limit")).trim();
		
		String cleanQuery = query != null ? query.trim().replaceAll("\\s+", " ").replaceAll("[&|*!~{}]", "") : null;
		if (StringUtils.isBlank(cleanQuery)) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "q cannot be empty");
			return;
		}
		
		int limit = 8;
		if (StringUtils.isNumeric(limitStr)) {
			limit = Integer.parseInt(limitStr);
			if (limit > 20) {
				limit = 20;
			}
		}

		List<MusicSearchDTO> musicSearchList = musicService.getRelevantMusicSearchDTO(cleanQuery, limit);
		Map<String, Object> data = new HashMap<>();
		data.put("q", cleanQuery);
		data.put("results", musicSearchList);

		ResponseUtil.sendJson(response, data);

	}

	private void handleFetchMusic(String string, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
