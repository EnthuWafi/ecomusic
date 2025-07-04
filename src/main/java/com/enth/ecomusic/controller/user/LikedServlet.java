package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.LikeDTO;
import com.enth.ecomusic.model.dto.PlayHistoryDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.LikeService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class LikedServlet
 */
@WebServlet("/user/liked")
public class LikedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private LikeService likeService;
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.likeService = ctx.getLikeService();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikedServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		
		if (user == null ) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		int limit = 10;
		int offset = 0;
		
		List<LikeDTO> likeList = likeService.getLikedSongsForUser(user.getUserId(), offset, limit, user.getUserId());
		
		request.setAttribute("likeList", likeList);
		request.setAttribute("pageTitle", "Play History");
		request.setAttribute("contentPage", "/WEB-INF/views/user/liked.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
