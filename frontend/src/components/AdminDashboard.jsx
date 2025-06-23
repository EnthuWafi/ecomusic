const AdminDashboard = () => {
	const [kpiData, setKpiData] = React.useState({
		totalUsers: 15420,
		activeSubscriptions: 8934,
		totalTracks: 47892,
		monthlyRevenue: 89750,
		yearlyRevenue: 967340,
		todaySignups: 127,
		todayUploads: 89
	});

	const [recentActivity] = React.useState([
		{ id: 1, artist: "Alex Chen", track: "Midnight Dreams", time: "2 minutes ago", avatar: "AC" },
		{ id: 2, artist: "Sarah Johnson", track: "Ocean Waves", time: "8 minutes ago", avatar: "SJ" },
		{ id: 3, artist: "Mike Rodriguez", track: "City Lights", time: "15 minutes ago", avatar: "MR" },
		{ id: 4, artist: "Emma Thompson", track: "Forest Path", time: "23 minutes ago", avatar: "ET" },
		{ id: 5, artist: "David Kim", track: "Neon Nights", time: "31 minutes ago", avatar: "DK" },
		{ id: 6, artist: "Lisa Wang", track: "Mountain Echo", time: "45 minutes ago", avatar: "LW" },
		{ id: 7, artist: "James Brown", track: "Summer Breeze", time: "1 hour ago", avatar: "JB" },
		{ id: 8, artist: "Anna Garcia", track: "Digital Soul", time: "1 hour ago", avatar: "AG" }
	]);

	const userGrowthRef = React.useRef(null);
	const uploadsRef = React.useRef(null);
	const userGrowthChart = React.useRef(null);
	const uploadsChart = React.useRef(null);

	React.useEffect(() => {
		// User Growth Chart
		if (userGrowthRef.current) {
			const ctx = userGrowthRef.current.getContext('2d');

			if (userGrowthChart.current) {
				userGrowthChart.current.destroy();
			}

			userGrowthChart.current = new Chart(ctx, {
				type: 'line',
				data: {
					labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
					datasets: [{
						label: 'Total Users',
						data: [8500, 9200, 10100, 11800, 13500, 15420],
						borderColor: '#667eea',
						backgroundColor: 'rgba(102, 126, 234, 0.1)',
						borderWidth: 3,
						fill: true,
						tension: 0.4,
						pointBackgroundColor: '#667eea',
						pointBorderColor: '#fff',
						pointBorderWidth: 2,
						pointRadius: 6
					}]
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					plugins: {
						legend: {
							display: false
						}
					},
					scales: {
						y: {
							beginAtZero: false,
							grid: {
								color: 'rgba(0,0,0,0.1)'
							}
						},
						x: {
							grid: {
								display: false
							}
						}
					}
				}
			});
		}

		// Music Uploads Chart
		if (uploadsRef.current) {
			const ctx = uploadsRef.current.getContext('2d');

			if (uploadsChart.current) {
				uploadsChart.current.destroy();
			}

			uploadsChart.current = new Chart(ctx, {
				type: 'bar',
				data: {
					labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'This Week'],
					datasets: [{
						label: 'Music Uploads',
						data: [245, 312, 278, 389, 423],
						backgroundColor: 'rgba(118, 75, 162, 0.8)',
						borderColor: '#764ba2',
						borderWidth: 2,
						borderRadius: 8,
						borderSkipped: false
					}]
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					plugins: {
						legend: {
							display: false
						}
					},
					scales: {
						y: {
							beginAtZero: true,
							grid: {
								color: 'rgba(0,0,0,0.1)'
							}
						},
						x: {
							grid: {
								display: false
							}
						}
					}
				}
			});
		}

		return () => {
			if (userGrowthChart.current) {
				userGrowthChart.current.destroy();
			}
			if (uploadsChart.current) {
				uploadsChart.current.destroy();
			}
		};
	}, []);

	const formatCurrency = (amount) => {
		return new Intl.NumberFormat('en-US', {
			style: 'currency',
			currency: 'USD',
			minimumFractionDigits: 0
		}).format(amount);
	};

	const formatNumber = (num) => {
		return new Intl.NumberFormat('en-US').format(num);
	};

	return (
		<div>
			{/* Header */}
			<div className="dashboard-header">
				<div className="container">
					<div className="row align-items-center">
						<div className="col-md-8">
							<h1 className="mb-2">
								<i className="fas fa-music me-3"></i>
								Music Platform Dashboard
							</h1>
							<p className="mb-0 ms-2 opacity-75">Real-time analytics and insights</p>
						</div>
						<div className="col-md-4 text-end">
							<div className="d-flex align-items-center justify-content-end">
								<i className="fas fa-calendar-alt me-2"></i>
								<span>{new Date().toLocaleDateString('en-US', {
									weekday: 'long',
									year: 'numeric',
									month: 'long',
									day: 'numeric'
								})}</span>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div className="container">
				{/* KPI Cards */}
				<div className="row g-4 mb-4">
					<div className="col-xxl-3 col-md-6">
						<div className="card kpi-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: '#3498db' }}>
										<i className="fas fa-users"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value">{formatNumber(kpiData.totalUsers)}</div>
										<div className="kpi-label">Total Users</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div className="col-xxl-3 col-md-6">
						<div className="card kpi-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: '#e74c3c' }}>
										<i className="fas fa-crown"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value">{formatNumber(kpiData.activeSubscriptions)}</div>
										<div className="kpi-label">Active Subscriptions</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div className="col-xxl-3 col-md-6">
						<div className="card kpi-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: '#9b59b6' }}>
										<i className="fas fa-music"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value">{formatNumber(kpiData.totalTracks)}</div>
										<div className="kpi-label">Total Music Tracks</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div className="col-xxl-3 col-md-6">
						<div className="card kpi-card revenue-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: 'rgba(255,255,255,0.2)' }}>
										<i className="fas fa-dollar-sign"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value text-white">{formatCurrency(kpiData.monthlyRevenue)}</div>
										<div className="kpi-label text-white opacity-75">Monthly Revenue</div>
										<div className="yearly-revenue mt-2">
											<small className="text-white opacity-75">Yearly Revenue:</small>
											<div className="text-white fw-bold">{formatCurrency(kpiData.yearlyRevenue)}</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				{/* Today's Stats */}
				<div className="row g-4 mb-4">
					<div className="col-md-6">
						<div className="card kpi-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: '#f39c12' }}>
										<i className="fas fa-user-plus"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value">{formatNumber(kpiData.todaySignups)}</div>
										<div className="kpi-label">Today's New Signups</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div className="col-md-6">
						<div className="card kpi-card">
							<div className="card-body p-4">
								<div className="d-flex align-items-center">
									<div className="kpi-icon" style={{ backgroundColor: '#1abc9c' }}>
										<i className="fas fa-upload"></i>
									</div>
									<div className="ms-3 flex-grow-1">
										<div className="kpi-value">{formatNumber(kpiData.todayUploads)}</div>
										<div className="kpi-label">Today's Music Uploads</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				{/* Charts */}
				<div className="row g-4 mb-4">
					<div className="col-lg-8">
						<div className="chart-container">
							<h4 className="section-title">
								<i className="fas fa-chart-line me-2"></i>
								User Growth Over Time
							</h4>
							<div style={{ height: '300px' }}>
								<canvas ref={userGrowthRef}></canvas>
							</div>
						</div>
					</div>
					<div className="col-lg-4">
						<div className="chart-container">
							<h4 className="section-title">
								<i className="fas fa-chart-bar me-2"></i>
								Weekly Music Uploads
							</h4>
							<div style={{ height: '300px' }}>
								<canvas ref={uploadsRef}></canvas>
							</div>
						</div>
					</div>
				</div>

				{/* Recent Activity */}
				<div className="row">
					<div className="col-12">
						<div className="chart-container">
							<h4 className="section-title">
								<i className="fas fa-clock me-2"></i>
								Recent Music Uploads
							</h4>
							<div className="row">
								{recentActivity.map((activity) => (
									<div key={activity.id} className="col-lg-6">
										<div className="activity-item">
											<div className="d-flex align-items-center">
												<div className="activity-avatar me-3">
													{activity.avatar}
												</div>
												<div className="flex-grow-1">
													<div className="fw-bold text-dark mb-1">
														{activity.track}
													</div>
													<div className="text-muted small">
														by {activity.artist}
													</div>
												</div>
												<div className="text-muted small">
													<i className="fas fa-clock me-1"></i>
													{activity.time}
												</div>
											</div>
										</div>
									</div>
								))}
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};