package com.enth.ecomusic.controller.webhook;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.enth.ecomusic.service.StripeService;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.AppContext;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Servlet implementation class StripeWebhookServlet
 */
@WebServlet("/webhook/stripe")
public class StripeWebhookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// stripe listen --forward-to localhost:8081/ecomusic/webhook/stripe
	private static final String STRIPE_WEBHOOK_SECRET = AppConfig.get("stripeWebhookKey");
	private static final Logger logger = Logger.getLogger(StripeWebhookServlet.class.getName());
	private StripeService stripeService;

	@Override
	public void init() throws ServletException {
		AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
		stripeService = ctx.getStripeService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StripeWebhookServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletInputStream inputStream = request.getInputStream();
		byte[] payloadBytes = inputStream.readAllBytes();
		String payload = new String(payloadBytes, StandardCharsets.UTF_8);

		String sigHeader = request.getHeader("Stripe-Signature");

		Event event = null;

		try {
			event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
		} catch (SignatureVerificationException e) {
			logger.warning("Webhook signature verification failed.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// Handle event types
		switch (event.getType()) {
		case "checkout.session.completed":
			handleCheckoutCompleted(event);
			break;
		case "invoice.paid":
			handleRecurringPayment(event);
			break;
		case "customer.subscription.deleted":
			handleSubscriptionCancellation(event);
			break;
		default:
			logger.info("Unhandled event type: " + event.getType());
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void handleSubscriptionCancellation(Event event) {
		try {
			stripeService.processSubscriptionCancellation(event);
			logger.info("Subscription Cancellation event processed successfully");

		} catch (StripeException | NumberFormatException e) {
			logger.severe("Error processing customer.subscription.deleted: " + e.getMessage());
		}
	}

	private void handleRecurringPayment(Event event) {
		try {
			stripeService.processRecurringPayment(event);
			logger.info("Recurring payment event processed successfully");

		} catch (StripeException | NumberFormatException e) {
			logger.severe("Error processing invoice.paid: " + e.getMessage());
		}
		
	}

	private void handleCheckoutCompleted(Event event) {
		try {
			stripeService.processCheckoutCompleted(event);
			logger.info("Checkout completed event processed successfully");

		} catch (StripeException | NumberFormatException e) {
			logger.severe("Error processing checkout.session.completed: " + e.getMessage());
		}
	}
}
