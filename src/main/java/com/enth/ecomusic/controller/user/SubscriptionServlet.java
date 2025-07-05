package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet implementation class SubscriptionServlet
 */
@WebServlet("/user/subscription")
public class SubscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private SubscriptionService subscriptionService;
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		this.subscriptionService = ctx.getSubscriptionService();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubscriptionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserDTO user = (UserDTO) request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		List<SubscriptionDTO> subscriptionList = new ArrayList<>();
		
		for (PlanType planType : List.of(PlanType.CREATOR, PlanType.LISTENER)) {
	        SubscriptionDTO sub = subscriptionService.getLatestSubscriptionDTOByUserAndPlan(user.getUserId(), planType, user);
	        if (sub != null && sub.getEndDate() == null) {
	            subscriptionList.add(sub);
	        }
	    }
		
		request.setAttribute("subscriptions", subscriptionList);
		request.setAttribute("pageTitle", "Subscription List");
		request.setAttribute("contentPage", "/WEB-INF/views/user/subscription.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout-main.jsp").forward(request, response);
	}


}
