package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.CommonUtil;
import com.stripe.exception.StripeException;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath() + "/become-artist");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String subscriptionPlanId = request.getParameter("planId");

		SubscriptionPlan plan = subscriptionService.getSubscriptionPlanById(Integer.parseInt(subscriptionPlanId));

		// Originally intended to make this a checkout embed, but going to try redirect
//		request.setAttribute("subscriptionPlan", plan);
//		
//		request.setAttribute("stripePublicKey", getServletContext().getAttribute("stripePublicKey"));
//		request.setAttribute("pageTitle", "Complete Your Subscription");
//		request.setAttribute("contentPage", "/WEB-INF/views/user/checkout.jsp");
//		request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
		
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");
		
		String userId = String.valueOf(user.getUserId());
	    String email = user.getEmail();
	    String returnUrl = CommonUtil.getBaseUrl(request) + "/user/subscription/return";

		try {
			String sessionURL = StripeService.createRedirectCheckoutSessionForPlan(plan, returnUrl, userId, email);

			response.sendRedirect(sessionURL);
		} catch (StripeException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Stripe error: " + e.getMessage());
		}

	}

}
