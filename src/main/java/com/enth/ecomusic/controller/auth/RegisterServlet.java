package com.enth.ecomusic.controller.auth;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
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
	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		userService = ctx.getUserService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "Registration Page");
		request.setAttribute("contentPage", "/WEB-INF/views/auth/register.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// registration process
		String fname = request.getParameter("first_name");
		String lname = request.getParameter("last_name");
		String username = request.getParameter("username");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    HttpSession session = request.getSession();

	    // Check if user already exists
	    UserDTO existing = userService.getUserDTOByUsernameOrEmail(email);
	    if (existing != null) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Email is already registered.");
	        response.sendRedirect(request.getContextPath() + "/register");
	        return;
	    }

	    User user = new User(fname, lname, username, null, email, password);
	    boolean success = userService.registerUserAccount(user, null, RoleType.USER, null);

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
