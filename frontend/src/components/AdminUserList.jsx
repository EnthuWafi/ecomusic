import KpiCard from "./KpiCard.js";

export const AdminUserList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [users, setUsers] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const [kpiRes, userRes] = await Promise.all([
					fetch(`${baseUrl}/api/report/kpis?type=user`),
					fetch(`${baseUrl}/api/user?limit=5&offset=0`),
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
				method: "DELETE",
			});
			if (!res.ok) throw new Error("Delete failed");
			setUsers(users.filter((user) => user.id !== userId));
		} catch (err) {
			console.error("Error deleting user:", err);
			toastr.error("Failed to delete user.");
		}
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
					<table className="table table-hover">
						<thead>
							<tr>
								<th>Name</th>
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
									<td>{user.roleName}</td>
									<td>
										<button
											className="btn btn-sm btn-danger"
											onClick={() => handleDelete(user.id)}
										>
											Delete
										</button>
										{/* Add "Edit" button here if you wire up PUT */}
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
};
