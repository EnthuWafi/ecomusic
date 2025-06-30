package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

/**
 * Servlet implementation class ArtistSubscriptionReturnServlet
 */
@WebServlet("/user/subscription/return")
public class SubscriptionReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubscriptionReturnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(false);
        UserDTO user = (UserDTO) httpSession.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String sessionId = request.getParameter("session_id");
        if (sessionId == null) {
            CommonUtil.addMessage(httpSession, ToastrType.ERROR, "Payment failed or was canceled!");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        PlanType intent = (PlanType) httpSession.getAttribute("subscriptionIntent");
    	
    	if (intent == null) {
    		CommonUtil.addMessage(httpSession, ToastrType.ERROR, "Unknown payment intent");
            response.sendRedirect(request.getContextPath() + "/home");
    		return;
    	}
    	httpSession.removeAttribute("subscriptionIntent"); 
    	

        try {
        	   	
            Session session = Session.retrieve(sessionId);
            String status = session.getStatus(); 
            
            if ("complete".equals(status)) {
                
            	if (intent == PlanType.CREATOR) {
            	    CommonUtil.addMessage(httpSession, ToastrType.SUCCESS, "You're now an artist!");
            	} else if (intent == PlanType.LISTENER) {
            	    CommonUtil.addMessage(httpSession, ToastrType.SUCCESS, "You're now a Premium user!");
            	} else {
            	    CommonUtil.addMessage(httpSession, ToastrType.INFO, "Subscription complete.");
            	}
            	
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                CommonUtil.addMessage(httpSession, ToastrType.WARNING, "Subscription not completed.");
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (StripeException e) {
            e.printStackTrace();
            CommonUtil.addMessage(httpSession, ToastrType.ERROR, "Stripe error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

}
