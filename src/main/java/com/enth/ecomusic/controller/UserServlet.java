package com.enth.ecomusic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.util.Helper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
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
		int id = Helper.extractIdFromPath(pathInfo);
		
		if (pathInfo == null || pathInfo.equals("/")) {
			// Show all users
			List<User> userList = userDAO.getAllUsers();
			request.setAttribute("userList", userList);
			request.setAttribute("pageTitle", "User List");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/userList.jsp");
		}else if (pathInfo.equals("/add")) {
	        // Add user Form
			request.setAttribute("pageTitle", "Add User");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/addUser.jsp");
	    } 
		else if (pathInfo.matches("/\\d+")) {
			// Show a specific user
			User user = userDAO.getUserById(id);
			
			request.setAttribute("pageTitle", "Show User");
			request.setAttribute("user", user);
			request.setAttribute("contentPage", "/WEB-INF/views/admin/showUser.jsp");
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
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userType = request.getParameter("user_type");

		User user = new User(name, email, password, userType);
		boolean success = userDAO.insertUser(user);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		if (success) {
			out.println("User added successfully!");
		} else {
			out.println("Failed to add user.");
		}
	}
}
