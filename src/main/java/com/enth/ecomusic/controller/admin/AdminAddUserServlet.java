package com.enth.ecomusic.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.RoleType;
import com.enth.ecomusic.model.User;
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
		userService = new UserService();
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

		HttpSession session = request.getSession();
		// Check if user already exists
		User existing = userService.getUserByUsernameOrEmail(email);
		if (existing != null) {
			CommonUtil.addMessage(session, ToastrType.ERROR, "Email is already registered.");
			response.sendRedirect(request.getContextPath() + "/admin/user/add");
			return;
		}

		// Hash password
		String hashedPassword = CommonUtil.hashPassword(password);

		User user = new User(fname, lname, username, null, email, hashedPassword);
		
		RoleType role = RoleType.fromString(userType);
		
		boolean success = userService.registerUserWithRoleName(user, role);

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
