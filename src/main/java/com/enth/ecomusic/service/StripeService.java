package com.enth.ecomusic.service;

import com.enth.ecomusic.model.SubscriptionPlan;
import com.enth.ecomusic.util.AppConfig;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
	static {
		AppConfig config = new AppConfig();
		Stripe.apiKey = config.get("stripeSecretKey");
	}

	public static String createCheckoutSessionForPlan(SubscriptionPlan plan, String returnUrl, String userId, String email)
			throws StripeException {
		SessionCreateParams params = SessionCreateParams.builder().setUiMode(SessionCreateParams.UiMode.EMBEDDED)
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setReturnUrl(returnUrl + "?session_id={CHECKOUT_SESSION_ID}").setClientReferenceId(userId)
				.putMetadata("subscription_plan_id", String.valueOf(plan.getSubscriptionPlanId()))
				.setCustomerEmail(email)
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPrice(plan.getStripePriceId())
						.build())
				.build();

		Session session = Session.create(params);
		return session.getClientSecret();
	}
	
	public static String createRedirectCheckoutSessionForPlan(SubscriptionPlan plan, String returnUrl, String userId, String email)
	        throws StripeException {
	    SessionCreateParams params = SessionCreateParams.builder()
	            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
	            .setSuccessUrl(returnUrl + "?session_id={CHECKOUT_SESSION_ID}")
	            .setCancelUrl(returnUrl)
	            .setClientReferenceId(userId)
	            .putMetadata("subscription_plan_id", String.valueOf(plan.getSubscriptionPlanId()))
	            .setCustomerEmail(email)
	            .addLineItem(SessionCreateParams.LineItem.builder()
	                    .setQuantity(1L)
	                    .setPrice(plan.getStripePriceId())
	                    .build())
	            .build();

	    Session session = Session.create(params);
	    return session.getUrl(); 
	}

}
