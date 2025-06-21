package com.enth.ecomusic.controller.artist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class ArtistMusicServlet
 */
@WebServlet("/artist/music")
public class ArtistMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MusicService musicService;

	@Override
	public void init() throws ServletException {
		super.init();
		
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method
		
		int page = 1;
        int pageSize = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
		
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		List<MusicDTO> musicList = musicService.getPaginatedMusicDTOByArtistId(user.getUserId(), page, pageSize);
		int totalRecords = musicService.getMusicCountByArtist(user.getUserId()); 
        int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);
		
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
		request.setAttribute("musicList", musicList);
		request.setAttribute("pageTitle", "Music Page");
		request.setAttribute("contentPage", "/WEB-INF/views/artist/view-music-list.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
