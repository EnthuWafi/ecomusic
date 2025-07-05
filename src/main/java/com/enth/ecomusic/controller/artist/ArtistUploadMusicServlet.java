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

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Genre;
import com.enth.ecomusic.model.entity.Mood;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.service.GenreCacheService;
import com.enth.ecomusic.service.MoodCacheService;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ArtistUploadMusicServlet
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB in memory
maxFileSize = 1024 * 1024 * 10, // 10MB per file
maxRequestSize = 1024 * 1024 * 50 // 50MB total per request
)
@WebServlet("/artist/music/upload")
public class ArtistUploadMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MusicService musicService;
	private GenreCacheService genreCacheService;
	private MoodCacheService moodCacheService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.genreCacheService = ctx.getGenreCacheService();
		this.moodCacheService = ctx.getMoodCacheService();
		this.musicService = ctx.getMusicService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ArtistUploadMusicServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Genre> genreList = genreCacheService.getAll();
		List<Mood> moodList = moodCacheService.getAll();

		request.setAttribute("pageTitle", "Upload Music");
		request.setAttribute("genreList", genreList);
		request.setAttribute("moodList", moodList);
		request.setAttribute("contentPage", "/WEB-INF/views/artist/upload-music.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Read text fields via MultipartUtils (no more custom partToString / parseInt)
		int genreId = MultipartUtil.getInt(request.getPart("genreId"), -1);
		int moodId = MultipartUtil.getInt(request.getPart("moodId"), -1);
		String title = MultipartUtil.getString(request.getPart("title"));
		String desc = MultipartUtil.getString(request.getPart("description"));
		VisibilityType visibility = VisibilityType.fromString(MultipartUtil.getString(request.getPart("visibility")).toLowerCase());
		boolean isPremium = MultipartUtil.getBoolean(request.getPart("premiumContent"));

		if (genreId == -1 || moodId == -1) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Genre and Mood are required!");
			response.sendRedirect(request.getContextPath() + "/artist/music/upload");
			return;
		}

		Part audioPart = request.getPart("audio");
		Part imagePart = request.getPart("image");

		UserDTO artist = (UserDTO) session.getAttribute("user");
		Music music = new Music(artist.getUserId(), title, genreId, moodId, desc, null, null, isPremium);
		music.setVisibility(visibility);

		boolean success = musicService.uploadMusic(music, audioPart, imagePart, artist);
		if (success) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Music successfully uploaded");
			response.sendRedirect(request.getContextPath() + "/artist/music");
		} else {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Music failed to upload!");
			response.sendRedirect(request.getContextPath() + "/artist/music/upload");
		}
	}

}
