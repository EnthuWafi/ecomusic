package com.enth.ecomusic.controller.admin;

import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

@WebServlet("/admin/user/*")
public class AdminUserServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	// GET: fetch list or user by id
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo(); // e.g., "/5" or "/edit/5" or null
		int id = CommonUtil.extractIdFromPath(pathInfo);

		if (pathInfo == null || pathInfo.equals("/")) {
			// Show all users
			List<User> userList = userDAO.getAllUsers();
			request.setAttribute("userList", userList);
			request.setAttribute("pageTitle", "User List");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/view-user-list.jsp");
		} else if (pathInfo.equals("/add")) {
			// Add user Form
			request.setAttribute("pageTitle", "Add User");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/create-user.jsp");
		} else if (pathInfo.matches("/\\d+")) {
			// Show a specific user
			User user = userDAO.getUserById(id);

			request.setAttribute("pageTitle", "Show User");
			request.setAttribute("user", user);
			request.setAttribute("contentPage", "/WEB-INF/views/admin/view-user.jsp");
		} else if (pathInfo.startsWith("/edit/\\d+")) {
			// Show edit page TODO
		} else {
			// 404 Not Found
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	// POST: create new user
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fname = request.getParameter("first_name");
		String lname = request.getParameter("last_name");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userType = request.getParameter("user_type");

		HttpSession session = request.getSession();
		// Check if user already exists
		User existing = userDAO.getUserByUsernameOrEmail(email);
		if (existing != null) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Email is already registered.");
			response.sendRedirect(request.getContextPath() + "/admin/user/add");
			return;
		}

		// Hash password
		String hashedPassword = CommonUtil.hashPassword(password);

		User user = new User(fname, lname, null, username, email, hashedPassword, userType, userType);
		boolean success = userDAO.insertUser(user);

		if (success) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Registration successful. Please log in.");
			response.sendRedirect(request.getContextPath() + "/admin/user/");
			return;
		} else {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Something went wrong. Try again.");
			response.sendRedirect(request.getContextPath() + "/admin/user/add");
			return;
		}
	}

}
