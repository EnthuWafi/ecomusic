package com.enth.ecomusic.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.SubscriptionPlan;
import com.enth.ecomusic.model.entity.UserSubscription;
import com.enth.ecomusic.model.enums.PlanType;
import com.enth.ecomusic.util.AppConfig;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.Price;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.PlanCreateParams.Interval;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {

	private static final String CREATOR_PRODUCT_ID = AppConfig.get("adminProductId");
	private static final String LISTENER_PRODUCT_ID = AppConfig.get("listenerProductId");

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

		if (plan == null) {
			return null;
		}

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

	public boolean cancelSubscription(String stripeSubscriptionId) throws StripeException {
		Subscription canceled = Subscription.retrieve(stripeSubscriptionId).cancel();
		return canceled != null && "canceled".equals(canceled.getStatus()) || canceled.getCancelAt() != null;
	}

	private String getProductIdForPlan(PlanType planType) {
		switch (planType) {
		case PlanType.LISTENER:
			return LISTENER_PRODUCT_ID;
		case PlanType.CREATOR:
			return CREATOR_PRODUCT_ID;
		default:
			throw new IllegalArgumentException("Unknown plan type: " + planType);
		}
	}

	private String createStripePrice(SubscriptionPlan plan, String productId) throws StripeException {

		PriceCreateParams.Recurring.Interval interval = null;
		switch (plan.getBillingCycle()) {
		case "yearly":
			interval = PriceCreateParams.Recurring.Interval.YEAR;
			break;
		case "monthly":
			interval = PriceCreateParams.Recurring.Interval.MONTH;
			break;
		default:
			interval = PriceCreateParams.Recurring.Interval.MONTH;
			break;
		}

		PriceCreateParams params = PriceCreateParams.builder().setUnitAmount((long) (plan.getPrice() * 100))
				.setCurrency("myr").setRecurring(PriceCreateParams.Recurring.builder().setInterval(interval).build())
				.setProduct(productId).build();

		Price price = Price.create(params);
		return price.getId();
	}

	public boolean createSubscriptionPlan(SubscriptionPlan subscriptionPlan, UserDTO currentUser)
			throws StripeException {
		String productId = getProductIdForPlan(subscriptionPlan.getPlanType());

		String stripePriceId = createStripePrice(subscriptionPlan, productId);
		subscriptionPlan.setStripePriceId(stripePriceId);

		return subscriptionService.createSubscriptionPlan(subscriptionPlan, currentUser);
	}

	public boolean editSubscriptionPlan(SubscriptionPlan plan, UserDTO currentUser) throws StripeException {
		SubscriptionPlanDTO existingPlan = subscriptionService.getSubscriptionPlanById(plan.getSubscriptionPlanId());
		if (existingPlan == null) {
			System.err.println("Plan not found.");
			return false;
		}

		boolean priceChanged = plan.getPrice() != existingPlan.getPrice()
				|| !plan.getBillingCycle().equalsIgnoreCase(existingPlan.getBillingCycle());

		if (priceChanged) {
			String productId = getProductIdForPlan(plan.getPlanType());
			String stripePriceId = createStripePrice(plan, productId);
			plan.setStripePriceId(stripePriceId);
		} else {
			plan.setStripePriceId(existingPlan.getStripePriceId()); // Reuse existing price
		}

		return subscriptionService.updateSubscriptionPlan(plan, currentUser);
	}

	public boolean deleteSubscriptionPlan(int subscriptionPlanId, UserDTO currentUser) throws StripeException {
		SubscriptionPlanDTO existingPlan = subscriptionService.getSubscriptionPlanById(subscriptionPlanId);
		if (existingPlan == null) {
			System.err.println("Plan not found.");
			return false;
		}

		return subscriptionService.deleteSubscriptionPlan(subscriptionPlanId, currentUser);
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

		boolean success = subscriptionService.createSubscription(sub, planType);
		if (!success) {
			System.err.println("Failed to create subscription for ID: " + planIdStr);
		}

	}

	public void processRecurringPayment(Event event) throws StripeException {
		Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject()
				.orElseThrow(() -> new RuntimeException("Session data could not be deserialized"));

		if ("subscription_create".equals(invoice.getBillingReason())) {
			System.err.println("Skipping invoice.paid event — initial subscription invoice");
			return;
		}

		List<InvoiceLineItem> lineItems = invoice.getLines().getData();

		String subscriptionStripeId = null;
		for (InvoiceLineItem item : lineItems) {
			if (item.getSubscription() != null) {
				subscriptionStripeId = item.getSubscription();
				break;
			}
		}

		long cents = invoice.getAmountPaid();
		BigDecimal bd = BigDecimal.valueOf(cents, 2);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		double amountPaid = bd.doubleValue();

		boolean success = subscriptionService.updateSubscriptionAmountPaid(subscriptionStripeId, amountPaid);
		if (!success) {
			System.err.println("Failed to update subscription for ID: " + subscriptionStripeId);
		}

	}

	public void processSubscriptionCancellation(Event event) throws StripeException {
		Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject()
				.orElseThrow(() -> new RuntimeException("Session data could not be deserialized"));

		String subscriptionStripeId = subscription.getId();

		boolean success = subscriptionService.updateSubscriptionEndDate(subscriptionStripeId);
		if (!success) {
			System.err.println("Failed to delete subscription for ID: " + subscriptionStripeId);
		}
	}

}
