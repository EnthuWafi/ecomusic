import KpiCard from "./KpiCard.js";
import UserEditModal from "./UserEditModal.js";
import UserCreateModal from "./UserCreateModal.js";

export const AdminUserList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [users, setUsers] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	// Pagination state
	const [currentPage, setCurrentPage] = React.useState(1);
	const [totalUsers, setTotalUsers] = React.useState(0);
	const [usersPerPage] = React.useState(10); // You can make this configurable

	const [editUser, setEditUser] = React.useState(null);
	const [pendingRoleChange, setPendingRoleChange] = React.useState(null);
	const [showAddModal, setShowAddModal] = React.useState(false);

	// Calculate pagination values
	const totalPages = Math.ceil(totalUsers / usersPerPage);
	const offset = (currentPage - 1) * usersPerPage;

	const fetchUsers = async (page = 1) => {
		try {
			setLoading(true);
			const pageOffset = (page - 1) * usersPerPage;
			const userRes = await fetch(`${baseUrl}/api/user?limit=${usersPerPage}&offset=${pageOffset}`);
			const userJson = await userRes.json();
			
			setUsers(userJson.data.results);
		} catch (err) {
			console.error("Error loading users:", err);
			setError("Failed to load users.");
		} finally {
			setLoading(false);
		}
	};

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=user`);
				const kpiJson = await kpiRes.json();
				setKpiData(kpiJson.data.results);
				setTotalUsers(kpiJson.data.results.totalUserCount);
			} catch (err) {
				console.error("Error loading KPI data:", err);
				setError("Failed to load KPI data.");
			}
		};

		fetchData();
	}, [baseUrl]);

	React.useEffect(() => {
		fetchUsers(currentPage);
	}, [baseUrl, currentPage, usersPerPage]);

	const handlePageChange = (page) => {
		if (page >= 1 && page <= totalPages) {
			setCurrentPage(page);
		}
	};

	const handleDelete = async (userId) => {
		if (!confirm("Are you sure you want to delete this user?")) return;
		try {
			const res = await fetch(`${baseUrl}/api/user/${userId}`, {
				method: "DELETE"
			});
			if (!res.ok) throw new Error("Delete failed");
			
			// Refresh the current page after deletion
			await fetchUsers(currentPage);
			toastr.success("User deleted.");
		} catch (err) {
			console.error("Error deleting user:", err);
			toastr.error("Failed to delete user.");
		}
	};

	const handleRoleChange = (userId, newRole) => {
		setPendingRoleChange({ userId, newRole });
	};

	const confirmRoleChange = async () => {
		const { userId, newRole } = pendingRoleChange;
		try {
			const res = await fetch(`${baseUrl}/api/user/${userId}/role`, {
				method: "PUT",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify({ roleType: newRole })
			});
			if (!res.ok) throw new Error("Failed to update role");
			toastr.success("Role updated successfully");

			setUsers(users.map(u =>
				u.userId === userId ? { ...u, roleName: newRole } : u
			));
		} catch (err) {
			console.error("Role change failed:", err);
			toastr.error("Role change failed.");
		}
		setPendingRoleChange(null);
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
			<nav aria-label="User pagination">
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
			<h2 className="mb-4">User Management</h2>

			{kpiData && (
				<div className="row mb-4">
					<KpiCard title="Total Users" value={kpiData.totalUserCount} />
					<KpiCard title="Admin Users" value={kpiData.adminCount} />
					<KpiCard title="Superadmin Users" value={kpiData.superAdminCount} />
					<KpiCard title="Artist Users" value={kpiData.artistCount} />
					<KpiCard title="Premium Users" value={kpiData.premiumCount} />
					<KpiCard title="Regular Users" value={kpiData.userCount} />
				</div>
			)}

			<div className="card shadow-sm">
				<div className="card-body">
					<div className="d-flex justify-content-between align-items-center mb-3">
						<h5 className="card-title mb-0">Users</h5>
						<div className="d-flex align-items-center gap-3">
							<small className="text-muted">
								Showing {offset + 1}-{Math.min(offset + usersPerPage, totalUsers)} of {totalUsers} users
							</small>
							<button className="btn btn-success" onClick={() => setShowAddModal(true)}>
								<i className="bi bi-person-plus"></i> Add Admin
							</button>
						</div>
					</div>
					
					<div className="table-responsive">
						<table className="table table-hover">
							<thead>
								<tr>
									<th>Username</th>
									<th>Email</th>
									<th>Role</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{users.map((user) => (
									<tr key={user.userId}>
										<td className="d-flex align-items-center">
											<img
												src={`${baseUrl}/stream/image/user/${user.userId}`}
												alt="User"
												style={{
													width: "32px",
													height: "32px",
													objectFit: "cover",
													borderRadius: "50%",
													marginRight: "10px"
												}}
											/>
											<span>{user.username}</span>
										</td>
										<td>{user.email}</td>
										<td>
											<select
												className="form-select form-select-sm"
												value={user.roleName}
												onChange={(e) => handleRoleChange(user.userId, e.target.value)}
											>
												<option value="user">User</option>
												<option value="admin">Admin</option>
												<option value="superadmin">Superadmin</option>
											</select>
										</td>
										<td>
											<button
												className="btn btn-sm btn-primary me-2"
												onClick={() => setEditUser({ ...user })}
											>
												Edit
											</button>
											<button
												className="btn btn-sm btn-danger"
												onClick={() => handleDelete(user.userId)}
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

			{pendingRoleChange && (
				<div className="modal show d-block" tabIndex="-1">
					<div className="modal-dialog">
						<div className="modal-content">
							<div className="modal-header">
								<h5 className="modal-title">Confirm Role Change</h5>
								<button type="button" className="btn-close" onClick={() => setPendingRoleChange(null)}></button>
							</div>
							<div className="modal-body">
								<p>
									Are you sure you want to change this user's role to <strong>{pendingRoleChange.newRole}</strong>?
								</p>
							</div>
							<div className="modal-footer">
								<button className="btn btn-secondary" onClick={() => setPendingRoleChange(null)}>Cancel</button>
								<button className="btn btn-primary" onClick={confirmRoleChange}>Yes, Change</button>
							</div>
						</div>
					</div>
				</div>
			)}

			{showAddModal && (
				<UserCreateModal
					onClose={() => setShowAddModal(false)}
					onCreated={() => {
						setShowAddModal(false);
						fetchUsers(currentPage); // Refresh current page instead of reloading
					}}
					baseUrl={baseUrl}
				/>
			)}

			{editUser && (
				<UserEditModal
					user={editUser}
					onClose={() => setEditUser(null)}
					onSave={() => {
						setEditUser(null);
						fetchUsers(currentPage); // Refresh current page instead of reloading
					}}
					baseUrl={baseUrl}
				/>
			)}
		</div>
	);
};