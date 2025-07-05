import KpiCard from "./KpiCard.js";

export const AdminSubscriptionPlanList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [plans, setPlans] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	// Pagination state
	const [currentPage, setCurrentPage] = React.useState(1);
	const [totalPlans, setTotalPlans] = React.useState(0);
	const [plansPerPage] = React.useState(10);

	// Modal states
	const [showCreateModal, setShowCreateModal] = React.useState(false);
	const [editPlan, setEditPlan] = React.useState(null);
	const [deletePlan, setDeletePlan] = React.useState(null);

	// Form state
	const [formData, setFormData] = React.useState({
		name: "",
		stripePriceId: "",
		billingCycle: "monthly",
		price: "",
		description: "",
		planType: "LISTENER",
		features: []
	});
	const [featureInput, setFeatureInput] = React.useState("");

	// Calculate pagination values
	const totalPages = Math.ceil(totalPlans / plansPerPage);
	const offset = (currentPage - 1) * plansPerPage;

	const fetchPlans = async (page = 1) => {
		try {
			setLoading(true);
			const pageOffset = (page - 1) * plansPerPage;
			const planRes = await fetch(`${baseUrl}/api/subscription-plan?limit=${plansPerPage}&offset=${pageOffset}`);
			const planJson = await planRes.json();
			
			setPlans(planJson.data.results);
			
		} catch (err) {
			console.error("Error loading plans:", err);
			setError("Failed to load subscription plans.");
		} finally {
			setLoading(false);
		}
	};

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=subscription-plan`);
				const kpiJson = await kpiRes.json();
				setKpiData(kpiJson.data.results);
				setTotalPlans(kpiJson.data.results.totalPlan);
			} catch (err) {
				console.error("Error loading KPI data:", err);
				setError("Failed to load KPI data.");
			}
		};

		fetchData();
	}, [baseUrl]);

	React.useEffect(() => {
		fetchPlans(currentPage);
	}, [baseUrl, currentPage, plansPerPage]);

	const handlePageChange = (page) => {
		if (page >= 1 && page <= totalPages) {
			setCurrentPage(page);
		}
	};

	const resetForm = () => {
		setFormData({
			name: "",
			stripePriceId: "",
			billingCycle: "monthly",
			price: "",
			description: "",
			planType: "LISTENER",
			features: []
		});
		setFeatureInput("");
	};

	const handleCreateClick = () => {
		resetForm();
		setShowCreateModal(true);
	};

	const handleEditClick = (plan) => {
		setFormData({
			name: plan.name,
			stripePriceId: plan.stripePriceId,
			billingCycle: plan.billingCycle,
			price: plan.price.toString(),
			description: plan.description,
			planType: plan.planType,
			features: [...plan.features]
		});
		setEditPlan(plan);
	};

	const handleDeleteClick = (plan) => {
		setDeletePlan(plan);
	};

	const handleFormSubmit = async (e) => {
		e.preventDefault();
		
		if (!formData.name.trim() || !formData.price) {
			toastr.error("Please fill in all required fields");
			return;
		}

		try {
			const payload = {
				...formData,
				price: parseFloat(formData.price),
				features: formData.features.filter(f => f.trim())
			};

			const url = editPlan 
				? `${baseUrl}/api/subscription-plan/${editPlan.subscriptionPlanId}`
				: `${baseUrl}/api/subscription-plan`;
			
			const method = editPlan ? "PUT" : "POST";

			const res = await fetch(url, {
				method,
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(payload)
			});

			if (!res.ok) throw new Error("Save failed");
			
			toastr.success(`Subscription plan ${editPlan ? 'updated' : 'created'} successfully`);
			
			// Close modals and refresh
			setShowCreateModal(false);
			setEditPlan(null);
			resetForm();
			await fetchPlans(currentPage);
		} catch (err) {
			console.error("Error saving plan:", err);
			toastr.error("Failed to save subscription plan");
		}
	};

	const confirmDelete = async () => {
		try {
			const res = await fetch(`${baseUrl}/api/subscription-plan/${deletePlan.subscriptionPlanId}`, {
				method: "DELETE"
			});
			
			if (!res.ok) throw new Error("Delete failed");
			
			toastr.success("Subscription plan deleted successfully");
			setDeletePlan(null);
			await fetchPlans(currentPage);
		} catch (err) {
			console.error("Error deleting plan:", err);
			toastr.error("Failed to delete subscription plan");
		}
	};

	const addFeature = () => {
		if (featureInput.trim() && !formData.features.includes(featureInput.trim())) {
			setFormData(prev => ({
				...prev,
				features: [...prev.features, featureInput.trim()]
			}));
			setFeatureInput("");
		}
	};

	const removeFeature = (index) => {
		setFormData(prev => ({
			...prev,
			features: prev.features.filter((_, i) => i !== index)
		}));
	};

	const formatCurrency = (amount) => {
		return new Intl.NumberFormat('ms-MY', {
			style: 'currency',
			currency: 'MYR'
		}).format(amount);
	};

	const formatDate = (dateString) => {
		return new Date(dateString).toLocaleDateString();
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
			<nav aria-label="Subscription plan pagination">
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
			<h2 className="mb-4">Subscription Plan Management</h2>

			{kpiData && (
				<div className="row mb-4">
					<KpiCard title="Total Plans" value={kpiData.totalPlan} />
					<KpiCard title="Creator Plans" value={kpiData.creatorPlan} />
					<KpiCard title="Listener Plans" value={kpiData.listenerPlan} />
				</div>
			)}

			<div className="card shadow-sm">
				<div className="card-body">
					<div className="d-flex justify-content-between align-items-center mb-3">
						<h5 className="card-title mb-0">Subscription Plans</h5>
						<div className="d-flex align-items-center gap-3">
							<small className="text-muted">
								Showing {offset + 1}-{Math.min(offset + plansPerPage, totalPlans)} of {totalPlans} plans
							</small>
							<button className="btn btn-success" onClick={handleCreateClick}>
								<i className="bi bi-plus-circle"></i> Create Plan
							</button>
						</div>
					</div>
					
					<div className="table-responsive">
						<table className="table table-hover">
							<thead>
								<tr>
									<th>Plan Name</th>
									<th>Price</th>
									<th>Billing Cycle</th>
									<th>Type</th>
									<th>Stripe ID</th>
									<th>Features</th>
									<th>Created</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{plans.map((plan) => (
									<tr key={plan.subscriptionPlanId}>
										<td>
											<div className="fw-medium">{plan.name}</div>
											<small className="text-muted">{plan.description}</small>
										</td>
										<td>{formatCurrency(plan.price)}</td>
										<td>
											<span className="badge bg-info">
												{plan.billingCycle}
											</span>
										</td>
										<td>
											<span className={`badge ${plan.planType === 'LISTENER' ? 'bg-warning' : 'bg-secondary'}`}>
												{plan.planType}
											</span>
										</td>
										<td>
											<small className="text-muted font-monospace">
												{plan.stripePriceId}
											</small>
										</td>
										<td>
											<small className="text-muted">
												{plan.features.length} features
											</small>
										</td>
										<td>
											<small className="text-muted">
												{formatDate(plan.createdAt)}
											</small>
										</td>
										<td>
											<button
												className="btn btn-sm btn-primary me-2"
												onClick={() => handleEditClick(plan)}
											>
												Edit
											</button>
											<button
												className="btn btn-sm btn-danger"
												onClick={() => handleDeleteClick(plan)}
											>
												Delete
											</button>
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

			{/* Create/Edit Modal */}
			{(showCreateModal || editPlan) && (
				<div className="modal show d-block" tabIndex="-1">
					<div className="modal-dialog modal-lg">
						<div className="modal-content">
							<div className="modal-header">
								<h5 className="modal-title">
									{editPlan ? 'Edit Subscription Plan' : 'Create Subscription Plan'}
								</h5>
								<button type="button" className="btn-close" onClick={() => {
									setShowCreateModal(false);
									setEditPlan(null);
									resetForm();
								}}></button>
							</div>
							<form onSubmit={handleFormSubmit}>
								<div className="modal-body">
									<div className="row">
										<div className="col-md-6">
											<div className="mb-3">
												<label htmlFor="planName" className="form-label">Plan Name *</label>
												<input
													type="text"
													id="planName"
													className="form-control"
													value={formData.name}
													onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
													required
												/>
											</div>
										</div>
										<div className="col-md-6">
											<div className="mb-3">
												<label htmlFor="stripePriceId" className="form-label">Stripe Price ID *</label>
												<input
													type="text"
													id="stripePriceId"
													readOnly
													className="form-control"
													value={formData.stripePriceId}
													onChange={(e) => setFormData(prev => ({ ...prev, stripePriceId: e.target.value }))}
												/>
											</div>
										</div>
									</div>

									<div className="row">
										<div className="col-md-4">
											<div className="mb-3">
												<label htmlFor="price" className="form-label">Price *</label>
												<input
													type="number"
													id="price"
													className="form-control"
													step="0.01"
													value={formData.price}
													onChange={(e) => setFormData(prev => ({ ...prev, price: e.target.value }))}
													required
												/>
											</div>
										</div>
										<div className="col-md-4">
											<div className="mb-3">
												<label htmlFor="billingCycle" className="form-label">Billing Cycle</label>
												<select
													id="billingCycle"
													className="form-select"
													value={formData.billingCycle}
													onChange={(e) => setFormData(prev => ({ ...prev, billingCycle: e.target.value }))}
												>
													<option value="monthly">Monthly</option>
													<option value="yearly">Yearly</option>
												</select>
											</div>
										</div>
										<div className="col-md-4">
											<div className="mb-3">
												<label htmlFor="planType" className="form-label">Plan Type</label>
												<select
													id="planType"
													className="form-select"
													value={formData.planType}
													onChange={(e) => setFormData(prev => ({ ...prev, planType: e.target.value }))}
												>
													<option value="LISTENER">Listener</option>
													<option value="CREATOR">Creator</option>
												</select>
											</div>
										</div>
									</div>

									<div className="mb-3">
										<label htmlFor="description" className="form-label">Description</label>
										<textarea
											id="description"
											className="form-control"
											rows="3"
											value={formData.description}
											onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
										/>
									</div>

									<div className="mb-3">
										<label className="form-label">Features</label>
										<div className="d-flex mb-2">
											<input
												type="text"
												className="form-control me-2"
												placeholder="Add a feature..."
												value={featureInput}
												onChange={(e) => setFeatureInput(e.target.value)}
												onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addFeature())}
											/>
											<button type="button" className="btn btn-outline-primary" onClick={addFeature}>
												Add
											</button>
										</div>
										<div className="d-flex flex-wrap gap-2">
											{formData.features.map((feature, index) => (
												<span key={index} className="badge bg-secondary d-flex align-items-center">
													{feature}
													<button
														type="button"
														className="btn-close btn-close-white ms-1"
														onClick={() => removeFeature(index)}
														style={{ fontSize: '0.75em' }}
													></button>
												</span>
											))}
										</div>
									</div>
								</div>
								<div className="modal-footer">
									<button type="button" className="btn btn-secondary" onClick={() => {
										setShowCreateModal(false);
										setEditPlan(null);
										resetForm();
									}}>
										Cancel
									</button>
									<button type="submit" className="btn btn-primary">
										{editPlan ? 'Update Plan' : 'Create Plan'}
									</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			)}

			{/* Delete Confirmation Modal */}
			{deletePlan && (
				<div className="modal show d-block" tabIndex="-1">
					<div className="modal-dialog">
						<div className="modal-content">
							<div className="modal-header">
								<h5 className="modal-title">Delete Subscription Plan</h5>
								<button type="button" className="btn-close" onClick={() => setDeletePlan(null)}></button>
							</div>
							<div className="modal-body">
								<div className="alert alert-danger">
									<strong>Warning:</strong> This action cannot be undone. Deleting this plan may affect existing subscriptions.
								</div>
								<p>Are you sure you want to delete the plan <strong>"{deletePlan.name}"</strong>?</p>
								<div className="p-3 rounded">
									<small>
										<strong>Plan Details:</strong><br />
										Price: {formatCurrency(deletePlan.price)}<br />
										Billing: {deletePlan.billingCycle}<br />
										Stripe ID: {deletePlan.stripePriceId}<br />
										Features: {deletePlan.features.length} features
									</small>
								</div>
							</div>
							<div className="modal-footer">
								<button className="btn btn-secondary" onClick={() => setDeletePlan(null)}>
									Cancel
								</button>
								<button className="btn btn-danger" onClick={confirmDelete}>
									Yes, Delete Plan
								</button>
							</div>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};