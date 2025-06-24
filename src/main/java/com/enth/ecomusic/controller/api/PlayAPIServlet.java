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
import com.enth.ecomusic.model.entity.PlayHistory;
import com.enth.ecomusic.service.PlayHistoryService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class PlayAPIServlet
 */
@WebServlet("/api/play/*")
public class PlayAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private PlayHistoryService playHistoryService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlayAPIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.playHistoryService = ctx.getPlayHistoryService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1 && "recent".equals(pathParts[0])) {
				handleRecentPlayHistory(request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleRecentPlayHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ResponseUtil.sendJson(response, "Something here");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			createPlayHistoryRecord(request, response);
			return;
		}

		ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
	}

	private void createPlayHistoryRecord(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}
		
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
        long listenDuration;
        boolean wasSkipped;
        try {
            requestBody = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {});
            Object musicObj = requestBody.get("musicId");
            Object listenObj = requestBody.get("listenDuration");
            Object skipObj = requestBody.get("wasSkipped");
            
            musicId = (musicObj instanceof Number) ? ((Number) musicObj).intValue() : Integer.parseInt(musicObj.toString());
            listenDuration = (listenObj instanceof Number) ? ((Number) listenObj).longValue() : Long.parseLong(listenObj.toString());
            wasSkipped = (skipObj instanceof Boolean) ? ((Boolean) skipObj).booleanValue() : Boolean.parseBoolean(skipObj.toString());
        } catch (Exception e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body or format: " + e.getMessage());
            return;
        }
        
        PlayHistory ph = new PlayHistory();
        ph.setUserId(currentUser.getUserId());
        ph.setMusicId(musicId);
        ph.setListenDuration(listenDuration);
        ph.setWasSkipped(wasSkipped);
         
        boolean success = playHistoryService.recordPlay(ph, currentUser);
        
        if (success) {
        	ResponseUtil.sendJson(response, "Successfully created a play history record");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot create a play history music record");
		}
        
        
	}

}
