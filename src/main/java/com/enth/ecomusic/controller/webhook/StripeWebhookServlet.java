package com.enth.ecomusic.controller.webhook;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.enth.ecomusic.model.UserSubscription;
import com.enth.ecomusic.service.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.checkout.Session;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Servlet implementation class StripeWebhookServlet
 */
@WebServlet("/webhook/stripe")
public class StripeWebhookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//stripe listen --forward-to localhost:8081/ecomusic/webhook/stripe
	private static final String STRIPE_WEBHOOK_SECRET = "whsec_555ab27a8876b601deb279f6b20c238110e68060d74af0651118ce2679deaa68";
	private static final Logger logger = Logger.getLogger(StripeWebhookServlet.class.getName());
	private SubscriptionService subscriptionService;

	@Override
	public void init() throws ServletException {
		Stripe.apiKey = (String) getServletContext().getAttribute("stripeSecretKey");
		subscriptionService = new SubscriptionService();
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
			// Handle recurring subscription payments if needed
			break;
		case "customer.subscription.deleted":
			// Handle cancellations
			break;
		default:
			logger.info("Unhandled event type: " + event.getType());
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void handleCheckoutCompleted(Event event) {
		try {
			Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
			if (session == null) {
				logger.warning("Session data could not be deserialized.");
				return;
			}

			String userId = session.getClientReferenceId();
			String stripeSubId = session.getSubscription(); // e.g., "sub_123..."
			String planIdStr = session.getMetadata().get("subscription_plan_id");

			LocalDate today = LocalDate.now();

			Subscription stripeSub = Subscription.retrieve(stripeSubId);

			String latestInvoiceId = stripeSub.getLatestInvoice();
			Invoice invoice = Invoice.retrieve(latestInvoiceId);

			long cents = invoice.getAmountPaid();
			String paymentStatus = invoice.getStatus();

			 //Convert cents > ringgit with BigDecimal
			BigDecimal bd = BigDecimal.valueOf(cents, 2);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			double amountPaid = bd.doubleValue();
			

			UserSubscription sub = new UserSubscription(Integer.parseInt(userId), today, null, amountPaid, paymentStatus,
					stripeSubId, Integer.parseInt(planIdStr));

			subscriptionService.createSubscription(sub);

			logger.info("Subscription recorded for user ID: " + userId);

		} catch (StripeException | NumberFormatException e) {
			logger.severe("Error processing checkout.session.completed: " + e.getMessage());
		}
	}
}
