import KpiCard from "./KpiCard.js";
import UserEditModal from "./UserEditModal.js";
import UserCreateModal from "./UserCreateModal.js";
export const AdminUserList = ({
  baseUrl
}) => {
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
  const handlePageChange = page => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };
  const handleDelete = async userId => {
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
    setPendingRoleChange({
      userId,
      newRole
    });
  };
  const confirmRoleChange = async () => {
    const {
      userId,
      newRole
    } = pendingRoleChange;
    try {
      const res = await fetch(`${baseUrl}/api/user/${userId}/role`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          roleType: newRole
        })
      });
      if (!res.ok) throw new Error("Failed to update role");
      toastr.success("Role updated successfully");
      setUsers(users.map(u => u.userId === userId ? {
        ...u,
        roleName: newRole
      } : u));
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
    pages.push(/*#__PURE__*/React.createElement("li", {
      key: "prev",
      className: `page-item ${currentPage === 1 ? 'disabled' : ''}`
    }, /*#__PURE__*/React.createElement("button", {
      className: "page-link",
      onClick: () => handlePageChange(currentPage - 1),
      disabled: currentPage === 1
    }, "Previous")));

    // First page + ellipsis
    if (startPage > 1) {
      pages.push(/*#__PURE__*/React.createElement("li", {
        key: 1,
        className: "page-item"
      }, /*#__PURE__*/React.createElement("button", {
        className: "page-link",
        onClick: () => handlePageChange(1)
      }, "1")));
      if (startPage > 2) {
        pages.push(/*#__PURE__*/React.createElement("li", {
          key: "ellipsis1",
          className: "page-item disabled"
        }, /*#__PURE__*/React.createElement("span", {
          className: "page-link"
        }, "...")));
      }
    }

    // Page numbers
    for (let i = startPage; i <= endPage; i++) {
      pages.push(/*#__PURE__*/React.createElement("li", {
        key: i,
        className: `page-item ${currentPage === i ? 'active' : ''}`
      }, /*#__PURE__*/React.createElement("button", {
        className: "page-link",
        onClick: () => handlePageChange(i)
      }, i)));
    }

    // Last page + ellipsis
    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        pages.push(/*#__PURE__*/React.createElement("li", {
          key: "ellipsis2",
          className: "page-item disabled"
        }, /*#__PURE__*/React.createElement("span", {
          className: "page-link"
        }, "...")));
      }
      pages.push(/*#__PURE__*/React.createElement("li", {
        key: totalPages,
        className: "page-item"
      }, /*#__PURE__*/React.createElement("button", {
        className: "page-link",
        onClick: () => handlePageChange(totalPages)
      }, totalPages)));
    }

    // Next button
    pages.push(/*#__PURE__*/React.createElement("li", {
      key: "next",
      className: `page-item ${currentPage === totalPages ? 'disabled' : ''}`
    }, /*#__PURE__*/React.createElement("button", {
      className: "page-link",
      onClick: () => handlePageChange(currentPage + 1),
      disabled: currentPage === totalPages
    }, "Next")));
    return /*#__PURE__*/React.createElement("nav", {
      "aria-label": "User pagination"
    }, /*#__PURE__*/React.createElement("ul", {
      className: "pagination justify-content-center mb-0"
    }, pages));
  };
  if (loading) {
    return /*#__PURE__*/React.createElement("div", {
      className: "text-center my-5"
    }, /*#__PURE__*/React.createElement("div", {
      className: "spinner-border text-primary",
      role: "status"
    }, /*#__PURE__*/React.createElement("span", {
      className: "visually-hidden"
    }, "Loading...")));
  }
  if (error) {
    return /*#__PURE__*/React.createElement("div", {
      className: "alert alert-danger"
    }, error);
  }
  return /*#__PURE__*/React.createElement("div", {
    className: "container-xl mt-5"
  }, /*#__PURE__*/React.createElement("h2", {
    className: "mb-4"
  }, "User Management"), kpiData && /*#__PURE__*/React.createElement("div", {
    className: "row mb-4"
  }, /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Users",
    value: kpiData.totalUserCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Admin Users",
    value: kpiData.adminCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Superadmin Users",
    value: kpiData.superAdminCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Artist Users",
    value: kpiData.artistCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Premium Users",
    value: kpiData.premiumCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Regular Users",
    value: kpiData.userCount
  })), /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-between align-items-center mb-3"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title mb-0"
  }, "Users"), /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center gap-3"
  }, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, "Showing ", offset + 1, "-", Math.min(offset + usersPerPage, totalUsers), " of ", totalUsers, " users"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-success",
    onClick: () => setShowAddModal(true)
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-person-plus"
  }), " Add Admin"))), /*#__PURE__*/React.createElement("div", {
    className: "table-responsive"
  }, /*#__PURE__*/React.createElement("table", {
    className: "table table-hover"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Username"), /*#__PURE__*/React.createElement("th", null, "Email"), /*#__PURE__*/React.createElement("th", null, "Role"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, users.map(user => /*#__PURE__*/React.createElement("tr", {
    key: user.userId
  }, /*#__PURE__*/React.createElement("td", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/user/${user.userId}`,
    alt: "User",
    style: {
      width: "32px",
      height: "32px",
      objectFit: "cover",
      borderRadius: "50%",
      marginRight: "10px"
    }
  }), /*#__PURE__*/React.createElement("span", null, user.username)), /*#__PURE__*/React.createElement("td", null, user.email), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("select", {
    className: "form-select form-select-sm",
    value: user.roleName,
    onChange: e => handleRoleChange(user.userId, e.target.value)
  }, /*#__PURE__*/React.createElement("option", {
    value: "user"
  }, "User"), /*#__PURE__*/React.createElement("option", {
    value: "admin"
  }, "Admin"), /*#__PURE__*/React.createElement("option", {
    value: "superadmin"
  }, "Superadmin"))), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-primary me-2",
    onClick: () => setEditUser({
      ...user
    })
  }, "Edit"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-danger",
    onClick: () => handleDelete(user.userId)
  }, "Delete"))))))), /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-center mt-3"
  }, renderPagination()))), pendingRoleChange && /*#__PURE__*/React.createElement("div", {
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
  }, "Confirm Role Change"), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    onClick: () => setPendingRoleChange(null)
  })), /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, /*#__PURE__*/React.createElement("p", null, "Are you sure you want to change this user's role to ", /*#__PURE__*/React.createElement("strong", null, pendingRoleChange.newRole), "?")), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-secondary",
    onClick: () => setPendingRoleChange(null)
  }, "Cancel"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-primary",
    onClick: confirmRoleChange
  }, "Yes, Change"))))), showAddModal && /*#__PURE__*/React.createElement(UserCreateModal, {
    onClose: () => setShowAddModal(false),
    onCreated: () => {
      setShowAddModal(false);
      fetchUsers(currentPage); // Refresh current page instead of reloading
    },
    baseUrl: baseUrl
  }), editUser && /*#__PURE__*/React.createElement(UserEditModal, {
    user: editUser,
    onClose: () => setEditUser(null),
    onSave: () => {
      setEditUser(null);
      fetchUsers(currentPage); // Refresh current page instead of reloading
    },
    baseUrl: baseUrl
  }));
};