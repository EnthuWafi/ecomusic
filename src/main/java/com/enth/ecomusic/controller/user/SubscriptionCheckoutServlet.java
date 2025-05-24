package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.enth.ecomusic.model.SubscriptionPlan;
import com.enth.ecomusic.service.SubscriptionService;

/**
 * Servlet implementation class ArtistSubscriptionCheckoutServlet
 */
@WebServlet("/user/subscription/checkout")
public class SubscriptionCheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SubscriptionService subscriptionService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		subscriptionService = new SubscriptionService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubscriptionCheckoutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath() + "/become-artist");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String subscriptionPlanId  = request.getParameter("planId");
			
		SubscriptionPlan plan = subscriptionService.getSubscriptionPlanById(Integer.parseInt(subscriptionPlanId));
		
		request.setAttribute("subscriptionPlan", plan);
		
		request.setAttribute("stripePublicKey", getServletContext().getAttribute("stripePublicKey"));
		request.setAttribute("pageTitle", "Complete Your Subscription");
		request.setAttribute("contentPage", "/WEB-INF/views/user/checkout.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);

	}


}
