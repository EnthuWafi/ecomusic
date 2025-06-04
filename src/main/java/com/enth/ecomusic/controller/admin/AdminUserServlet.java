package com.enth.ecomusic.controller.admin;

import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {
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
		// Show all users
		List<UserDTO> userList = userService.getAllUsers();
		request.setAttribute("userList", userList);
		request.setAttribute("pageTitle", "User List");
		request.setAttribute("contentPage", "/WEB-INF/views/admin/view-user-list.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
