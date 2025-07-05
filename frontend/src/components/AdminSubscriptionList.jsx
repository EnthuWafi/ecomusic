import KpiCard from "./KpiCard.js";

export const AdminSubscriptionList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [subscriptions, setSubscriptions] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	// Pagination state
	const [currentPage, setCurrentPage] = React.useState(1);
	const [totalSubscriptions, setTotalSubscriptions] = React.useState(0);
	const [subscriptionsPerPage] = React.useState(10);

	// Cancel modal state
	const [cancelSubscription, setCancelSubscription] = React.useState(null);
	const [cancelReason, setCancelReason] = React.useState("");

	// Calculate pagination values
	const totalPages = Math.ceil(totalSubscriptions / subscriptionsPerPage);
	const offset = (currentPage - 1) * subscriptionsPerPage;

	const fetchSubscriptions = async (page = 1) => {
		try {
			setLoading(true);
			const pageOffset = (page - 1) * subscriptionsPerPage;
			const subscriptionRes = await fetch(`${baseUrl}/api/subscription?limit=${subscriptionsPerPage}&offset=${pageOffset}`);
			const subscriptionJson = await subscriptionRes.json();
			
			setSubscriptions(subscriptionJson.data.results);
		} catch (err) {
			console.error("Error loading subscriptions:", err);
			setError("Failed to load subscriptions.");
		} finally {
			setLoading(false);
		}
	};

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=subscription`);
				const kpiJson = await kpiRes.json();
				setKpiData(kpiJson.data.results);
				setTotalSubscriptions(kpiJson.data.results.totalSubscription);
			} catch (err) {
				console.error("Error loading KPI data:", err);
				setError("Failed to load KPI data.");
			}
		};

		fetchData();
	}, [baseUrl]);

	React.useEffect(() => {
		fetchSubscriptions(currentPage);
	}, [baseUrl, currentPage, subscriptionsPerPage]);

	const handlePageChange = (page) => {
		if (page >= 1 && page <= totalPages) {
			setCurrentPage(page);
		}
	};

	const handleCancelClick = (subscription) => {
		setCancelSubscription(subscription);
		setCancelReason("");
	};

	const confirmCancelSubscription = async () => {
		if (!cancelReason.trim()) {
			toastr.error("Please provide a reason for cancellation");
			return;
		}

		try {
			const res = await fetch(`${baseUrl}/api/subscription/${cancelSubscription.subscriptionId}`, {
				method: "PUT",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify({ 
					message: cancelReason.trim()
				})
			});
			
			if (!res.ok) throw new Error("Cancel failed");
			toastr.success("Subscription cancelled and user notified");
			
			// Refresh the current page
			await fetchSubscriptions(currentPage);
			setCancelSubscription(null);
			setCancelReason("");
		} catch (err) {
			console.error("Error cancelling subscription:", err);
			toastr.error("Failed to cancel subscription");
		}
	};

	const getSubscriptionStatus = (subscription) => {
		return subscription.endDate ? "cancelled" : "active";
	};

	const formatDate = (dateString) => {
		if (!dateString) return "N/A";
		return new Date(dateString).toLocaleDateString();
	};

	const formatCurrency = (amount) => {
		return new Intl.NumberFormat('ms-MY', {
			style: 'currency',
			currency: 'MYR'
		}).format(amount);
	};

	const renderPagination = () => {
		if (totalPages <= 1) return null;

		const pages = [];
		const maxVisiblePages = 5;
		
		let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
		let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
		
		if (endPage - startPage + 1 < maxVisiblePages) {
			startPage = Math.max(1, endPage - maxVisiblePages + 1);
		}

		// Previous button
		pages.push(
			<li key="prev" className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
				<button 
					className="page-link" 
					onClick={() => handlePageChange(currentPage - 1)}
					disabled={currentPage === 1}
				>
					Previous
				</button>
			</li>
		);

		// First page + ellipsis
		if (startPage > 1) {
			pages.push(
				<li key={1} className="page-item">
					<button className="page-link" onClick={() => handlePageChange(1)}>1</button>
				</li>
			);
			if (startPage > 2) {
				pages.push(
					<li key="ellipsis1" className="page-item disabled">
						<span className="page-link">...</span>
					</li>
				);
			}
		}

		// Page numbers
		for (let i = startPage; i <= endPage; i++) {
			pages.push(
				<li key={i} className={`page-item ${currentPage === i ? 'active' : ''}`}>
					<button className="page-link" onClick={() => handlePageChange(i)}>
						{i}
					</button>
				</li>
			);
		}

		// Last page + ellipsis
		if (endPage < totalPages) {
			if (endPage < totalPages - 1) {
				pages.push(
					<li key="ellipsis2" className="page-item disabled">
						<span className="page-link">...</span>
					</li>
				);
			}
			pages.push(
				<li key={totalPages} className="page-item">
					<button className="page-link" onClick={() => handlePageChange(totalPages)}>
						{totalPages}
					</button>
				</li>
			);
		}

		// Next button
		pages.push(
			<li key="next" className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
				<button 
					className="page-link" 
					onClick={() => handlePageChange(currentPage + 1)}
					disabled={currentPage === totalPages}
				>
					Next
				</button>
			</li>
		);

		return (
			<nav aria-label="Subscription pagination">
				<ul className="pagination justify-content-center mb-0">
					{pages}
				</ul>
			</nav>
		);
	};

	if (loading) {
		return (
			<div className="text-center my-5">
				<div className="spinner-border text-primary" role="status">
					<span className="visually-hidden">Loading...</span>
				</div>
			</div>
		);
	}

	if (error) {
		return <div className="alert alert-danger">{error}</div>;
	}

	return (
		<div className="container-xl mt-5">
			<h2 className="mb-4">Subscription Management</h2>

			{kpiData && (
				<div className="row mb-4">
					<KpiCard title="Total Subscriptions" value={kpiData.totalSubscription} />
					<KpiCard title="Active Subscriptions" value={kpiData.activeSubscription} />
					<KpiCard title="Cancelled Subscriptions" value={kpiData.cancelledSubscription} />
					<KpiCard title="Total Revenue" value={formatCurrency(kpiData.totalRevenue)} />
				</div>
			)}

			<div className="card shadow-sm">
				<div className="card-body">
					<div className="d-flex justify-content-between align-items-center mb-3">
						<h5 className="card-title mb-0">Subscriptions</h5>
						<div className="d-flex align-items-center gap-3">
							<small className="text-muted">
								Showing {offset + 1}-{Math.min(offset + subscriptionsPerPage, totalSubscriptions)} of {totalSubscriptions} subscriptions
							</small>
						</div>
					</div>
					
					<div className="table-responsive">
						<table className="table table-hover">
							<thead>
								<tr>
									<th>User</th>
									<th>Plan</th>
									<th>Status</th>
									<th>Duration</th>
									<th>Amount</th>
									<th>Gateway Ref</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{subscriptions.map((subscription) => (
									<tr key={subscription.subscriptionId}>
										<td className="d-flex align-items-center">
											<img
												src={`${baseUrl}/stream/image/user/${subscription.user.userId}?size=thumb`}
												alt="User"
												style={{
													width: "32px",
													height: "32px",
													objectFit: "cover",
													borderRadius: "50%",
													marginRight: "10px"
												}}
											/>
											<div>
												<div className="fw-medium">{subscription.user.username}</div>
												<small className="text-muted">{subscription.user.email}</small>
											</div>
										</td>
										<td>
											<div className="fw-medium">{subscription.subscriptionPlan.name}</div>
											<small className="text-muted">
												{formatCurrency(subscription.subscriptionPlan.price)}/{subscription.subscriptionPlan.billingCycle}
											</small>
										</td>
										<td>
											<span className={`badge ${getSubscriptionStatus(subscription) === 'active' ? 'bg-success' : 'bg-secondary'}`}>
												{getSubscriptionStatus(subscription)}
											</span>
										</td>
										<td>
											<div>{formatDate(subscription.startDate)}</div>
											<small className="text-muted">to {formatDate(subscription.endDate) || "Active"}</small>
										</td>
										<td>{formatCurrency(subscription.amountPaid)}</td>
										<td>
											<small className="text-muted font-monospace">
												{subscription.paymentGatewayRef ? 
													subscription.paymentGatewayRef.substring(0, 12) + "..." : 
													"N/A"
												}
											</small>
										</td>
										<td>
											{getSubscriptionStatus(subscription) === 'active' && (
												<button
													className="btn btn-sm btn-danger"
													onClick={() => handleCancelClick(subscription)}
												>
													Cancel
												</button>
											)}
										</td>
									</tr>
								))}
							</tbody>
						</table>
					</div>

					{/* Pagination */}
					<div className="d-flex justify-content-center mt-3">
						{renderPagination()}
					</div>
				</div>
			</div>

			{/* Cancel Subscription Modal */}
			{cancelSubscription && (
				<div className="modal show d-block" tabIndex="-1">
					<div className="modal-dialog">
						<div className="modal-content">
							<div className="modal-header">
								<h5 className="modal-title">Cancel Subscription</h5>
								<button type="button" className="btn-close" onClick={() => setCancelSubscription(null)}></button>
							</div>
							<div className="modal-body">
								<div className="alert alert-warning">
									<strong>Warning:</strong> This action will cancel the subscription immediately and notify the user via email.
								</div>
								
								<div className="mb-3">
									<strong>User:</strong> {cancelSubscription.user.username} ({cancelSubscription.user.email})
								</div>
								<div className="mb-3">
									<strong>Plan:</strong> {cancelSubscription.subscriptionPlan.name} - {formatCurrency(cancelSubscription.amountPaid)}
								</div>
								<div className="mb-3">
									<strong>Gateway Reference:</strong> {cancelSubscription.paymentGatewayRef || "N/A"}
								</div>
								
								<div className="mb-3">
									<label htmlFor="cancelReason" className="form-label">
										<strong>Reason for cancellation *</strong>
									</label>
									<textarea
										id="cancelReason"
										className="form-control"
										rows="4"
										placeholder="Please provide a reason for cancelling this subscription..."
										value={cancelReason}
										onChange={(e) => setCancelReason(e.target.value)}
									/>
								</div>
							</div>
							<div className="modal-footer">
								<button className="btn btn-secondary" onClick={() => setCancelSubscription(null)}>
									Cancel
								</button>
								<button 
									className="btn btn-danger" 
									onClick={confirmCancelSubscription}
									disabled={!cancelReason.trim()}
								>
									Yes, Cancel Subscription
								</button>
							</div>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};