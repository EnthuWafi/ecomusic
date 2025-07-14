package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.PlaylistMusic;
import com.enth.ecomusic.service.MailService;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.JsonUtil;
import com.enth.ecomusic.util.ResponseUtil;
import com.enth.ecomusic.util.ToastrType;
import com.google.gson.reflect.TypeToken;
import com.stripe.exception.StripeException;

/**
 * Servlet implementation class SubscriptionAPIServlet
 */
@WebServlet("/api/subscription/*")
public class SubscriptionAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private SubscriptionService subscriptionService;
	private StripeService stripeService;
	private MailService mailService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.subscriptionService = ctx.getSubscriptionService();
		this.stripeService = ctx.getStripeService();
		this.mailService = ctx.getMailService();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubscriptionAPIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			handleFetchSubscriptions(request, response);
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				handleFetchSubscription(Integer.parseInt(pathParts[0]), request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}

	private void handleFetchSubscriptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

		int limit = CommonUtil.parseIntLimitParam(request.getParameter("limit"), 5, 50);
		int offset = CommonUtil.parseIntLimitParam(request.getParameter("offset"), 0, Integer.MAX_VALUE);

		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		
		List<SubscriptionDTO> subscriptionList = subscriptionService.getAllSubscription(offset, limit, currentUser);
		
		Map<String, Object> data = new HashMap<>();
		data.put("limit", limit);
		data.put("offset", offset);
		data.put("results", subscriptionList);
		ResponseUtil.sendJson(response, data);
		
	}
	private void handleFetchSubscription(int subscriptionId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
		UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
		
		SubscriptionDTO subscription = subscriptionService.getSubscriptionById(subscriptionId, currentUser);
		
		Map<String, Object> data = new HashMap<>();
		data.put("results", subscription);
		ResponseUtil.sendJson(response, data);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID is required!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1) {
				cancelSubscription(Integer.parseInt(pathParts[0]), request, response);

			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}
	private void cancelSubscription(int subscriptionId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String jsonBody;
        try {
            // Read the entire request body as a String using UTF-8 encoding
            jsonBody = IOUtils.toString(request.getReader()); 
        } catch (IOException e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Failed to read request body: " + e.getMessage());
            return;
        }

        Map<String, Object> requestBody;
        try {
            requestBody = JsonUtil.fromJson(jsonBody, new TypeToken<Map<String, Object>>() {});
            
            String message = requestBody.get("message").toString();
            
            UserDTO currentUser = (UserDTO) request.getSession().getAttribute("user");
            
            SubscriptionDTO subscription = subscriptionService.getSubscriptionById(subscriptionId, currentUser);
    	    
    	    if (subscription == null) {
    	    	ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "No subscription found" + subscriptionId);
    	        return;
    	    }
            
    		if (stripeService.cancelSubscription(subscription.getPaymentGatewayRef())) {
    			String messageBody = mailService.buildCancelSubscriptionEmail(subscription.getUser(), subscription, message);
    		    
    		    mailService.sendEmail(
    		        subscription.getUser().getEmail(),
    		        "Moosic Subscription Cancellation",
    		        messageBody
    		    );
    			ResponseUtil.sendJson(response, "Successfully cancelled subscription " + subscriptionId);
    		} else {
    			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot cancel subscription " + subscriptionId);
    		}
            

        } 
        catch (StripeException e) {
        	ResponseUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Stripe error: " + e.getMessage());
	        return;
	    }
        catch (Exception e) {
            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body: " + e.getMessage());
            return;
        }
	}

}
