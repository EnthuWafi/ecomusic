package com.enth.ecomusic.controller.common;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/home", "/index", ""})
public class HomeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("pageTitle", "Home Page");
		request.setAttribute("contentPage", "/WEB-INF/views/common/home-page.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}
}
