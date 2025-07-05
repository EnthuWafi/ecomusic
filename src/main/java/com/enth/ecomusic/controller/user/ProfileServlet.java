package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.EmailValidator;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.mapper.UserMapper;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.service.PlayHistoryService;
import com.enth.ecomusic.service.PlaylistService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.NumberFormatUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ProfileServlet
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB in memory
		maxFileSize = 1024 * 1024 * 10, // 10MB per file
		maxRequestSize = 1024 * 1024 * 50 // 50MB total per request
)
@WebServlet("/user/profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;
	private PlayHistoryService playService;
	private MusicService musicService;
	private PlaylistService playlistService;

	@Override
	public void init() throws ServletException {
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.userService = ctx.getUserService();
		this.playService = ctx.getPlayHistoryService();
		this.musicService = ctx.getMusicService();
		this.playlistService = ctx.getPlaylistService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProfileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		String listeningTime = NumberFormatUtil.formatDuration(playService.sumListenDurationByUserId(user.getUserId()));
		int musicCount = musicService.getMusicCountByArtist(user.getUserId(), user.getUserId());
		int playlistCount = playlistService.getPlaylistCountByArtist(user.getUserId(), user.getUserId());

		request.setAttribute("musicCount", musicCount);
		request.setAttribute("playlistCount", playlistCount);
		request.setAttribute("listeningTime", listeningTime);
		request.setAttribute("user", user);
		request.setAttribute("pageTitle", "User Profile");
		request.setAttribute("contentPage", "/WEB-INF/views/user/profile.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");

		try {
			String firstName = MultipartUtil.getString(request.getPart("firstName"));
			String lastName = MultipartUtil.getString(request.getPart("lastName"));
			String username = MultipartUtil.getString(request.getPart("username"));
			String bio = MultipartUtil.getString(request.getPart("bio"));
			String email = MultipartUtil.getString(request.getPart("email"));
			Part imagePart = request.getPart("image"); // can be null or empty

			// Empty field check
			if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) || StringUtils.isBlank(username)
					|| StringUtils.isBlank(email)) {
				CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "All fields are required.");
				response.sendRedirect(request.getContextPath() + "/user/profile");
				return;
			}

			// Validate email format
			if (!EmailValidator.getInstance().isValid(email)) {
				CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Invalid email format.");
				response.sendRedirect(request.getContextPath() + "/user/profile");
				return;
			}

			// Validate name and username (simple alphanumeric + length check)
			if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
				CommonUtil.addMessage(request.getSession(), ToastrType.ERROR,
						"Username must be 4-20 characters, alphanumeric or underscores only.");
				response.sendRedirect(request.getContextPath() + "/user/profile");
				return;
			}

			if (!firstName.matches("^[a-zA-Z\\s]{1,50}$") || !lastName.matches("^[a-zA-Z\\s]{1,50}$")) {
				CommonUtil.addMessage(request.getSession(), ToastrType.ERROR,
						"First and Last name must contain only letters.");
				response.sendRedirect(request.getContextPath() + "/user/profile");
				return;
			}

			User updatedUser = new User();
			updatedUser.setUserId(currentUser.getUserId());
			updatedUser.setFirstName(firstName);
			updatedUser.setLastName(lastName);
			updatedUser.setUsername(username);
			updatedUser.setBio(bio);
			updatedUser.setEmail(email);
			updatedUser.setRoleId(currentUser.getRoleId());
			updatedUser.setImageUrl(currentUser.getImageUrl());

			if (userService.updateUser(updatedUser, imagePart, RoleType.USER, currentUser)) {

				request.getSession().setAttribute("user", UserMapper.INSTANCE.toDTO(updatedUser));
				CommonUtil.addMessage(request.getSession(), ToastrType.SUCCESS, "Successfully updated user");
			} else {
				CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Something went wrong!");
			}
			response.sendRedirect(request.getContextPath() + "/user/profile");

		} catch (IllegalStateException e) {
			// Happens when file size exceeds @MultipartConfig limits
			response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Upload too large: " + e.getMessage());
		} catch (ServletException e) {
			// Catch multipart parsing issues (e.g. bad encoding, invalid parts)
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed multipart request: " + e.getMessage());
		} catch (Exception e) {
			// Generic fallback
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
		}

	}

}
