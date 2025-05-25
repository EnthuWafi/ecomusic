package com.enth.ecomusic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class AdminGetUserServlet
 */
@WebServlet("/admin/user/*")
public class AdminGetUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		RoleCacheService roleCache = (RoleCacheService) getServletContext().getAttribute("roleCache");
		userService = new UserService(roleCache);
	}
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminGetUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Show a specific user
		String pathInfo = request.getPathInfo();
		int id = CommonUtil.extractIdFromPath(pathInfo);
		
		User user = userService.getUserById(id);

		request.setAttribute("pageTitle", "Show User");
		request.setAttribute("user", user);
		request.setAttribute("contentPage", "/WEB-INF/views/admin/view-user.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
