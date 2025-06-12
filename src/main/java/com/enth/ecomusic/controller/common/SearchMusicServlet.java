package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class SearchMusicServlet
 */
@WebServlet("/music/search")
public class SearchMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MusicService musicService;
	private GenreCacheService genreCacheService;
	private MoodCacheService moodCacheService;

    @Override
    public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
		genreCacheService = ctx.getGenreCacheService();
		moodCacheService = ctx.getMoodCacheService();
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
		String[] genreIds = request.getParameterValues("genre");
		String[] moodIds = request.getParameterValues("mood");
		
		String cleanQuery = query != null ? query.trim().replaceAll("\\s+", " ").replaceAll("[&|*!~{}]", "") : null;
        if (StringUtils.isBlank(cleanQuery)) {
            cleanQuery = null;
        }
          
		List<Integer> genreIdList = new ArrayList<>();
		if (genreIds != null) {
		    for (String id : genreIds) {
		        if (StringUtils.isNumeric(id)) {
		        	int parsedId = Integer.parseInt(id);
		        	if (genreCacheService.getById(parsedId) != null) {
		                genreIdList.add(parsedId);
		            }
		        }
		    }
		}

		List<Integer> moodIdList = new ArrayList<>();
		if (moodIds != null) {
		    for (String id : moodIds) {
		        if (StringUtils.isNumeric(id)) {
		        	int parsedId = Integer.parseInt(id);
		            if (moodCacheService.getById(parsedId) != null) {
		                moodIdList.add(parsedId);
		            }
		        }
		    }
		}
        List<MusicDetailDTO> musicList;
        int totalRecords = 0;
        int page = 1;
        int pageSize = 10;
        
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
         
        if (cleanQuery != null) {
            musicList = musicService.getPaginatedMusicDetailDTOLikeKeyword(cleanQuery, genreIdList, moodIdList, page, pageSize);
            totalRecords = musicService.getMusicCountLikeKeyword(cleanQuery, genreIdList, moodIdList); 
        } else {
            musicList = musicService.getPaginatedMusicDetailDTO(page, pageSize); 
            totalRecords = musicService.getMusicCount(); 
        }
        
        int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        
        request.setAttribute("musicList", musicList);
        request.setAttribute("searchQuery", query);
        request.setAttribute("pageTitle", "Search Results");
        request.setAttribute("contentPage", "/WEB-INF/views/common/search-music.jsp");

        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
