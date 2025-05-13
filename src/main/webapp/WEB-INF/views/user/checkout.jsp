<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://js.stripe.com/v3/"></script>

<h2>Become an Artist</h2>
<button id="subscribeButton">Subscribe Now</button>
<div id="stripe-checkout-element" style="margin-top: 2rem;"></div>

<script>
  const stripe = Stripe("${stripePublicKey}");

  document.getElementById("subscribeButton").addEventListener("click", async () => {
    const response = await fetch("${pageContext.request.contextPath}/user/subscription/artist/checkout", {
      method: "POST"
    });

    if (!response.ok) {
      alert("Failed to start checkout.");
      return;
    }

    const data = await response.json();
    const clientSecret = data.clientSecret;

    stripe.initEmbeddedCheckout({ clientSecret }).mount("#stripe-checkout-element");
  });
</script>
