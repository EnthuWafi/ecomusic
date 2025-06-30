package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.enums.VisibilityType;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class UserAPIServlet
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB in memory
		maxFileSize = 1024 * 1024 * 10, // 10MB per file
		maxRequestSize = 1024 * 1024 * 50 // 50MB total per request
)
@WebServlet("/api/user/*")
public class UserAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.userService = ctx.getUserService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserAPIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			handleFetchUsers(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				handleFetchUser(userId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	private void handleFetchUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String limitStr = StringUtils.defaultString(request.getParameter("limit"));
		String offsetStr = StringUtils.defaultString(request.getParameter("offset"));

		int limit = StringUtils.isNumeric(limitStr) ? Integer.parseInt(limitStr) : 5;
		int offset = StringUtils.isNumeric(offsetStr) ? Integer.parseInt(offsetStr) : 0;

		List<UserDTO> userList = userService.getAllUserDTO(offset, limit);

		Map<String, Object> data = new HashMap<>();
		data.put("limit", limit);
		data.put("offset", offset);
		data.put("results", userList);

		ResponseUtil.sendJson(response, data);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			createUserPost(request, response);
			return;
		}

		ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
	}

	private void createUserPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		try {
			// === Parse Multipart Fields ===
			String firstName = MultipartUtil.getString(request.getPart("firstName"));
			String lastName = MultipartUtil.getString(request.getPart("lastName"));
			String username = MultipartUtil.getString(request.getPart("username"));
			String email = MultipartUtil.getString(request.getPart("email"));
			String bio = MultipartUtil.getString(request.getPart("bio"));
			String password = MultipartUtil.getString(request.getPart("password"));
			String roleTypeStr = MultipartUtil.getString(request.getPart("roleType"));

			Part imagePart = request.getPart("image");

			// === Convert Role ===
			RoleType roleType = RoleType.fromString(roleTypeStr);

			// === Build User Entity ===
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(username);
			user.setEmail(email);
			user.setBio(bio);
			user.setPassword(password); // will be hashed inside service

			// === Call Service ===
			boolean success = userService.registerUserAccount(user, imagePart, roleType, currentUser);

			if (success) {
				ResponseUtil.sendJson(response, "User registered successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN,
						"Registration failed (unauthorized or validation failed)");
			}

		} catch (IllegalStateException e) {
			// Happens when file size exceeds @MultipartConfig limits
			ResponseUtil.sendError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
					"Upload too large: " + e.getMessage());
		} catch (ServletException e) {
			// Catch multipart parsing issues (e.g. bad encoding, invalid parts)
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Malformed multipart request: " + e.getMessage());
		} catch (Exception e) {
			// Generic fallback
			e.printStackTrace();
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Server error: " + e.getMessage());
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				editUser(userId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void editUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		try {
			// === Parse Multipart Fields ===
			String firstName = MultipartUtil.getString(request.getPart("firstName"));
			String lastName = MultipartUtil.getString(request.getPart("lastName"));
			String username = MultipartUtil.getString(request.getPart("username"));
			String email = MultipartUtil.getString(request.getPart("email"));
			String bio = MultipartUtil.getString(request.getPart("bio"));
			String password = MultipartUtil.getString(request.getPart("password"));
			String roleTypeStr = MultipartUtil.getString(request.getPart("roleType"));
			Part imagePart = request.getPart("image");

			// === Convert Role ===
			RoleType roleType = RoleType.fromString(roleTypeStr);

			// === Build User Entity ===
			User user = new User();
			user.setUserId(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(username);
			user.setEmail(email);
			user.setBio(bio);
			user.setPassword(password); // hashed inside service

			// === Call Service ===
			boolean success = userService.updateUser(user, imagePart, roleType, currentUser);

			if (success) {
				ResponseUtil.sendJson(response, "User updated successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN,
						"Update failed (unauthorized or validation failed)");
			}

		} catch (IllegalStateException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
					"Upload too large: " + e.getMessage());
		} catch (ServletException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Malformed multipart request: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Server error: " + e.getMessage());
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				deleteUser(userId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void deleteUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		// Fetch music by ID from DB
		User user = userService.getUserById(userId);

		if (user == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "User not found");
			return;
		}

		// Attempt to delete
		boolean success = userService.deleteUser(user, currentUser);

		if (success) {
			ResponseUtil.sendJson(response, "User deleted successfully");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN,
					"You are not allowed to delete this user");
		}
	}

}
