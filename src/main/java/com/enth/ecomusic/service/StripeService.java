package com.enth.ecomusic.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.model.enums.PlanType;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {

	private SubscriptionService subscriptionService;

	public StripeService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	// Embedded
	public String createCheckoutSessionForPlan(SubscriptionPlanDTO plan, String returnUrl, String userId, String email)
			throws StripeException {
		SessionCreateParams params = SessionCreateParams.builder().setUiMode(SessionCreateParams.UiMode.EMBEDDED)
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setReturnUrl(returnUrl + "?session_id={CHECKOUT_SESSION_ID}").setClientReferenceId(userId)
				.putMetadata("subscription_plan_id", String.valueOf(plan.getSubscriptionPlanId()))
				.putMetadata("subscription_plan_type", plan.getPlanType().getValue()).setCustomerEmail(email)
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPrice(plan.getStripePriceId())
						.build())
				.build();

		Session session = Session.create(params);
		return session.getClientSecret();
	}

	// Redirect
	public String createRedirectCheckoutSessionForPlan(SubscriptionPlanDTO plan, String returnUrl, String userId,
			String email) throws StripeException {
		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setSuccessUrl(returnUrl + "?session_id={CHECKOUT_SESSION_ID}").setCancelUrl(returnUrl)
				.setClientReferenceId(userId)
				.putMetadata("subscription_plan_id", String.valueOf(plan.getSubscriptionPlanId()))
				.putMetadata("subscription_plan_type", plan.getPlanType().getValue()).setCustomerEmail(email)
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPrice(plan.getStripePriceId())
						.build())
				.build();

		Session session = Session.create(params);
		return session.getUrl();
	}

	public void processCheckoutCompleted(Event event) throws StripeException {
		Session session = (Session) event.getDataObjectDeserializer().getObject()
				.orElseThrow(() -> new RuntimeException("Session data could not be deserialized"));

		String userId = session.getClientReferenceId();
		String stripeSubId = session.getSubscription();
		String planIdStr = session.getMetadata().get("subscription_plan_id");
		PlanType planType = PlanType.fromString(session.getMetadata().get("subscription_plan_type").toLowerCase());

		// Get payment details from Stripe
		LocalDate today = LocalDate.now();

		Subscription stripeSub = Subscription.retrieve(stripeSubId);

		String latestInvoiceId = stripeSub.getLatestInvoice();
		Invoice invoice = Invoice.retrieve(latestInvoiceId);

		long cents = invoice.getAmountPaid();
		String paymentStatus = invoice.getStatus();

		// Convert cents > ringgit with BigDecimal
		BigDecimal bd = BigDecimal.valueOf(cents, 2);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		double amountPaid = bd.doubleValue();

		UserSubscription sub = new UserSubscription(Integer.parseInt(userId), today, null, amountPaid, paymentStatus,
				stripeSubId, Integer.parseInt(planIdStr));

		subscriptionService.createSubscription(sub, planType);
	}

}
