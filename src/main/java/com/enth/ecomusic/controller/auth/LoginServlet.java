package com.enth.ecomusic.controller.auth;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
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
		
		RoleCacheService roleCache = (RoleCacheService) getServletContext().getAttribute("roleCacheService");
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

		HttpSession session = request.getSession();

		UserDTO userDTO = userService.authenticateUser(email, password);
		if (userDTO != null) {	
			session.setAttribute("user", userDTO);
			
			CommonUtil.addMessage(session, ToastrType.SUCCESS, "Successfully logged in!");
			
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
