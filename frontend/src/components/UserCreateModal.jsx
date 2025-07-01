const UserCreateModal = ({ onClose, onCreated, baseUrl }) => {
	const [formData, setFormData] = React.useState({
		firstName: "",
		lastName: "",
		username: "",
		email: "",
		bio: "",
		password: "",
		roleType: "user",
		premium: false,
		artist: false
	});
	const [imageFile, setImageFile] = React.useState(null);
	const [saving, setSaving] = React.useState(false);

	const handleChange = (field, value) => {
		setFormData({ ...formData, [field]: value });
	};

	const handleSubmit = async () => {
		setSaving(true);
		const body = new FormData();

		for (const key in formData) {
			body.append(key, formData[key]);
		}
		if (imageFile) {
			body.append("image", imageFile);
		}

		try {
			const res = await fetch(`${baseUrl}/api/user`, {
				method: "POST",
				body
			});
			if (!res.ok) throw new Error("Create failed");

			toastr.success("User created.");
			onCreated(); // trigger reload
			onClose(); // close modal
		} catch (err) {
			console.error("Failed to create user:", err);
			toastr.error("Failed to create user.");
		} finally {
			setSaving(false);
		}
	};

	return (
		<div className="modal show d-block" tabIndex="-1">
			<div className="modal-dialog">
				<div className="modal-content">
					<div className="modal-header">
						<h5 className="modal-title">Create New User</h5>
						<button type="button" className="btn-close" onClick={onClose}></button>
					</div>
					<div className="modal-body">
						<div className="mb-3">
							<label className="form-label">First Name</label>
							<input
								type="text"
								autoComplete="off"
								className="form-control"
								value={formData.firstName}
								onChange={(e) => handleChange("firstName", e.target.value)}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Last Name</label>
							<input
								type="text"
								autoComplete="off"
								className="form-control"
								value={formData.lastName}
								onChange={(e) => handleChange("lastName", e.target.value)}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Username</label>
							<input
								type="text"
								autoComplete="new-username" // prevent autofill
								className="form-control"
								value={formData.username}
								onChange={(e) => handleChange("username", e.target.value)}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Email</label>
							<input
								type="email"
								autoComplete="off"
								className="form-control"
								value={formData.email}
								onChange={(e) => handleChange("email", e.target.value)}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Password</label>
							<input
								type="password"
								autoComplete="new-password" // prevent browser autofill
								className="form-control"
								value={formData.password}
								onChange={(e) => handleChange("password", e.target.value)}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Bio</label>
							<textarea
								className="form-control"
								value={formData.bio}
								onChange={(e) => handleChange("bio", e.target.value)}
							></textarea>
						</div>

						<div className="mb-3">
							<label className="form-label">Profile Image</label>
							<input
								type="file"
								className="form-control"
								onChange={(e) => setImageFile(e.target.files[0])}
							/>
						</div>

						<div className="mb-3">
							<label className="form-label">Role</label>
							<select
								value={formData.roleType}
								onChange={(e) => handleChange("roleType", e.target.value)}
								className="form-select"
							>
								<option value="user">User</option>
								<option value="admin">Admin</option>
								<option value="superadmin">Superadmin</option>
							</select>
						</div>
					</div>
					<div className="modal-footer">
						<button className="btn btn-secondary" onClick={onClose}>Cancel</button>
						<button className="btn btn-success" onClick={handleSubmit} disabled={saving}>
							{saving ? "Creating..." : "Create User"}
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};

export default UserCreateModal;