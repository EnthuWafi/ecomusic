package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class SessionAPIServlet
 */
@WebServlet("/api/session")
public class SessionAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionAPIServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(false) != null && (request.getSession().getAttribute("user") != null)) {
			UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
			ResponseUtil.sendJson(response, currentUser);
			return;
		}
		ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Session does not exist");
	}


}
