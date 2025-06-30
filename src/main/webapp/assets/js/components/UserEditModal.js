const UserEditModal = ({
  user,
  onClose,
  onSave,
  baseUrl
}) => {
  const [formData, setFormData] = React.useState({
    ...user
  });
  const [imageFile, setImageFile] = React.useState(null);
  const [saving, setSaving] = React.useState(false);
  const handleChange = (field, value) => {
    setFormData({
      ...formData,
      [field]: value
    });
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
  return /*#__PURE__*/React.createElement("div", {
    className: "modal show d-block",
    tabIndex: "-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-dialog"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-content"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-header"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "modal-title"
  }, "Edit User"), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    onClick: onClose
  })), /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "First Name"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control",
    value: formData.firstName || "",
    onChange: e => handleChange("firstName", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Last Name"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control",
    value: formData.lastName || "",
    onChange: e => handleChange("lastName", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Username"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control",
    value: formData.username || "",
    onChange: e => handleChange("username", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Email"), /*#__PURE__*/React.createElement("input", {
    type: "email",
    className: "form-control",
    value: formData.email || "",
    onChange: e => handleChange("email", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Password"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control",
    onChange: e => handleChange("password", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Bio"), /*#__PURE__*/React.createElement("textarea", {
    className: "form-control",
    value: formData.bio || "",
    onChange: e => handleChange("bio", e.target.value)
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Profile Image"), /*#__PURE__*/React.createElement("input", {
    type: "file",
    className: "form-control",
    onChange: e => setImageFile(e.target.files[0])
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Role Type"), /*#__PURE__*/React.createElement("select", {
    value: formData.roleName,
    onChange: e => handleChange("roleName", e.target.value),
    className: "form-select form-select-sm"
  }, /*#__PURE__*/React.createElement("option", {
    value: "user"
  }, "User"), /*#__PURE__*/React.createElement("option", {
    value: "admin"
  }, "Admin"), /*#__PURE__*/React.createElement("option", {
    value: "superadmin"
  }, "Superadmin")))), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-secondary",
    onClick: onClose
  }, "Cancel"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-success",
    onClick: handleSubmit,
    disabled: saving
  }, saving ? "Saving..." : "Save Changes")))));
};
export default UserEditModal;