package com.enth.ecomusic.controller.auth;

import java.io.IOException;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		super.init();
		userDAO = new UserDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "Registration Page");
		request.setAttribute("contentPage", "/WEB-INF/views/auth/register.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// registration process
		String name = request.getParameter("name");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    HttpSession session = request.getSession();

	    // Basic validation
	    if (name == null || name.isBlank() ||
	        email == null || email.isBlank() ||
	        password == null || password.isBlank()) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "All fields are required.");
	        response.sendRedirect(request.getContextPath() + "/register");
	        return;
	    }

	    // Check if user already exists
	    User existing = userDAO.getUserByUsernameOrEmail(email);
	    if (existing != null) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Email is already registered.");
	        response.sendRedirect(request.getContextPath() + "/register");
	        return;
	    }

	    // Hash password
	    String hashedPassword = CommonUtil.hashPassword(password);

	    // Create user object
	    User newUser = new User(name, email, hashedPassword, "listener");

	    boolean success = userDAO.insertUser(newUser);

	    if (success) {
	        CommonUtil.addMessage(session, ToastrType.SUCCESS, "Registration successful. Please log in.");
	        response.sendRedirect(request.getContextPath() + "/login");
	        return;
	    } else {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Something went wrong. Try again.");
	        response.sendRedirect(request.getContextPath() + "/register");
	        return;
	    }
	}
}
