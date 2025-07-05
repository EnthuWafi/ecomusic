<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-5">
	<h2 class="mb-4">Your Active Subscriptions</h2>

	<c:if test="${empty subscriptions}">
		<div class="alert alert-info">You have no active subscriptions.
		</div>
	</c:if>

	<div class="row">
		<c:forEach var="sub" items="${subscriptions}" varStatus="status">
			<div class="col-md-6 mb-4">
				<div class="card h-100 shadow-sm">
					<div class="card-header">
						<strong><c:out value="${sub.subscriptionPlan.name}" /></strong> <span
							class="float-right"> <c:choose>
								<c:when test="${sub.paymentStatus == 'paid'}">
									<span class="badge badge-success">Paid</span>
								</c:when>
								<c:otherwise>
									<span class="badge badge-warning"><c:out
											value="${sub.paymentStatus}" /></span>
								</c:otherwise>
							</c:choose>
						</span>
					</div>
					<div class="card-body">
						<p class="mb-1">
							<strong>Billing Cycle:</strong>
							<c:out value="${sub.subscriptionPlan.billingCycle}" />
						</p>
						<p class="mb-1">
							<strong>Price:</strong> RM
							<fmt:formatNumber value="${sub.subscriptionPlan.price}"
								type="currency" />
						</p>
						<p class="mb-1">
							<strong>Start Date:</strong>
							<fmt:formatDate value="${sub.startDateDate}" type="date" />
						</p>
						<p class="mb-0">
							<strong>Amount Paid:</strong> RM
							<fmt:formatNumber value="${sub.amountPaid}" type="currency" />
						</p>
					</div>
					<div class="card-footer text-right">
						<form method="post"
							action="${pageContext.request.contextPath}/user/subscription/cancel">
							<input type="hidden" name="subId" value="${sub.subscriptionId}" />
							<input type="submit" class="btn btn-sm btn-outline-danger"
								value="Cancel" />
						</form>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
