package com.enth.ecomusic.controller.common;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("pageTitle", "Home Page");
		request.setAttribute("contentPage", "/WEB-INF/views/common/homePage.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
	}
}
