package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.dto.SubscriptionDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.service.SubscriptionService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

/**
 * Servlet implementation class SubscriptionCancelServlet
 */
@WebServlet("/user/subscription/cancel")
public class SubscriptionCancelServlet extends HttpServlet {
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
    public SubscriptionCancelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

	    UserDTO user = (UserDTO) session.getAttribute("user");

	    // Validate user object
	    if (user.getUserId() == 0 || user.getEmail() == null || user.getEmail().isBlank()) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "User session is invalid. Please log in again.");
	        session.removeAttribute("user");
	        response.sendRedirect(request.getContextPath() + "/login");
	        return;
	    }
	    

	    String subscriptionId = request.getParameter("subId");

	    if (subscriptionId == null || !subscriptionId.matches("\\d+")) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Invalid subscription selected.");
	        response.sendRedirect(request.getContextPath());
	        return;
	    }
	    
	    SubscriptionDTO subscription = subscriptionService.getSubscriptionById(Integer.parseInt(subscriptionId), user);
	    
	    if (subscription == null) {
	        CommonUtil.addMessage(session, ToastrType.ERROR, "That subscription doesn't exist.");
	        response.sendRedirect(request.getContextPath());
	        return;
	    }
	    
	    //cancel subscription
	    String stripeSubscriptionId = subscription.getPaymentGatewayRef();

	    try {
	        Subscription canceled = Subscription.retrieve(stripeSubscriptionId)
	            .cancel();
	        
	        System.out.println("Canceled subscription: " + canceled.getId());

	        CommonUtil.addMessage(session, ToastrType.SUCCESS, "Subscription successfully canceled.");
	        response.sendRedirect(request.getContextPath() + "/user/profile");
	        return;

	    } catch (StripeException e) {
	        e.printStackTrace();
	        CommonUtil.addMessage(session, ToastrType.ERROR, "Stripe error: " + e.getMessage());
	        response.sendRedirect(request.getContextPath());
	        return;
	    }
	}

}
