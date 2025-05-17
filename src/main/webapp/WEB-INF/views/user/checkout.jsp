<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://js.stripe.com/basil/stripe.js"></script>

<div class="container mt-5">
  <div class="card shadow p-4">
    <h2 class="mb-4">Subscribe to the <strong>${subscriptionPlan.name}</strong> Plan</h2>

    <div class="row">
      <div class="col-md-6">
        <ul class="list-group mb-3">
          <li class="list-group-item d-flex justify-content-between align-items-center">
            <strong>Plan Type</strong>
            <span class="badge bg-primary text-uppercase">${subscriptionPlan.planType}</span>
          </li>
          <li class="list-group-item d-flex justify-content-between">
            <strong>Billing</strong> 
            <span>${subscriptionPlan.billingCycle}</span>
          </li>
          <li class="list-group-item d-flex justify-content-between">
            <strong>Price</strong> 
            <span>RM${subscriptionPlan.price}</span>
          </li>
        </ul>

        <h5>Description</h5>
        <p>${subscriptionPlan.description}</p>

        <c:if test="${not empty subscriptionPlan.features}">
          <h5>Features</h5>
          <ul>
            <c:forEach var="feature" items="${subscriptionPlan.features}">
              <li>${feature}</li>
            </c:forEach>
          </ul>
        </c:if>

        <button id="subscribeButton" class="btn btn-success btn-lg mt-3">
          Pay with Stripe
        </button>
      </div>

      <div class="col-md-6">
        <div id="stripe-checkout-element" class="border p-4 rounded" style="min-height: 300px;"></div>
      </div>
    </div>
  </div>
</div>

<script>
  const stripe = Stripe("${stripePublicKey}");

  document.getElementById("subscribeButton").addEventListener("click", async () => {
    const response = await fetch("${pageContext.request.contextPath}/user/subscription/checkout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        priceId: "${subscriptionPlan.stripePriceId}",
        planId: "${subscriptionPlan.subscriptionPlanId}",
        planType: "${subscriptionPlan.planType}"
      })
    });

    if (!response.ok) {
      alert("Failed to initiate Stripe checkout.");
      return;
    }

    const data = await response.json();
    const clientSecret = data.clientSecret;

    const checkout = stripe.initEmbeddedCheckout({ clientSecret });
    checkout.mount("#stripe-checkout-element");
  });
</script>