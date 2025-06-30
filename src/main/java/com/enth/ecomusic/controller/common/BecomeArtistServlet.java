package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class BecomeCreatorServlet
 */
@WebServlet("/become-artist")
public class BecomeArtistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private SubscriptionService subscriptionService;

    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
    	subscriptionService = ctx.getSubscriptionService();
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BecomeArtistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   	
    	
    	List<SubscriptionPlanDTO> plans = subscriptionService.getAllSubscriptionPlansForCreator();
    	
    	request.setAttribute("subscriptionPlanList", plans);
        request.setAttribute("pageTitle", "Become an Artist");
        request.setAttribute("contentPage", "/WEB-INF/views/common/choose-plan.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
    }

}
