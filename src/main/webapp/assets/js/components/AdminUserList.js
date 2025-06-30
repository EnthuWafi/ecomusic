import KpiCard from "./KpiCard.js";
export const AdminUserList = ({
  baseUrl
}) => {
  const [kpiData, setKpiData] = React.useState(null);
  const [users, setUsers] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  React.useEffect(() => {
    const fetchData = async () => {
      try {
        const [kpiRes, userRes] = await Promise.all([fetch(`${baseUrl}/api/report/kpis?type=user`), fetch(`${baseUrl}/api/user?limit=5&offset=0`)]);
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
  const handleDelete = async userId => {
    if (!confirm("Are you sure you want to delete this user?")) return;
    try {
      const res = await fetch(`${baseUrl}/api/user/${userId}`, {
        method: "DELETE"
      });
      if (!res.ok) throw new Error("Delete failed");
      setUsers(users.filter(user => user.id !== userId));
    } catch (err) {
      console.error("Error deleting user:", err);
      toastr.error("Failed to delete user.");
    }
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
    className: "container mt-5"
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
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title"
  }, "Users"), /*#__PURE__*/React.createElement("table", {
    className: "table table-hover"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Name"), /*#__PURE__*/React.createElement("th", null, "Email"), /*#__PURE__*/React.createElement("th", null, "Role"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, users.map(user => /*#__PURE__*/React.createElement("tr", {
    key: user.userId
  }, /*#__PURE__*/React.createElement("td", null, user.username), /*#__PURE__*/React.createElement("td", null, user.email), /*#__PURE__*/React.createElement("td", null, user.roleName), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-danger",
    onClick: () => handleDelete(user.id)
  }, "Delete")))))))));
};