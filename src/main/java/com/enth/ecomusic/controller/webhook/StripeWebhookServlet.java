package com.enth.ecomusic.controller.webhook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Servlet implementation class StripeWebhookServlet
 */
@WebServlet("/StripeWebhookServlet")
public class StripeWebhookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STRIPE_WEBHOOK_SECRET = "whsec_...";
    private static final Logger logger = Logger.getLogger(StripeWebhookServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public StripeWebhookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder payloadBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                payloadBuilder.append(line);
            }
        }

        String payload = payloadBuilder.toString();
        String sigHeader = req.getHeader("Stripe-Signature");

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            logger.warning("Webhook signature verification failed.");
            resp.setStatus(400);
            return;
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject().orElse(null);

            if (session != null && "subscription".equals(session.getMode())) {
                // TODO: extract userId from client_reference_id
                // TODO: store subscription in DB (including session.getId() or subscription ID)

                String subscriptionId = session.getSubscription();
                String userId = session.getClientReferenceId();
                String paymentRef = session.getId(); // Or subscriptionId — depending on your setup

                logger.info("✅ New subscription: userId=" + userId + " ref=" + paymentRef);

                // Call a service class here (e.g. SubscriptionService.insertSubscription(...))
            }
        }

        resp.setStatus(200);
    }

}
