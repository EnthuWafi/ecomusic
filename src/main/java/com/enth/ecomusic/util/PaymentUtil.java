package com.enth.ecomusic.util;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class PaymentUtil {
	static {
		AppConfig config = new AppConfig();
		Stripe.apiKey = config.get("stripeSecretKey");
	}

	public static String createCheckoutSessionForPlan(String stripePriceId, String returnUrl, String userId)
			throws StripeException {
		SessionCreateParams params = SessionCreateParams.builder().setUiMode(SessionCreateParams.UiMode.EMBEDDED)
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setReturnUrl(returnUrl + "?session_id={CHECKOUT_SESSION_ID}").setClientReferenceId(userId)
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPrice(stripePriceId).build())
				.build();

		Session session = Session.create(params);
		return session.getClientSecret();
	}

}
