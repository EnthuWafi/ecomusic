package com.enth.ecomusic.controller.admin;

import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.RoleType;
import com.enth.ecomusic.model.User;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

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
		userService = new UserService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Show all users
		List<User> userList = userService.getAllUsers();
		request.setAttribute("userList", userList);
		request.setAttribute("pageTitle", "User List");
		request.setAttribute("contentPage", "/WEB-INF/views/admin/view-user-list.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

}
