package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.service.MusicService;

/**
 * Servlet implementation class SearchMusicServlet
 */
@WebServlet("/music/search")
public class SearchMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MusicService musicService;

    @Override
    public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		GenreCacheService genreCacheService = (GenreCacheService) this.getServletContext().getAttribute("genreCacheService");
		MoodCacheService moodCacheService = (MoodCacheService) this.getServletContext().getAttribute("moodCacheService");
		this.musicService = new MusicService(genreCacheService, moodCacheService);
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("q"); 
        List<MusicDetailDTO> musicList;

        int page = 1;
        int pageSize = 10;
        
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        
        if (query != null && !query.trim().isEmpty()) {
            musicList = musicService.getPaginatedMusicDetailDTOLikeKeyword(query.trim(), page, pageSize);
        } else {
            musicList = musicService.getPaginatedMusicDetailDTO(page, pageSize); 
        }
        
        int totalRecords = musicService.getMusicCount(); 
        int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        
        request.setAttribute("musicList", musicList);
        request.setAttribute("searchQuery", query);
        request.setAttribute("pageTitle", "Search Results");
        request.setAttribute("contentPage", "/WEB-INF/views/common/browse-music.jsp");

        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
