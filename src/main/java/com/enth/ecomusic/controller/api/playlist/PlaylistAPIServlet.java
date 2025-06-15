package com.enth.ecomusic.controller.api.playlist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.enth.ecomusic.model.dto.PlaylistDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class PlaylistAPIServlet
 */
@WebServlet("/api/playlist/*")
public class PlaylistAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private final PlaylistService playlistService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaylistAPIServlet() {
        super();
        AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
        this.playlistService = ctx.getPlaylistService();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Playlist ID is required");
            return;
        }

        String[] pathParts = pathInfo.split("/");

        try {
            if (pathParts.length == 2) {
                handleFetchPlaylist(pathParts[1], request, response);

            } else if (pathParts.length == 4 && "music".equals(pathParts[2])) {
                handleFetchMusic(pathParts[1], pathParts[3], request, response);

            } else {
            	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
            }
        } catch (NumberFormatException e) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
        }
    }
    

    private void handleFetchMusic(String playlistIdStr, String musicIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int playlistId = Integer.parseInt(playlistIdStr);
        int musicId = Integer.parseInt(musicIdStr);
        
        PlaylistDTO playlist = playlistService.getPlaylistByPlaylistId(playlistId);
        UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
        
        if (!playlistService.canAccessPublicPlaylist(playlist, currentUser)) {
            ResponseUtil.sendError(response, 403, "Access denied to this playlist");
            return;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("playlistId", playlistId);
        data.put("musicId", musicId);
        data.put("action", "fetch_music_in_playlist");
        data.put("data", playlistService.getPlaylistMusic(playlistId, musicId));

        response.getWriter().write(JsonUtil.toJson(data));
    }


	private void handleFetchPlaylist(String playlistIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int playlistId = Integer.parseInt(playlistIdStr);

	    PlaylistDTO playlist = playlistService.getPlaylistByPlaylistId(playlistId);
        UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
        
        if (!playlistService.canAccessPublicPlaylist(playlist, currentUser)) {
            ResponseUtil.sendError(response, 403, "Access denied to this playlist");
            return;
        }
        
	    Map<String, Object> data = new HashMap<>();
	    data.put("playlistId", playlistId);
	    data.put("action", "fetch_playlist");
	    data.put("data", playlist);

	    response.getWriter().write(JsonUtil.toJson(data));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            //create playlist
        	createPlaylistPost(request, response);
        }

        String[] pathParts = pathInfo.split("/");

        try {
            if (pathParts.length == 3 && "music".equals(pathParts[2])) {
            	
                addPlaylistMusic(pathParts[1], request, response);

            } else {
            	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
            }
        } catch (NumberFormatException e) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
        }
	}
	
	private void createPlaylistPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	private void addPlaylistMusic(String playlistIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int playlistId = Integer.parseInt(playlistIdStr);
		
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Playlist ID is required");
            return;
        }

        String[] pathParts = pathInfo.split("/");

        try {
            if (pathParts.length == 2) {
                updatePlaylist(pathParts[1], request, response);

            } else if (pathParts.length == 4 && "music".equals(pathParts[2])) {
            	
                updatePlaylistMusic(pathParts[1], pathParts[3], request, response);

            } else {
            	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
            }
        } catch (NumberFormatException e) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
        }
	}

	private void updatePlaylistMusic(String playlistIdStr, String musicIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int playlistId = Integer.parseInt(playlistIdStr);
        int musicId = Integer.parseInt(musicIdStr);
       
        int newPosition = Integer.parseInt(request.getParameter("position"));
        
        PlaylistDTO playlist = playlistService.getPlaylistByPlaylistId(playlistId);
        UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
        
        if (!playlistService.canModifyPlaylist(playlist, currentUser)) {
            ResponseUtil.sendError(response, 403, "Access denied to this playlist");
            return;
        }
        
        if (playlistService.updatePlaylistSongPosition(playlistId, musicId, newPosition)) {
        	ResponseUtil.sendJson(response, null);
        }
        else {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot update playlist music");
        }
		
	}

	private void updatePlaylist(String playlistIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int playlistId = Integer.parseInt(playlistIdStr);
		
		
	}


}
