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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.EmailValidator;

import com.enth.ecomusic.model.dto.ChartDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Role;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.service.ReportService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.MultipartUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.enth.ecomusic.util.ToastrType;
import com.google.gson.reflect.TypeToken;

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
	private ReportService reportService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.userService = ctx.getUserService();
		this.reportService = ctx.getReportService();
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
			if (pathParts.length == 1 && "role".equals(pathParts[0])) {
				handleFetchRole(request, response);
			} 
			else if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				handleFetchUser(userId, request, response);
			} else if (pathParts.length == 2 && "chart".equals(pathParts[1])) {
				int userId = Integer.parseInt(pathParts[0]);
				handleFetchUserChart(userId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchUserChart(int userId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		String typeStr = StringUtils.defaultIfBlank(request.getParameter("type"), "plays");
		String dateTypeStr = StringUtils.defaultIfBlank(request.getParameter("dateType"), "daily");
		String startStr = request.getParameter("start");
		String endStr = request.getParameter("end");

		LocalDate start = null;
		LocalDate end = null;

		try {
			if (StringUtils.isNotBlank(startStr)) {
				start = LocalDate.parse(startStr);
			}
			if (StringUtils.isNotBlank(endStr)) {
				end = LocalDate.parse(endStr);
			}
		} catch (DateTimeParseException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Invalid date format. Use yyyy-MM-dd.");
			return;
		}

		ChartDTO chartDTO;

		switch (typeStr.toLowerCase()) {
		case "plays":
			chartDTO = reportService.getArtistUserPlayChartDTO(start, end, dateTypeStr, userId);
			break;
		default:
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Unsupported chart type: " + typeStr);
			return;
		}

		Map<String, Object> data = new HashMap<>();
		data.put("type", typeStr);
		data.put("dateType", dateTypeStr);
		data.put("start", startStr);
		data.put("end", endStr);
		data.put("results", chartDTO);

		ResponseUtil.sendJson(response, data);
		
	}

	private void handleFetchRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

		List<Role> roles = userService.getRoles();
		Map<String, Object> data = new HashMap<>();
		data.put("results", roles);

		ResponseUtil.sendJson(response, data);
	}

	private void handleFetchUser(int userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	private void handleFetchUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int limit = CommonUtil.parseIntLimitParam(request.getParameter("limit"), 5, 50);
		int offset = CommonUtil.parseIntLimitParam(request.getParameter("offset"), 0, Integer.MAX_VALUE);

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

			RoleType roleType = RoleType.fromString(roleTypeStr);
			
			// Empty field check
		    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) || StringUtils.isBlank(username)
		            || StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
		        return;
		    }

		    // Validate email format
		    if (!EmailValidator.getInstance().isValid(email)) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid email format.");
		        return;
		    }

		    // Validate name and username (simple alphanumeric + length check)
		    if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Username must be 4-20 characters, alphanumeric or underscores only.");
		        return;
		    }

		    if (!firstName.matches("^[a-zA-Z\\s]{1,50}$") || !lastName.matches("^[a-zA-Z\\s]{1,50}$")) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "First and Last name must contain only letters.");
		        return;
		    }

		    if (password.length() < 6) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Password must be at least 6 characters long.");
		        return;
		    }
		    if (roleType == null) {
		    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Role type cannot be null.");
		        return;
		    }

			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(username);
			user.setEmail(email);
			user.setBio(bio);
			user.setPassword(password);

			
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
			} else if (pathParts.length == 2 && "role".equals(pathParts[1])) {
				int userId = Integer.parseInt(pathParts[0]);
				editUserRole(userId, request, response);
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void editUserRole(int userId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}

		String jsonBody;
		try {
			// Read JSON body
			jsonBody = IOUtils.toString(request.getReader());
		} catch (IOException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Failed to read request body: " + e.getMessage());
			return;
		}

		try {
			// Parse JSON into a map
			Map<String, Object> body = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {});

			String roleTypeStr = (String) body.get("roleType");
			if (roleTypeStr == null || roleTypeStr.isBlank()) {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing or empty 'roleType'");
				return;
			}

			RoleType roleType = RoleType.fromString(roleTypeStr);

			boolean success = userService.updateUserSetRole(userId, roleType, currentUser);

			if (success) {
				ResponseUtil.sendJson(response, "User role updated successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN, "You are not allowed to update this role");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON or server error: " + e.getMessage());
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

			RoleType roleType = null;
			if (!(roleTypeStr == null)) {
				roleType = RoleType.fromString(roleTypeStr);
			}

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
