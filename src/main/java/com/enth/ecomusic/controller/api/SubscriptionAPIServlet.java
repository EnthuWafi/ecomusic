package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class SubscriptionAPIServlet
 */
@WebServlet("/api/subscription/*")
public class SubscriptionAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private SubscriptionService subscriptionService;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.subscriptionService = ctx.getSubscriptionService();
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
		// TODO Auto-generated method stub
		
	}
	private void handleFetchSubscription(int int1, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		// TODO Auto-generated method stub
		
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
	private void cancelSubscription(int int1, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
