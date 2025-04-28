package com.enth.ecomusic.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

		request.setAttribute("pageTitle", "Home Page");
		request.setAttribute("contentPage", "/WEB-INF/views/homePage.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
    }
	
	
	
}