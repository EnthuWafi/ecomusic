import KpiCard from "./KpiCard.js";
import UserEditModal from "./UserEditModal.js";
import UserCreateModal from "./UserCreateModal.js";


export const AdminUserList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [users, setUsers] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	const [editUser, setEditUser] = React.useState(null);
	const [pendingRoleChange, setPendingRoleChange] = React.useState(null); 
	const [showAddModal, setShowAddModal] = React.useState(false);

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const [kpiRes, userRes] = await Promise.all([
					fetch(`${baseUrl}/api/report/kpis?type=user`),
					fetch(`${baseUrl}/api/user?limit=5&offset=0`)
				]);

				const kpiJson = await kpiRes.json();
				const userJson = await userRes.json();

				setKpiData(kpiJson.data.results);
				setUsers(userJson.data.results);
			} catch (err) {
				console.error("Error loading user dashboard:", err);
				setError("Failed to load data.");
			} finally {
				setLoading(false);
			}
		};

		fetchData();
	}, [baseUrl]);

	const handleDelete = async (userId) => {
		if (!confirm("Are you sure you want to delete this user?")) return;
		try {
			const res = await fetch(`${baseUrl}/api/user/${userId}`, {
				method: "DELETE"
			});
			if (!res.ok) throw new Error("Delete failed");
			setUsers(users.filter((user) => user.userId !== userId));
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
		<div className="container mt-5">
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
					<h5 className="card-title">Users</h5>
					<div className="mb-3 text-end">
						<button className="btn btn-success" onClick={() => setShowAddModal(true)}>
							<i className="bi bi-person-plus"></i> Add Admin
						</button>
					</div>
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
									<td>{user.username}</td>
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
			      location.reload();
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
						location.reload();
					}}
					baseUrl={baseUrl}
				/>
			)}
		</div>
	);
};
