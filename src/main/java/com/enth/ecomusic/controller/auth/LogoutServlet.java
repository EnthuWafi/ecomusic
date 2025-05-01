package com.enth.ecomusic.controller.auth;

import java.io.IOException;

import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    HttpSession session = request.getSession(false);

	    if (session != null && session.getAttribute("user") != null) {
	        session.removeAttribute("user");
	        CommonUtil.addMessage(session, ToastrType.SUCCESS, "You are now logged out!");
	    }

	    response.sendRedirect(request.getContextPath() + "/home");
	}

}
