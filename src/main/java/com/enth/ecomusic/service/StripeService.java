package com.enth.ecomusic.service;

import com.enth.ecomusic.model.dto.SubscriptionPlanDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
	
	//Embedded
	public String createCheckoutSessionForPlan(SubscriptionPlanDTO plan, String returnUrl, String userId, String email)
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
	
	//Redirect
	public String createRedirectCheckoutSessionForPlan(SubscriptionPlanDTO plan, String returnUrl, String userId, String email)
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
