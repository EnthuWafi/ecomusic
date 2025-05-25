package com.enth.ecomusic.controller.auth;

import java.io.IOException;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		
		RoleCacheService roleCache = (RoleCacheService) getServletContext().getAttribute("roleCache");
		userService = new UserService(roleCache);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "Login Page");
		request.setAttribute("contentPage", "/WEB-INF/views/auth/login.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Login post path
		String email = request.getParameter("email"); // username or email really
		String password = request.getParameter("password");

		User user = userService.getUserByUsernameOrEmail(email);

		HttpSession session = request.getSession();

		if (user != null && CommonUtil.checkPassword(password, user.getPassword())) {
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Successfully logged in!");
			session.setAttribute("user", user);
			
			// Handle redirection after login
			String redirectUri = (String) session.getAttribute("redirectAfterLogin");
			if (redirectUri != null) {
				session.removeAttribute("redirectAfterLogin");
				response.sendRedirect(
						request.getContextPath() + redirectUri.replaceFirst("^" + request.getContextPath(), ""));
			} else {
				response.sendRedirect(request.getContextPath() + "/home");
			}
			return;
		} else {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Invalid username or password!");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
	}
}
