package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.dto.MusicSearchDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.model.mapper.MusicMapper;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class MusicAPIServlet
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB in memory
		maxFileSize = 1024 * 1024 * 10, // 10MB per file
		maxRequestSize = 1024 * 1024 * 50 // 50MB total per request
)
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
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			handleFetchListMusic(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1 && "search".equals(pathParts[0])) {
				handleFetchSearch(request, response);
			} else if (pathParts.length == 1) {
				int musicId = Integer.parseInt(pathParts[0]);
				handleFetchMusic(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchListMusic(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sortStr = StringUtils.defaultString(request.getParameter("sort"));
		int limit = CommonUtil.parseIntLimitParam(request.getParameter("limit"), 5, 50);
		int offset = CommonUtil.parseIntLimitParam(request.getParameter("offset"), 0, Integer.MAX_VALUE);

		List<MusicDTO> musicList = "top".equalsIgnoreCase(sortStr) ? musicService.getTopPlayedMusicDTO(offset, limit)
				: musicService.getAllMusicDTO(offset, limit);

		Map<String, Object> data = new HashMap<>();
		data.put("limit", limit);
		data.put("offset", offset);
		data.put("results", musicList);

		ResponseUtil.sendJson(response, data);
	}

	private void handleFetchSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String query = StringUtils.trimToEmpty(request.getParameter("q")).replaceAll("\\s+", " ")
				.replaceAll("[&|*!~{}]", "");

		if (StringUtils.isBlank(query)) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "q cannot be empty");
			return;
		}

		int limit = CommonUtil.parseIntLimitParam(request.getParameter("limit"), 8, 20);
		List<MusicSearchDTO> results = musicService.getRelevantMusicSearchDTO(query, limit);

		Map<String, Object> data = new HashMap<>();
		data.put("q", query);
		data.put("results", results);

		ResponseUtil.sendJson(response, data);
	}

	private void handleFetchMusic(int musicId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		MusicDetailDTO music = musicService.getMusicDetailDTOById(musicId, currentUser);

		Map<String, Object> data = new HashMap<>();
		data.put("results", music);
		ResponseUtil.sendJson(response, data);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			createMusicPost(request, response);
			return;
		}

		ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
	}

	private void createMusicPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		try {
			// Multipart request parsing
			int genreId = MultipartUtil.getInt(request.getPart("genreId"), 0);
			int moodId = MultipartUtil.getInt(request.getPart("moodId"), 0);
			String title = MultipartUtil.getString(request.getPart("title"));
			String desc = MultipartUtil.getString(request.getPart("description"));
			String visibilityStr = MultipartUtil.getString(request.getPart("visibility"));
			boolean isPremium = MultipartUtil.getBoolean(request.getPart("premiumContent"));

			if (genreId == 0 || moodId == 0 || title == null || visibilityStr == null) {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
				return;
			}

			VisibilityType visibility = VisibilityType.fromString(visibilityStr.toLowerCase());

			Part audioPart = request.getPart("audio");
			Part imagePart = request.getPart("image");

			if (audioPart == null || audioPart.getSize() == 0) {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Audio file is required");
				return;
			}

			Music music = new Music(currentUser.getUserId(), title, genreId, moodId, desc, null, null, isPremium, visibility);

			boolean success = musicService.uploadMusic(music, audioPart, imagePart, currentUser);

			if (success) {
				ResponseUtil.sendJson(response, "Music uploaded successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Failed to upload music");
			}

		} catch (IllegalStateException e) {
			// Happens when file size exceeds @MultipartConfig limits
			ResponseUtil.sendError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
					"Upload too large: " + e.getMessage());
		} catch (ServletException e) {
			// Catch multipart parsing issues (e.g. bad encoding, invalid parts)
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Malformed multipart request: " + e.getMessage());
		} catch (Exception e) {
			// Generic fallback
			e.printStackTrace();
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Server error: " + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
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
				editMusicPut(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void editMusicPut(int musicId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {

		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		try {
			// Multipart request parsing
			int genreId = MultipartUtil.getInt(request.getPart("genreId"), 0);
			int moodId = MultipartUtil.getInt(request.getPart("moodId"), 0);
			String title = MultipartUtil.getString(request.getPart("title"));
			String desc = MultipartUtil.getString(request.getPart("description"));
			String visibilityStr = MultipartUtil.getString(request.getPart("visibility"));
			boolean isPremium = MultipartUtil.getBoolean(request.getPart("premiumContent"));

			if (genreId == 0 || moodId == 0 || title == null || visibilityStr == null) {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
				return;
			}

			VisibilityType visibility = VisibilityType.fromString(visibilityStr.toLowerCase());

			Part audioPart = request.getPart("audio");
			Part imagePart = request.getPart("image");

			if (audioPart == null || audioPart.getSize() == 0) {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Audio file is required");
				return;
			}

			MusicDTO musicDTO = musicService.getMusicDTOById(musicId, currentUser);

			Music music = MusicMapper.INSTANCE.toMusic(musicDTO);
			music.setTitle(title);
			music.setDescription(desc);
			music.setPremiumContent(isPremium);
			music.setGenreId(genreId);
			music.setMoodId(moodId);
			music.setVisibility(visibility);

			boolean success = musicService.updateMusic(music, audioPart, imagePart, currentUser);
			;

			if (success) {
				ResponseUtil.sendJson(response, "Music updated successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Failed to update music");
			}

		} catch (IllegalStateException e) {
			// Happens when file size exceeds @MultipartConfig limits
			ResponseUtil.sendError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
					"Upload too large: " + e.getMessage());
		} catch (ServletException e) {
			// Catch multipart parsing issues (e.g. bad encoding, invalid parts)
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Malformed multipart request: " + e.getMessage());
		} catch (Exception e) {
			// Generic fallback
			e.printStackTrace();
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Server error: " + e.getMessage());
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
				deleteMusic(musicId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void deleteMusic(int musicId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {

		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		// Fetch music by ID from DB
		Music music = musicService.getMusicById(musicId);

		if (music == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Music not found");
			return;
		}

		// Attempt to delete
		boolean success = musicService.deleteMusic(music, currentUser);

		if (success) {
			ResponseUtil.sendJson(response, "Music deleted successfully");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN,
					"You are not allowed to delete this music");
		}

	}
	
}
