import KpiCard from "./KpiCard.js";
export const AdminSubscriptionList = ({
  baseUrl
}) => {
  const [kpiData, setKpiData] = React.useState(null);
  const [subscriptions, setSubscriptions] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  // Pagination state
  const [currentPage, setCurrentPage] = React.useState(1);
  const [totalSubscriptions, setTotalSubscriptions] = React.useState(0);
  const [subscriptionsPerPage] = React.useState(10);

  // Cancel modal state
  const [cancelSubscription, setCancelSubscription] = React.useState(null);
  const [cancelReason, setCancelReason] = React.useState("");

  // Calculate pagination values
  const totalPages = Math.ceil(totalSubscriptions / subscriptionsPerPage);
  const offset = (currentPage - 1) * subscriptionsPerPage;
  const fetchSubscriptions = async (page = 1) => {
    try {
      setLoading(true);
      const pageOffset = (page - 1) * subscriptionsPerPage;
      const subscriptionRes = await fetch(`${baseUrl}/api/subscription?limit=${subscriptionsPerPage}&offset=${pageOffset}`);
      const subscriptionJson = await subscriptionRes.json();
      setSubscriptions(subscriptionJson.data.results);
    } catch (err) {
      console.error("Error loading subscriptions:", err);
      setError("Failed to load subscriptions.");
    } finally {
      setLoading(false);
    }
  };
  React.useEffect(() => {
    const fetchData = async () => {
      try {
        const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=subscription`);
        const kpiJson = await kpiRes.json();
        setKpiData(kpiJson.data.results);
        setTotalSubscriptions(kpiJson.data.results.totalSubscription);
      } catch (err) {
        console.error("Error loading KPI data:", err);
        setError("Failed to load KPI data.");
      }
    };
    fetchData();
  }, [baseUrl]);
  React.useEffect(() => {
    fetchSubscriptions(currentPage);
  }, [baseUrl, currentPage, subscriptionsPerPage]);
  const handlePageChange = page => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };
  const handleCancelClick = subscription => {
    setCancelSubscription(subscription);
    setCancelReason("");
  };
  const confirmCancelSubscription = async () => {
    if (!cancelReason.trim()) {
      toastr.error("Please provide a reason for cancellation");
      return;
    }
    try {
      const res = await fetch(`${baseUrl}/api/subscription/${cancelSubscription.subscriptionId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          message: cancelReason.trim()
        })
      });
      if (!res.ok) throw new Error("Cancel failed");
      toastr.success("Subscription cancelled and user notified");

      // Refresh the current page
      await fetchSubscriptions(currentPage);
      setCancelSubscription(null);
      setCancelReason("");
    } catch (err) {
      console.error("Error cancelling subscription:", err);
      toastr.error("Failed to cancel subscription");
    }
  };
  const getSubscriptionStatus = subscription => {
    return subscription.endDate ? "cancelled" : "active";
  };
  const formatDate = dateString => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString();
  };
  const formatCurrency = amount => {
    return new Intl.NumberFormat('ms-MY', {
      style: 'currency',
      currency: 'MYR'
    }).format(amount);
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
      "aria-label": "Subscription pagination"
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
  }, "Subscription Management"), kpiData && /*#__PURE__*/React.createElement("div", {
    className: "row mb-4"
  }, /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Subscriptions",
    value: kpiData.totalSubscription
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Active Subscriptions",
    value: kpiData.activeSubscription
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Cancelled Subscriptions",
    value: kpiData.cancelledSubscription
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Revenue",
    value: formatCurrency(kpiData.totalRevenue)
  })), /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-between align-items-center mb-3"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title mb-0"
  }, "Subscriptions"), /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center gap-3"
  }, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, "Showing ", offset + 1, "-", Math.min(offset + subscriptionsPerPage, totalSubscriptions), " of ", totalSubscriptions, " subscriptions"))), /*#__PURE__*/React.createElement("div", {
    className: "table-responsive"
  }, /*#__PURE__*/React.createElement("table", {
    className: "table table-hover"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "User"), /*#__PURE__*/React.createElement("th", null, "Plan"), /*#__PURE__*/React.createElement("th", null, "Status"), /*#__PURE__*/React.createElement("th", null, "Duration"), /*#__PURE__*/React.createElement("th", null, "Amount"), /*#__PURE__*/React.createElement("th", null, "Gateway Ref"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, subscriptions.map(subscription => /*#__PURE__*/React.createElement("tr", {
    key: subscription.subscriptionId
  }, /*#__PURE__*/React.createElement("td", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/user/${subscription.user.userId}?size=thumb`,
    alt: "User",
    style: {
      width: "32px",
      height: "32px",
      objectFit: "cover",
      borderRadius: "50%",
      marginRight: "10px"
    }
  }), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "fw-medium"
  }, subscription.user.username), /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, subscription.user.email))), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", {
    className: "fw-medium"
  }, subscription.subscriptionPlan.name), /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, formatCurrency(subscription.subscriptionPlan.price), "/", subscription.subscriptionPlan.billingCycle)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: `badge ${getSubscriptionStatus(subscription) === 'active' ? 'bg-success' : 'bg-secondary'}`
  }, getSubscriptionStatus(subscription))), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", null, formatDate(subscription.startDate)), /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, "to ", formatDate(subscription.endDate) || "Active")), /*#__PURE__*/React.createElement("td", null, formatCurrency(subscription.amountPaid)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("small", {
    className: "text-muted font-monospace"
  }, subscription.paymentGatewayRef ? subscription.paymentGatewayRef.substring(0, 12) + "..." : "N/A")), /*#__PURE__*/React.createElement("td", null, getSubscriptionStatus(subscription) === 'active' && /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-danger",
    onClick: () => handleCancelClick(subscription)
  }, "Cancel"))))))), /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-center mt-3"
  }, renderPagination()))), cancelSubscription && /*#__PURE__*/React.createElement("div", {
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
  }, "Cancel Subscription"), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    onClick: () => setCancelSubscription(null)
  })), /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "alert alert-warning"
  }, /*#__PURE__*/React.createElement("strong", null, "Warning:"), " This action will cancel the subscription immediately and notify the user via email."), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("strong", null, "User:"), " ", cancelSubscription.user.username, " (", cancelSubscription.user.email, ")"), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("strong", null, "Plan:"), " ", cancelSubscription.subscriptionPlan.name, " - ", formatCurrency(cancelSubscription.amountPaid)), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("strong", null, "Gateway Reference:"), " ", cancelSubscription.paymentGatewayRef || "N/A"), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "cancelReason",
    className: "form-label"
  }, /*#__PURE__*/React.createElement("strong", null, "Reason for cancellation *")), /*#__PURE__*/React.createElement("textarea", {
    id: "cancelReason",
    className: "form-control",
    rows: "4",
    placeholder: "Please provide a reason for cancelling this subscription...",
    value: cancelReason,
    onChange: e => setCancelReason(e.target.value)
  }))), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-secondary",
    onClick: () => setCancelSubscription(null)
  }, "Cancel"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-danger",
    onClick: confirmCancelSubscription,
    disabled: !cancelReason.trim()
  }, "Yes, Cancel Subscription"))))));
};