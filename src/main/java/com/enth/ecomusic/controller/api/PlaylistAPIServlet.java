package com.enth.ecomusic.controller.api; 

import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.PlaylistMusic;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.ResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class PlaylistAPIServlet
 */
@WebServlet("/api/playlist/*")
public class PlaylistAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PlaylistService playlistService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.playlistService = ctx.getPlaylistService();
	}

	public PlaylistAPIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			handleFetchPlaylists(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				// /{id}
				handleFetchPlaylist(pathParts[0], request, response);

			} else if (pathParts.length == 3 && "music".equals(pathParts[1])) {
				// /{id}/music/{id}
				handleFetchMusic(pathParts[0], pathParts[2], request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}
	
	private void handleFetchPlaylists(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userIdStr = StringUtils.defaultString(request.getParameter("userId")).trim();
		String limitStr = StringUtils.defaultString(request.getParameter("limit")).trim();
		
		if (!StringUtils.isNumeric(userIdStr)) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "No user ID specified!");
			return;
		}
		
		//TODO: 
		int limit = 8;
		if (StringUtils.isNumeric(limitStr)) {
			limit = Integer.parseInt(limitStr);
			if (limit > 20) {
				limit = 20;
			}
		}
		
		int userId = Integer.parseInt(userIdStr);
		
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		List<PlaylistDTO> playlists = playlistService.getUserPlaylistByUserId(userId, currentUser);
	
		Map<String, Object> data = new HashMap<>();
		data.put("userId", userId);
		data.put("results", playlists);

		ResponseUtil.sendJson(response, data);
	}


	private void handleFetchMusic(String playlistIdStr, String musicIdStr, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
		int playlistId = Integer.parseInt(playlistIdStr);
		int musicId = Integer.parseInt(musicIdStr);
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		PlaylistDTO playlist = playlistService.getPlaylistByPlaylistId(playlistId, currentUser);
		
		if (playlist == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Access denied or does not exist");
			return;
		}
		Map<String, Object> data = new HashMap<>();
		data.put("playlistId", playlistId);
		data.put("musicId", musicId);
		data.put("data", playlistService.getPlaylistMusic(playlistId, musicId));

		ResponseUtil.sendJson(response, data);
	}

	private void handleFetchPlaylist(String playlistIdStr, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {
		int playlistId = Integer.parseInt(playlistIdStr);
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
	
		String fetchMusic = request.getParameter("music");
		
		PlaylistDTO playlist;
		if (StringUtils.isBlank(fetchMusic)) {
			playlist = playlistService.getPlaylistByPlaylistId(playlistId, currentUser);
		}
		else {
			playlist = playlistService.getPlaylistWithMusicByPlaylistId(playlistId, currentUser);
		}

		if (playlist == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Access denied or does not exist");
			return;
		}

		Map<String, Object> data = new HashMap<>();
		data.put("playlistId", playlistId);
		data.put("data", playlist);

		ResponseUtil.sendJson(response, data);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			// create playlist
			createPlaylistPost(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 2 && "music".equals(pathParts[1])) {
				// /{id}/music
				int playlistId = Integer.parseInt(pathParts[0]);
				addPlaylistMusic(playlistId, request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void createPlaylistPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	private void addPlaylistMusic(int playlistId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {
		
		String jsonBody;
        try {
            // Read the entire request body as a String using UTF-8 encoding
            jsonBody = IOUtils.toString(request.getReader()); 
        } catch (IOException e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Failed to read request body: " + e.getMessage());
            return;
        }

        Map<String, Object> requestBody;
        int musicId;
        try {
            requestBody = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {});
            
            musicId = Integer.parseInt(requestBody.get("musicId").toString());
            
            UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
            PlaylistMusic playlistMusic = new PlaylistMusic();
            playlistMusic.setPlaylistId(playlistId);
            playlistMusic.setMusicId(musicId);
            
    		if (playlistService.addSongToPlaylist(playlistMusic, currentUser)) {
    			ResponseUtil.sendJson(response, "Successfully added song to playlist " + playlistId);
    		} else {
    			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot add song to playlist music");
    		}
            

        } catch (Exception e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body or 'position' format: " + e.getMessage());
            return;
        }

	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Playlist ID is required");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				// {id}
				updatePlaylist(pathParts[0], request, response);
			} else if (pathParts.length == 3 && "music".equals(pathParts[1])) {
				// {id}/music/{id}
				updatePlaylistMusic(pathParts[0], pathParts[2], request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void updatePlaylistMusic(String playlistIdStr, String musicIdStr, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
		
		int playlistId = Integer.parseInt(playlistIdStr);
		int musicId = Integer.parseInt(musicIdStr);

		String jsonBody;
        try {
            // Read the entire request body as a String using UTF-8 encoding
            jsonBody = IOUtils.toString(request.getReader()); 
        } catch (IOException e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Failed to read request body: " + e.getMessage());
            return;
        }

        Map<String, Object> requestBody;
        int newPosition;
        try {
            requestBody = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {});
            Object posObj = requestBody.get("position");
            if (posObj == null) {
                ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing 'position' in request body.");
                return;
            }
            newPosition = (posObj instanceof Number) ? ((Number) posObj).intValue() : Integer.parseInt(posObj.toString());
            
            UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

    		if (playlistService.updatePlaylistSongPosition(playlistId, musicId, newPosition, currentUser)) {
    			ResponseUtil.sendJson(response, "Successfully updated playlist song position to " + newPosition);
    		} else {
    			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot update playlist music");
    		}

        } catch (Exception e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body or 'position' format: " + e.getMessage());
            return;
        }
        
        

	}

	private void updatePlaylist(String playlistIdStr, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {
		// TODO Auto-generated method stub
		int playlistId = Integer.parseInt(playlistIdStr);

	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Playlist ID is required");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				// {id}
				deletePlaylist(pathParts[0], request, response);
			} else if (pathParts.length == 3 && "music".equals(pathParts[1])) {
				// {id}/music/{id}
				deletePlaylistMusic(pathParts[0], pathParts[2], request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void deletePlaylistMusic(String playlistIdStr, String musicIdStr, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, NumberFormatException  {
		// TODO Auto-generated method stub
		int playlistId = Integer.parseInt(playlistIdStr);
		int musicId = Integer.parseInt(playlistIdStr);
		
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}
		
		if (playlistService.removeSongFromPlaylist(playlistId, musicId, currentUser)) {
			ResponseUtil.sendJson(response, "Successfully deleted music with ID " + musicId + " in playlist " + playlistId);
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot delete playlist music");
		}
		
		
	}

	private void deletePlaylist(String playlistIdStr, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, NumberFormatException {
		int playlistId = Integer.parseInt(playlistIdStr);	
		
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}
		
		if (playlistService.removePlaylist(playlistId, currentUser)) {
			ResponseUtil.sendJson(response, "Successfully deleted playlist with ID " + playlistId);
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot delete playlist");
		}
		
	}
}