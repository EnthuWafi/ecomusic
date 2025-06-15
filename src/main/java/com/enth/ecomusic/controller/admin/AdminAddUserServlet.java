package com.enth.ecomusic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class AdminAddUserServlet
 */
@WebServlet("/admin/user/add")
public class AdminAddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		RoleCacheService roleCache = (RoleCacheService) getServletContext().getAttribute("roleCacheService");
		userService = new UserService(roleCache);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAddUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setAttribute("pageTitle", "Add User");
		request.setAttribute("contentPage", "/WEB-INF/views/admin/create-user.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fname = request.getParameter("first_name");
		String lname = request.getParameter("last_name");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userType = request.getParameter("user_type");
		
		Part imagePart = request.getPart("image");

		HttpSession session = request.getSession();
		UserDTO existing = userService.getUserDTOByUsernameOrEmail(email);
		
		if (existing != null) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Email is already registered.");
			response.sendRedirect(request.getContextPath() + "/admin/user/add");
			return;
		}
	
		RoleType role = RoleType.fromString(userType);
		
		User user = new User(fname, lname, username, null, email, password);
		boolean success = userService.registerUserAccount(user, imagePart, role);

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
