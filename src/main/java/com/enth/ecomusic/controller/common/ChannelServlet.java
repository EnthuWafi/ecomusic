package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet implementation class ChannelServlet
 */
@WebServlet("/channel/*")
public class ChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelServlet() {
        super();
        AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
        this.userService = ctx.getUserService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// This is going to display info about this user channel
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID required!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				int userId = Integer.parseInt(pathParts[0]);
				showChannel(userId, request, response);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path does not exist");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
		
	}

	private void showChannel(int userId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserDTO user = userService.getUserDTOById(userId);
		
		if (user == null) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "User with that channel does not exist");
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		if (user.isAdmin() || user.isSuperAdmin()) {
			CommonUtil.addMessage(request.getSession(), ToastrType.ERROR, "Admin does not have a channel");
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		request.setAttribute("pageTitle", user.getUsername() + "'s Channel");
		request.setAttribute("artist", user);
		request.setAttribute("contentPage", "/WEB-INF/views/common/channel.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
