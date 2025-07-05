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
import com.enth.ecomusic.service.PlayHistoryService;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class ArtistDashboardServlet
 */
@WebServlet({"/artist/", "/artist/dashboard"})
public class ArtistDashboardServlet extends HttpServlet {
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
    public ArtistDashboardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		
		double multiplier = Double.parseDouble(AppConfig.get("revenuePerView"));
		
		int totalPlayCount = musicService.getTotalPlayCountByArtist(currentUser.getUserId());
		int musicCount = musicService.getMusicCountByArtist(currentUser.getUserId(), currentUser.getUserId());
		double totalRevenue = totalPlayCount * multiplier;
		
		List<MusicDTO> topMusics = musicService.getTopPlayedMusicDTOByUserId(currentUser.getUserId(), currentUser, 0, 5);
		List<MusicDTO> likedMusics = musicService.getTopLikedMusicDTOByUserId(currentUser.getUserId(), currentUser, 0, 5);
		
		request.setAttribute("topMusics", topMusics);
		request.setAttribute("likedMusics", likedMusics);
		request.setAttribute("totalPlayCount", totalPlayCount);
		request.setAttribute("musicCount", musicCount);
		request.setAttribute("totalRevenue", totalRevenue);
		request.setAttribute("pageTitle", "Artist Dashboard");
		request.setAttribute("contentPage", "/WEB-INF/views/artist/dashboard.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
