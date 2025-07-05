package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.google.gson.reflect.TypeToken;
import com.stripe.exception.StripeException;

/**
 * Servlet implementation class SubscriptionPlanAPIServlet
 */
@WebServlet("/api/subscription-plan/*")
public class SubscriptionPlanAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SubscriptionService subscriptionService;
	private StripeService stripeService;

	@Override
	public void init() throws ServletException {
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.subscriptionService = ctx.getSubscriptionService();
		this.stripeService = ctx.getStripeService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubscriptionPlanAPIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			handleFetchSubscriptionPlans(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				handleFetchSubscriptionPlan(Integer.parseInt(pathParts[0]), request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchSubscriptionPlan(int subscriptionPlanId, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SubscriptionPlanDTO subscriptionPlan = subscriptionService.getSubscriptionPlanById(subscriptionPlanId);

		Map<String, Object> data = new HashMap<>();
		data.put("results", subscriptionPlan);
		ResponseUtil.sendJson(response, data);

	}

	private void handleFetchSubscriptionPlans(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<SubscriptionPlanDTO> subscriptionList = subscriptionService.getAllSubscriptionPlans();

		Map<String, Object> data = new HashMap<>();
		data.put("results", subscriptionList);
		ResponseUtil.sendJson(response, data);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			createSubscriptionPlan(request, response);
			return;
		}
		ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API Path!");
	}

	private void createSubscriptionPlan(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String jsonBody;
		try {
			jsonBody = IOUtils.toString(request.getReader());
		} catch (IOException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Failed to read request body: " + e.getMessage());
			return;
		}

		Map<String, Object> body;
		try {
			body = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {
			});
		} catch (Exception e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON: " + e.getMessage());
			return;
		}

		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
			return;
		}

		if (!(currentUser.isAdmin() || currentUser.isSuperAdmin())) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			return;
		}

		SubscriptionPlan plan = new SubscriptionPlan();
		try {
			plan.setName(((String) body.getOrDefault("name", "")).trim());
			plan.setBillingCycle(((String) body.getOrDefault("billingCycle", "")).trim().toLowerCase());
			plan.setPrice(Double.parseDouble(body.getOrDefault("price", "0").toString()));
			plan.setDescription(((String) body.getOrDefault("description", "")).trim());
			plan.setPlanType(PlanType.fromString(((String) body.getOrDefault("planType", "")).trim()));
			@SuppressWarnings("unchecked")
			List<String> features = (List<String>) body.getOrDefault("features", Collections.emptyList());
			features = features.stream().map(String::trim).collect(Collectors.toList());
			plan.setFeatures(features);
		} catch (IllegalArgumentException | NullPointerException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Invalid plan data: " + e.getMessage());
			return;
		}

		try {
			boolean ok = stripeService.createSubscriptionPlan(plan, currentUser);
			if (ok) {
				ResponseUtil.sendJson(response, "Subscription plan created successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot create subscription plan");
			}
		} catch (StripeException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Stripe error: " + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID is required!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				editSubscriptionPlan(Integer.parseInt(pathParts[0]), request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void editSubscriptionPlan(int subscriptionPlanId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String jsonBody;
		try {
			jsonBody = IOUtils.toString(request.getReader());
		} catch (IOException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Failed to read request body: " + e.getMessage());
			return;
		}

		Map<String, Object> body;
		try {
			body = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {
			});
		} catch (Exception e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON: " + e.getMessage());
			return;
		}

		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
			return;
		}

		if (!(currentUser.isAdmin() || currentUser.isSuperAdmin())) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			return;
		}

		SubscriptionPlan plan = new SubscriptionPlan();
		plan.setSubscriptionPlanId(subscriptionPlanId);
		try {
			plan.setName(((String) body.get("name")).trim());
			plan.setBillingCycle(((String) body.get("billingCycle")).trim().toLowerCase());
			plan.setPrice(Double.parseDouble(body.get("price").toString()));
			plan.setDescription(((String) body.get("description")).trim());
			plan.setPlanType(PlanType.fromString(((String) body.get("planType")).trim()));
			@SuppressWarnings("unchecked")
			List<String> features = (List<String>) body.get("features");
			features = features.stream().map(String::trim).collect(Collectors.toList());
			plan.setFeatures(features);
		} catch (IllegalArgumentException | NullPointerException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST,
					"Invalid plan data: " + e.getMessage());
			return;
		}

		try {
			boolean ok = stripeService.editSubscriptionPlan(plan, currentUser);
			if (ok) {
				ResponseUtil.sendJson(response, "Subscription plan updated successfully");
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot update subscription plan");
			}
		} catch (StripeException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Stripe error: " + e.getMessage());
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID is required!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				deleteSubscriptionPlan(Integer.parseInt(pathParts[0]), request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void deleteSubscriptionPlan(int subscriptionPlanId, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
			return;
		}
		if (!(currentUser.isAdmin() || currentUser.isSuperAdmin())) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
			return;
		}

		boolean ok = subscriptionService.deleteSubscriptionPlan(subscriptionPlanId, currentUser);
		if (ok) {
			ResponseUtil.sendJson(response, "Subscription plan deleted successfully");
		} else {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot delete subscription plan");
		}

	}

}
