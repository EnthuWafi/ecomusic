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
import com.enth.ecomusic.util.ToastrType;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

/**
 * Servlet implementation class ArtistSubscriptionReturnServlet
 */
@WebServlet("/user/subscription/artist/return")
public class ArtistSubscriptionReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistSubscriptionReturnServlet() {
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
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String sessionId = request.getParameter("session_id");
        if (sessionId == null) {
            CommonUtil.addMessage(httpSession, ToastrType.ERROR, "Missing session ID.");
            response.sendRedirect(request.getContextPath() + "/become-artist");
            return;
        }

        try {
            Session session = Session.retrieve(sessionId);
            String status = session.getStatus(); // "complete", "open", etc.

            if ("complete".equals(status)) {
                CommonUtil.addMessage(httpSession, ToastrType.SUCCESS, "You're now an artist!");
                response.sendRedirect(request.getContextPath() + "/artist/dashboard");
            } else {
                CommonUtil.addMessage(httpSession, ToastrType.WARNING, "Subscription not completed.");
                response.sendRedirect(request.getContextPath() + "/become-artist");
            }

        } catch (StripeException e) {
            e.printStackTrace();
            CommonUtil.addMessage(httpSession, ToastrType.ERROR, "Stripe error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/become-artist");
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
