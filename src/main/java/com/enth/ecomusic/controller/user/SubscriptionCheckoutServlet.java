package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;
import com.stripe.exception.StripeException;

/**
 * Servlet implementation class ArtistSubscriptionCheckoutServlet
 */
@WebServlet("/user/subscription/checkout")
public class SubscriptionCheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SubscriptionService subscriptionService;
	private StripeService stripeService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		subscriptionService = ctx.getSubscriptionService();
		stripeService = ctx.getStripeService();
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    HttpSession session = request.getSession(false);

	    UserDTO user = (UserDTO) session.getAttribute("user");

	    // Validate user object
	    if (user.getUserId() == 0 || user.getEmail() == null || user.getEmail().isBlank()) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "User session is invalid. Please log in again.");
	        response.sendRedirect(request.getContextPath() + "/login");
	        return;
	    }

	    String subscriptionPlanId = request.getParameter("planId");

	    if (subscriptionPlanId == null || !subscriptionPlanId.matches("\\d+")) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Invalid subscription plan selected.");
	        response.sendRedirect(request.getContextPath() + "/choose-plan");
	        return;
	    }

	    SubscriptionPlanDTO plan = subscriptionService.getSubscriptionPlanById(Integer.parseInt(subscriptionPlanId));

	    if (plan == null) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "That subscription plan doesn't exist.");
	        response.sendRedirect(request.getContextPath() + "/choose-plan");
	        return;
	    }

	    String userId = String.valueOf(user.getUserId());
	    String email = user.getEmail();
	    String returnUrl = CommonUtil.getBaseUrl(request) + "/user/subscription/return";

	    try {
	        String sessionURL = stripeService.createRedirectCheckoutSessionForPlan(plan, returnUrl, userId, email);

	        if (sessionURL == null || sessionURL.isBlank()) {
	            CommonUtil.addMessage(session, ToastrType.ERROR, "Failed to initiate checkout. Please try again.");
	            response.sendRedirect(request.getContextPath() + "/choose-plan");
	            return;
	        }
	        
	        session.setAttribute("subscriptionIntent", plan.getPlanType());
	        response.sendRedirect(sessionURL);
	    } catch (StripeException e) {
	        e.printStackTrace(); // Optional: use a logger
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Something went wrong with Stripe. Try again later.");
	        response.sendRedirect(request.getContextPath() + "/choose-plan");
	    }
	}



}
