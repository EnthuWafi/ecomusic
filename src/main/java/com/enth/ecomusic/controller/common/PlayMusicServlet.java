package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class MusicServlet
 */
@WebServlet("/music/play/*")
public class PlayMusicServlet extends HttpServlet {
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
    public PlayMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();
		int id = CommonUtil.extractIdFromPath(pathInfo);
		
		if (pathInfo.matches("/\\d+")) {
			MusicDetailDTO music = musicService.getMusicDetailDTOById(id);	
			
			request.setAttribute("pageTitle", "Listen to Music");
			request.setAttribute("musicDTO", music);			
			//request.setAttribute("relatedTracks", recommendedList);
			request.setAttribute("contentPage", "/WEB-INF/views/common/play-music.jsp");
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
