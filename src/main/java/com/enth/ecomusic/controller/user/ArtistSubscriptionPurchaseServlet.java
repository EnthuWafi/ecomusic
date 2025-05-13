package com.enth.ecomusic.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.PaymentUtil;
import com.enth.ecomusic.util.ToastrType;
import com.stripe.exception.StripeException;

/**
 * Servlet implementation class ArtistSubscriptionCheckoutServlet
 */
@WebServlet("/user/subscription/artist/checkout")
public class ArtistSubscriptionPurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistSubscriptionPurchaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

        User user = (User) session.getAttribute("user");
        if (CommonUtil.isArtist(user)) {
            CommonUtil.addMessage(session, ToastrType.WARNING, "Already an artist!");
            response.sendRedirect(request.getContextPath() + "/artist/dashboard");
            return;
        }

        String userId = String.valueOf(user.getUserId());
        
        try {
            String returnUrl = request.getRequestURL().toString();
            String clientSecret = PaymentUtil.createCheckoutSessionForPlan("", returnUrl, userId);

            // Forward clientSecret to page so it can embed Stripe checkout
            request.setAttribute("stripePublicKey", getServletContext().getAttribute("stripePublicKey"));
            request.setAttribute("stripeClientSecret", clientSecret);
            request.setAttribute("pageTitle", "Complete Your Subscription");
            request.setAttribute("contentPage", "/WEB-INF/views/user/checkout.jsp");
            request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
        } catch (StripeException e) {
            e.printStackTrace(); // Or log
            CommonUtil.addMessage(session, ToastrType.ERROR, "Stripe error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/become-artist");
        }
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (CommonUtil.isArtist(user)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Already an artist");
            return;
        }

        String userId = String.valueOf(user.getUserId());
        String returnUrl = request.getRequestURL().toString(); // Replace later with success/failure redirect

        try {
            String clientSecret = PaymentUtil.createCheckoutSessionForPlan(
                "price_1ROFrkK0WXRjNyZGqtoNOWV3", // TODO temporary price
                returnUrl,
                userId
            );

            response.setContentType("application/json");
            response.getWriter().write("{\"clientSecret\":\"" + clientSecret + "\"}");
        } catch (StripeException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Stripe error: " + e.getMessage());
        }
    }

}
