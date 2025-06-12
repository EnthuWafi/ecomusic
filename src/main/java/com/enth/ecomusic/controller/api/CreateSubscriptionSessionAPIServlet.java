package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.stripe.exception.StripeException;

/**
 * Servlet implementation class SubscriptionAPIServlet
 */
@WebServlet("/api/subscription/create")
public class CreateSubscriptionSessionAPIServlet extends HttpServlet {
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
    public CreateSubscriptionSessionAPIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		// Read JSON from request body
	    StringBuilder jsonBody = new StringBuilder();
	    String line;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null) {
	        jsonBody.append(line);
	    }

	    // Parse JSON into Map
	    Map<String, Object> requestBody = JsonUtil.fromJson(jsonBody.toString(), new TypeToken<Map<String, Object>>() {});

	    int subscriptionPlanId = Integer.parseInt(requestBody.get("planId").toString());
	    String userId = String.valueOf(user.getUserId());
	    String email = user.getEmail();
	    String returnUrl = CommonUtil.getBaseUrl(request) + "/user/subscription/return";

	    SubscriptionPlanDTO plan = subscriptionService.getSubscriptionPlanById(subscriptionPlanId);

		try {

			String clientSecret = stripeService.createCheckoutSessionForPlan(plan, returnUrl, userId, email);

			response.setContentType("application/json");
			
			Map<String, Object> responseData = new HashMap<>();
			responseData.put("clientSecret", clientSecret);

			response.setContentType("application/json");
			response.getWriter().write(JsonUtil.toJson(responseData));
		} catch (StripeException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Stripe error: " + e.getMessage());
		}
	}

}
