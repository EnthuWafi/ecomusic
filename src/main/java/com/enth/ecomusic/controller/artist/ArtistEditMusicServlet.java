package com.enth.ecomusic.controller.artist;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Genre;
import com.enth.ecomusic.model.entity.Mood;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.model.mapper.MusicMapper;
import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ArtistEditMusicServlet
 */
@MultipartConfig
@WebServlet("/artist/music/edit/*")
public class ArtistEditMusicServlet extends HttpServlet {
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
		this.genreCacheService = ctx.getGenreCacheService();
		this.moodCacheService = ctx.getMoodCacheService();
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistEditMusicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String pathInfo = request.getPathInfo(); 
		int musicId = CommonUtil.extractIdFromPath(pathInfo);
		
		List<Genre> genreList = genreCacheService.getAll();
		List<Mood> moodList = moodCacheService.getAll();
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		
		MusicDTO music = musicService.getMusicDTOById(musicId, currentUser);

		request.setAttribute("pageTitle", "Edit Music");
		request.setAttribute("genreList", genreList);
		request.setAttribute("moodList", moodList);
		request.setAttribute("musicId", musicId);
		request.setAttribute("musicDTOJson", JsonUtil.toJson(music));
		request.setAttribute("contentPage", "/WEB-INF/views/artist/edit-music.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");
		
		String pathInfo = request.getPathInfo(); 
		Integer musicId = CommonUtil.extractIdFromPath(pathInfo);

		int genreId = MultipartUtil.getInt(request.getPart("genreId"), -1);
		int moodId = MultipartUtil.getInt(request.getPart("moodId"), -1);
		String title = MultipartUtil.getString(request.getPart("title"));
		String desc = MultipartUtil.getString(request.getPart("description"));
		VisibilityType visibility = VisibilityType.fromString(MultipartUtil.getString(request.getPart("visibility")).toLowerCase());
		boolean isPremium = MultipartUtil.getBoolean(request.getPart("premiumContent"));

		MusicDTO musicDTO = musicService.getMusicDTOById(musicId, currentUser);
		
		if (musicDTO == null) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Invalid music!");
			response.sendRedirect(request.getContextPath() + "/artist/music");
			return;
		}
		
		if (genreId == -1 || moodId == -1) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Genre and Mood are required!");
			response.sendRedirect(request.getContextPath() + "/artist/music/edit/" + musicId);
			return;
		}

		Part audioPart = request.getPart("audio");
		Part imagePart = request.getPart("image");

		Music music = MusicMapper.INSTANCE.toMusic(musicDTO);
		music.setTitle(title);
		music.setDescription(desc);
		music.setPremiumContent(isPremium);
		music.setGenreId(genreId);
		music.setMoodId(moodId);
		music.setVisibility(visibility);

		boolean success = musicService.updateMusic(music, audioPart, imagePart, currentUser);
		if (success) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Music successfully updated");
			response.sendRedirect(request.getContextPath() + "/artist/music");
		} else {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Music failed to update!");
			response.sendRedirect(request.getContextPath() + "/artist/music/edit/" + musicId);
		}
	}

}
