<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container my-5">
	<h1 class="mb-4">Subscription Plans</h1>
	<div class="row g-4">
		<c:forEach var="plan" items="${subscriptionPlanList}">
			<div class="col-lg-4 col-md-6">
				<div class="card h-100 shadow-sm">
					<div class="card-header bg-primary text-white">
						<h5 class="card-title mb-0">${plan.name}</h5>
						<small class="d-block"> Created: <fmt:formatDate
								value="${plan.createdAt}" pattern="MMM d, yyyy" />
						</small>
					</div>
					<div class="card-body">
						<h6 class="card-subtitle mb-2 text-muted">${plan.planType} •
							${plan.billingCycle}</h6>
						<p class="card-text">${plan.description}</p>
						<ul class="list-unstyled">
							<c:forEach var="feature" items="${plan.features}">
								<li>✔ ${feature}</li>
							</c:forEach>
						</ul>
					</div>
					<div
						class="card-footer d-flex justify-content-between align-items-center">
						<span class="h5 mb-0"> RM ${plan.price} </span>
						<form action="${pageContext.request.contextPath}/user/subscription/checkout" method="get">
							<input type="hidden" name="planId"
								value="${plan.subscriptionPlanId}" />
							<button type="submit" class="btn btn-success">Subscribe
							</button>
						</form>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
