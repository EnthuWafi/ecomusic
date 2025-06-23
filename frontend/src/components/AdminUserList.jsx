const AdminUserList = () => {
	const [users, setUsers] = React.useState([
		{
			id: 1,
			name: "Sarah Johnson",
			email: "sarah.johnson@email.com",
			role: "admin",
			status: "active",
			joinDate: "2024-01-15",
			lastLogin: "2025-06-22",
			tracksUploaded: 23,
			avatar: "SJ"
		},
		{
			id: 2,
			name: "Mike Rodriguez",
			email: "mike.rodriguez@email.com",
			role: "artist",
			status: "active",
			joinDate: "2024-02-20",
			lastLogin: "2025-06-21",
			tracksUploaded: 45,
			avatar: "MR"
		},
		{
			id: 3,
			name: "Emma Thompson",
			email: "emma.thompson@email.com",
			role: "user",
			status: "active",
			joinDate: "2024-03-10",
			lastLogin: "2025-06-20",
			tracksUploaded: 12,
			avatar: "ET"
		},
		{
			id: 4,
			name: "Alex Chen",
			email: "alex.chen@email.com",
			role: "artist",
			status: "inactive",
			joinDate: "2024-01-25",
			lastLogin: "2025-05-15",
			tracksUploaded: 89,
			avatar: "AC"
		},
		{
			id: 5,
			name: "David Kim",
			email: "david.kim@email.com",
			role: "premium",
			status: "active",
			joinDate: "2024-04-05",
			lastLogin: "2025-06-22",
			tracksUploaded: 67,
			avatar: "DK"
		},
		{
			id: 6,
			name: "Lisa Wang",
			email: "lisa.wang@email.com",
			role: "artist",
			status: "pending",
			joinDate: "2025-06-20",
			lastLogin: "Never",
			tracksUploaded: 0,
			avatar: "LW"
		},
		{
			id: 7,
			name: "James Brown",
			email: "james.brown@email.com",
			role: "admin",
			status: "active",
			joinDate: "2023-12-01",
			lastLogin: "2025-06-22",
			tracksUploaded: 156,
			avatar: "JB"
		},
		{
			id: 8,
			name: "Anna Garcia",
			email: "anna.garcia@email.com",
			role: "artist",
			status: "active",
			joinDate: "2024-05-12",
			lastLogin: "2025-06-19",
			tracksUploaded: 78,
			avatar: "AG"
		},
		{
			id: 9,
			name: "Tom Wilson",
			email: "tom.wilson@email.com",
			role: "user",
			status: "active",
			joinDate: "2024-06-01",
			lastLogin: "2025-06-21",
			tracksUploaded: 5,
			avatar: "TW"
		},
		{
			id: 10,
			name: "Maya Patel",
			email: "maya.patel@email.com",
			role: "artist",
			status: "active",
			joinDate: "2024-03-15",
			lastLogin: "2025-06-22",
			tracksUploaded: 134,
			avatar: "MP"
		}
	]);

	const [searchTerm, setSearchTerm] = React.useState("");
	const [showAddModal, setShowAddModal] = React.useState(false);

	const filteredUsers = users.filter(user =>
		user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
		user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
		user.role.toLowerCase().includes(searchTerm.toLowerCase())
	);

	const getAvatarColor = (name) => {
		const colors = ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7'];
		const index = name.charCodeAt(0) % colors.length;
		return colors[index];
	};

	const getStatusBadge = (status) => {
		const statusClasses = {
			active: 'status-badge status-active',
			inactive: 'status-badge status-inactive',
			pending: 'status-badge status-pending'
		};
		return statusClasses[status] || 'status-badge';
	};

	const getRoleBadge = (role) => {
		const roleClasses = {
			admin: 'role-badge role-admin',
			user: 'role-badge role-user',
			artist: 'role-badge role-artist',
			premium: 'role-badge role-premium'
		};
		return roleClasses[role] || 'role-badge';
	};

	const handleDeleteUser = (userId) => {
		if (window.confirm('Are you sure you want to delete this user?')) {
			setUsers(users.filter(user => user.id !== userId));
		}
	};

	const handleToggleStatus = (userId) => {
		setUsers(users.map(user =>
			user.id === userId
				? { ...user, status: user.status === 'active' ? 'inactive' : 'active' }
				: user
		));
	};

	const stats = {
		total: users.length,
		active: users.filter(u => u.status === 'active').length,
		artist: users.filter(u => u.role === 'artist').length,
		premium: users.filter(u => u.role === 'premium').length,
		admin: users.filter(u => u.role === 'admin').length
	};

	return (
		<div>
			<div className="page-header">
				<div className="container">
					<div className="row align-items-center">
						<div className="col-md-8">
							<h1 className="mb-2">
								<i className="bi bi-people-fill me-3"></i>
								User Management
							</h1>
							<p className="mb-0 opacity-75">Manage users and administrators</p>
						</div>
						<div className="col-md-4 text-end">
							<button className="btn add-admin-btn" onClick={() => setShowAddModal(true)}>
								<i className="bi bi-person-plus-fill me-2"></i>
								Add Admin
							</button>
						</div>
					</div>
				</div>
			</div>

			<div className="container">
				{/* Stats Row */}
				<div className="row stats-row g-3">
					<div className="col-lg-3 col-md-6">
						<div className="stat-card total">
							<div className="stat-number">{stats.total}</div>
							<div className="stat-label">Total Users</div>
						</div>
					</div>
					<div className="col-lg-3 col-md-6">
						<div className="stat-card active">
							<div className="stat-number">{stats.active}</div>
							<div className="stat-label">Active Users</div>
						</div>
					</div>
					<div className="col-lg-3 col-md-6">
						<div className="stat-card artist">
							<div className="stat-number">{stats.artist}</div>
							<div className="stat-label">Artists</div>
						</div>
					</div>
					<div className="col-lg-3 col-md-6">
						<div className="stat-card premium">
							<div className="stat-number">{stats.premium}</div>
							<div className="stat-label">Premium Users</div>
						</div>
					</div>
					<div className="col-lg-3 col-md-6">
						<div className="stat-card admin">
							<div className="stat-number">{stats.admin}</div>
							<div className="stat-label">Administrators</div>
						</div>
					</div>
				</div>

				{/* Search */}
				<div className="search-container">
					<div className="row align-items-center">
						<div className="col-md-8">
							<div className="input-group">
								<span className="input-group-text bg-white border-end-0">
									<i className="bi bi-search text-muted"></i>
								</span>
								<input
									type="text"
									className="form-control search-input border-start-0"
									placeholder="Search users and artists by name, email, or role..."
									value={searchTerm}
									onChange={(e) => setSearchTerm(e.target.value)}
								/>
							</div>
						</div>
						<div className="col-md-4 text-end">
							<span className="text-muted">
								Showing {filteredUsers.length} of {users.length} users & artists
							</span>
						</div>
					</div>
				</div>

				{/* User Table */}
				<div className="user-table-container">
					<div className="table-responsive">
						<table className="table">
							<thead>
								<tr>
									<th>User/Artist</th>
									<th>Role</th>
									<th>Status</th>
									<th>Join Date</th>
									<th>Last Login</th>
									<th>Tracks</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{filteredUsers.map((user) => (
									<tr key={user.id}>
										<td>
											<div className="d-flex align-items-center">
												<div
													className="user-avatar me-3"
													style={{ backgroundColor: getAvatarColor(user.name) }}
												>
													{user.avatar}
												</div>
												<div className="user-info">
													<h6>{user.name}</h6>
													<div className="user-email">{user.email}</div>
												</div>
											</div>
										</td>
										<td>
											<span className={getRoleBadge(user.role)}>
												{user.role}
											</span>
										</td>
										<td>
											<span className={getStatusBadge(user.status)}>
												{user.status}
											</span>
										</td>
										<td>{new Date(user.joinDate).toLocaleDateString()}</td>
										<td>{user.lastLogin === 'Never' ? 'Never' : new Date(user.lastLogin).toLocaleDateString()}</td>
										<td>
											<span className="fw-bold">{user.tracksUploaded}</span>
										</td>
										<td>
											<div className="d-flex">
												<button
													className="action-btn text-primary"
													title="Edit User"
												>
													<i className="bi bi-pencil-square"></i>
												</button>
												<button
													className="action-btn text-warning"
													title="Toggle Status"
													onClick={() => handleToggleStatus(user.id)}
												>
													<i className={user.status === 'active' ? 'bi bi-pause-circle' : 'bi bi-play-circle'}></i>
												</button>
												<button
													className="action-btn text-danger"
													title="Delete User"
													onClick={() => handleDeleteUser(user.id)}
												>
													<i className="bi bi-trash3"></i>
												</button>
											</div>
										</td>
									</tr>
								))}
							</tbody>
						</table>
					</div>
				</div>

				{filteredUsers.length === 0 && (
					<div className="text-center py-5">
						<i className="bi bi-search display-4 text-muted mb-3"></i>
						<h5 className="text-muted">No users or artists found</h5>
						<p className="text-muted">Try adjusting your search criteria</p>
					</div>
				)}
			</div>
		</div>
	);
};