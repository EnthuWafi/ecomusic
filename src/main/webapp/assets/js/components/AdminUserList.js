const AdminUserList = () => {
  const [users, setUsers] = React.useState([{
    id: 1,
    name: "Sarah Johnson",
    email: "sarah.johnson@email.com",
    role: "admin",
    status: "active",
    joinDate: "2024-01-15",
    lastLogin: "2025-06-22",
    tracksUploaded: 23,
    avatar: "SJ"
  }, {
    id: 2,
    name: "Mike Rodriguez",
    email: "mike.rodriguez@email.com",
    role: "artist",
    status: "active",
    joinDate: "2024-02-20",
    lastLogin: "2025-06-21",
    tracksUploaded: 45,
    avatar: "MR"
  }, {
    id: 3,
    name: "Emma Thompson",
    email: "emma.thompson@email.com",
    role: "user",
    status: "active",
    joinDate: "2024-03-10",
    lastLogin: "2025-06-20",
    tracksUploaded: 12,
    avatar: "ET"
  }, {
    id: 4,
    name: "Alex Chen",
    email: "alex.chen@email.com",
    role: "artist",
    status: "inactive",
    joinDate: "2024-01-25",
    lastLogin: "2025-05-15",
    tracksUploaded: 89,
    avatar: "AC"
  }, {
    id: 5,
    name: "David Kim",
    email: "david.kim@email.com",
    role: "premium",
    status: "active",
    joinDate: "2024-04-05",
    lastLogin: "2025-06-22",
    tracksUploaded: 67,
    avatar: "DK"
  }, {
    id: 6,
    name: "Lisa Wang",
    email: "lisa.wang@email.com",
    role: "artist",
    status: "pending",
    joinDate: "2025-06-20",
    lastLogin: "Never",
    tracksUploaded: 0,
    avatar: "LW"
  }, {
    id: 7,
    name: "James Brown",
    email: "james.brown@email.com",
    role: "admin",
    status: "active",
    joinDate: "2023-12-01",
    lastLogin: "2025-06-22",
    tracksUploaded: 156,
    avatar: "JB"
  }, {
    id: 8,
    name: "Anna Garcia",
    email: "anna.garcia@email.com",
    role: "artist",
    status: "active",
    joinDate: "2024-05-12",
    lastLogin: "2025-06-19",
    tracksUploaded: 78,
    avatar: "AG"
  }, {
    id: 9,
    name: "Tom Wilson",
    email: "tom.wilson@email.com",
    role: "user",
    status: "active",
    joinDate: "2024-06-01",
    lastLogin: "2025-06-21",
    tracksUploaded: 5,
    avatar: "TW"
  }, {
    id: 10,
    name: "Maya Patel",
    email: "maya.patel@email.com",
    role: "artist",
    status: "active",
    joinDate: "2024-03-15",
    lastLogin: "2025-06-22",
    tracksUploaded: 134,
    avatar: "MP"
  }]);
  const [searchTerm, setSearchTerm] = React.useState("");
  const [showAddModal, setShowAddModal] = React.useState(false);
  const filteredUsers = users.filter(user => user.name.toLowerCase().includes(searchTerm.toLowerCase()) || user.email.toLowerCase().includes(searchTerm.toLowerCase()) || user.role.toLowerCase().includes(searchTerm.toLowerCase()));
  const getAvatarColor = name => {
    const colors = ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7'];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
  };
  const getStatusBadge = status => {
    const statusClasses = {
      active: 'status-badge status-active',
      inactive: 'status-badge status-inactive',
      pending: 'status-badge status-pending'
    };
    return statusClasses[status] || 'status-badge';
  };
  const getRoleBadge = role => {
    const roleClasses = {
      admin: 'role-badge role-admin',
      user: 'role-badge role-user',
      artist: 'role-badge role-artist',
      premium: 'role-badge role-premium'
    };
    return roleClasses[role] || 'role-badge';
  };
  const handleDeleteUser = userId => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      setUsers(users.filter(user => user.id !== userId));
    }
  };
  const handleToggleStatus = userId => {
    setUsers(users.map(user => user.id === userId ? {
      ...user,
      status: user.status === 'active' ? 'inactive' : 'active'
    } : user));
  };
  const stats = {
    total: users.length,
    active: users.filter(u => u.status === 'active').length,
    artist: users.filter(u => u.role === 'artist').length,
    premium: users.filter(u => u.role === 'premium').length,
    admin: users.filter(u => u.role === 'admin').length
  };
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "page-header"
  }, /*#__PURE__*/React.createElement("div", {
    className: "container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-8"
  }, /*#__PURE__*/React.createElement("h1", {
    className: "mb-2"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-people-fill me-3"
  }), "User Management"), /*#__PURE__*/React.createElement("p", {
    className: "mb-0 opacity-75"
  }, "Manage users and administrators")), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4 text-end"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn add-admin-btn",
    onClick: () => setShowAddModal(true)
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-person-plus-fill me-2"
  }), "Add Admin"))))), /*#__PURE__*/React.createElement("div", {
    className: "container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row stats-row g-3"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-lg-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-card total"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-number"
  }, stats.total), /*#__PURE__*/React.createElement("div", {
    className: "stat-label"
  }, "Total Users"))), /*#__PURE__*/React.createElement("div", {
    className: "col-lg-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-card active"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-number"
  }, stats.active), /*#__PURE__*/React.createElement("div", {
    className: "stat-label"
  }, "Active Users"))), /*#__PURE__*/React.createElement("div", {
    className: "col-lg-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-card artist"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-number"
  }, stats.artist), /*#__PURE__*/React.createElement("div", {
    className: "stat-label"
  }, "Artists"))), /*#__PURE__*/React.createElement("div", {
    className: "col-lg-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-card premium"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-number"
  }, stats.premium), /*#__PURE__*/React.createElement("div", {
    className: "stat-label"
  }, "Premium Users"))), /*#__PURE__*/React.createElement("div", {
    className: "col-lg-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-card admin"
  }, /*#__PURE__*/React.createElement("div", {
    className: "stat-number"
  }, stats.admin), /*#__PURE__*/React.createElement("div", {
    className: "stat-label"
  }, "Administrators")))), /*#__PURE__*/React.createElement("div", {
    className: "search-container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-8"
  }, /*#__PURE__*/React.createElement("div", {
    className: "input-group"
  }, /*#__PURE__*/React.createElement("span", {
    className: "input-group-text bg-white border-end-0"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-search text-muted"
  })), /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control search-input border-start-0",
    placeholder: "Search users and artists by name, email, or role...",
    value: searchTerm,
    onChange: e => setSearchTerm(e.target.value)
  }))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4 text-end"
  }, /*#__PURE__*/React.createElement("span", {
    className: "text-muted"
  }, "Showing ", filteredUsers.length, " of ", users.length, " users & artists")))), /*#__PURE__*/React.createElement("div", {
    className: "user-table-container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "table-responsive"
  }, /*#__PURE__*/React.createElement("table", {
    className: "table"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "User/Artist"), /*#__PURE__*/React.createElement("th", null, "Role"), /*#__PURE__*/React.createElement("th", null, "Status"), /*#__PURE__*/React.createElement("th", null, "Join Date"), /*#__PURE__*/React.createElement("th", null, "Last Login"), /*#__PURE__*/React.createElement("th", null, "Tracks"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, filteredUsers.map(user => /*#__PURE__*/React.createElement("tr", {
    key: user.id
  }, /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "user-avatar me-3",
    style: {
      backgroundColor: getAvatarColor(user.name)
    }
  }, user.avatar), /*#__PURE__*/React.createElement("div", {
    className: "user-info"
  }, /*#__PURE__*/React.createElement("h6", null, user.name), /*#__PURE__*/React.createElement("div", {
    className: "user-email"
  }, user.email)))), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: getRoleBadge(user.role)
  }, user.role)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: getStatusBadge(user.status)
  }, user.status)), /*#__PURE__*/React.createElement("td", null, new Date(user.joinDate).toLocaleDateString()), /*#__PURE__*/React.createElement("td", null, user.lastLogin === 'Never' ? 'Never' : new Date(user.lastLogin).toLocaleDateString()), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: "fw-bold"
  }, user.tracksUploaded)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", {
    className: "d-flex"
  }, /*#__PURE__*/React.createElement("button", {
    className: "action-btn text-primary",
    title: "Edit User"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-pencil-square"
  })), /*#__PURE__*/React.createElement("button", {
    className: "action-btn text-warning",
    title: "Toggle Status",
    onClick: () => handleToggleStatus(user.id)
  }, /*#__PURE__*/React.createElement("i", {
    className: user.status === 'active' ? 'bi bi-pause-circle' : 'bi bi-play-circle'
  })), /*#__PURE__*/React.createElement("button", {
    className: "action-btn text-danger",
    title: "Delete User",
    onClick: () => handleDeleteUser(user.id)
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-trash3"
  })))))))))), filteredUsers.length === 0 && /*#__PURE__*/React.createElement("div", {
    className: "text-center py-5"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-search display-4 text-muted mb-3"
  }), /*#__PURE__*/React.createElement("h5", {
    className: "text-muted"
  }, "No users or artists found"), /*#__PURE__*/React.createElement("p", {
    className: "text-muted"
  }, "Try adjusting your search criteria"))));
};