
const UserEditModal = ({ user, onClose, onSave, baseUrl }) => {
	const [formData, setFormData] = React.useState({ ...user });
	const [imageFile, setImageFile] = React.useState(null);
	const [saving, setSaving] = React.useState(false);

	const handleChange = (field, value) => {
		setFormData({ ...formData, [field]: value });
	};

	const handleSubmit = async () => {
		setSaving(true);
		const body = new FormData();

		body.append("firstName", formData.firstName || "");
		body.append("lastName", formData.lastName || "");
		body.append("username", formData.username || "");
		body.append("email", formData.email || "");
		body.append("bio", formData.bio || "");
		body.append("password", "");
		body.append("roleType", formData.roleName); 
		body.append("premium", formData.premium);
		body.append("artist", formData.artist);

		if (imageFile) {
			body.append("image", imageFile);
		}

		try {
			const res = await fetch(`${baseUrl}/api/user/${user.userId}`, {
				method: "PUT",
				body
			});
			if (!res.ok) throw new Error("Update failed");

			toastr.success("User updated.");
			onSave(); // trigger reload or update
		} catch (err) {
			console.error("Failed to save user:", err);
			toastr.error("Failed to update user.");
		} finally {
			setSaving(false);
		}
	};

	return (
		<div className="modal show d-block" tabIndex="-1">
			<div className="modal-dialog">
				<div className="modal-content">
					<div className="modal-header">
						<h5 className="modal-title">Edit User</h5>
						<button type="button" className="btn-close" onClick={onClose}></button>
					</div>
					<div className="modal-body">
						<div className="mb-3">
							<label className="form-label">First Name</label>
							<input
								type="text"
								className="form-control"
								value={formData.firstName || ""}
								onChange={(e) => handleChange("firstName", e.target.value)}
							/>
						</div>
						<div className="mb-3">
							<label className="form-label">Last Name</label>
							<input
								type="text"
								className="form-control"
								value={formData.lastName || ""}
								onChange={(e) => handleChange("lastName", e.target.value)}
							/>
						</div>
						<div className="mb-3">
							<label className="form-label">Username</label>
							<input
								type="text"
								className="form-control"
								value={formData.username || ""}
								onChange={(e) => handleChange("username", e.target.value)}
							/>
						</div>
						<div className="mb-3">
							<label className="form-label">Email</label>
							<input
								type="email"
								className="form-control"
								value={formData.email || ""}
								onChange={(e) => handleChange("email", e.target.value)}
							/>
						</div>
						<div className="mb-3">
							<label className="form-label">Password</label>
							<input
								type="text"
								className="form-control"
								onChange={(e) => handleChange("password", e.target.value)}
							/>
						</div>
						<div className="mb-3">
							<label className="form-label">Bio</label>
							<textarea
								className="form-control"
								value={formData.bio || ""}
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
							<label className="form-label">Role Type</label>
							<select value={formData.roleName} onChange={(e) => handleChange("roleName", e.target.value)} className="form-select form-select-sm">
								<option value="user">User</option>
								<option value="admin">Admin</option>
								<option value="superadmin">Superadmin</option>
							</select>
						</div>
					</div>
					<div className="modal-footer">
						<button className="btn btn-secondary" onClick={onClose}>
							Cancel
						</button>
						<button className="btn btn-success" onClick={handleSubmit} disabled={saving}>
							{saving ? "Saving..." : "Save Changes"}
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};

export default UserEditModal;
