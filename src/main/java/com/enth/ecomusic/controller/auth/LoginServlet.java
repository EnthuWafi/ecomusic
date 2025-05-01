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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("pageTitle", "Login Page");
		request.setAttribute("contentPage", "/WEB-INF/views/auth/login.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Login post path
        String username = request.getParameter("username"); //username or email really
        String password = request.getParameter("password");
				
		User user = userDAO.getUserByUsernameOrEmail(username);
		
		if (user != null && CommonUtil.checkPassword(password, user.getPassword())) {
			CommonUtil.addMessage(request.getSession(), ToastrType.SUCCESS, "Successfully logged in!");
	        request.getSession().setAttribute("user", user);
	        response.sendRedirect(request.getContextPath() + "/home");
	        return;
	    }
		else {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Invalid username or password!");
			response.sendRedirect(request.getContextPath() + "/login");
	        return;
		}
	}
}
