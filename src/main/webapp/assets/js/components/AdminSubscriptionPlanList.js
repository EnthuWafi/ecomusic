import KpiCard from "./KpiCard.js";
export const AdminSubscriptionPlanList = ({
  baseUrl
}) => {
  const [kpiData, setKpiData] = React.useState(null);
  const [plans, setPlans] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  // Pagination state
  const [currentPage, setCurrentPage] = React.useState(1);
  const [totalPlans, setTotalPlans] = React.useState(0);
  const [plansPerPage] = React.useState(10);

  // Modal states
  const [showCreateModal, setShowCreateModal] = React.useState(false);
  const [editPlan, setEditPlan] = React.useState(null);
  const [deletePlan, setDeletePlan] = React.useState(null);

  // Form state
  const [formData, setFormData] = React.useState({
    name: "",
    stripePriceId: "",
    billingCycle: "monthly",
    price: "",
    description: "",
    planType: "LISTENER",
    features: []
  });
  const [featureInput, setFeatureInput] = React.useState("");

  // Calculate pagination values
  const totalPages = Math.ceil(totalPlans / plansPerPage);
  const offset = (currentPage - 1) * plansPerPage;
  const fetchPlans = async (page = 1) => {
    try {
      setLoading(true);
      const pageOffset = (page - 1) * plansPerPage;
      const planRes = await fetch(`${baseUrl}/api/subscription-plan?limit=${plansPerPage}&offset=${pageOffset}`);
      const planJson = await planRes.json();
      setPlans(planJson.data.results);
    } catch (err) {
      console.error("Error loading plans:", err);
      setError("Failed to load subscription plans.");
    } finally {
      setLoading(false);
    }
  };
  React.useEffect(() => {
    const fetchData = async () => {
      try {
        const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=subscription-plan`);
        const kpiJson = await kpiRes.json();
        setKpiData(kpiJson.data.results);
        setTotalPlans(kpiJson.data.results.totalPlan);
      } catch (err) {
        console.error("Error loading KPI data:", err);
        setError("Failed to load KPI data.");
      }
    };
    fetchData();
  }, [baseUrl]);
  React.useEffect(() => {
    fetchPlans(currentPage);
  }, [baseUrl, currentPage, plansPerPage]);
  const handlePageChange = page => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };
  const resetForm = () => {
    setFormData({
      name: "",
      stripePriceId: "",
      billingCycle: "monthly",
      price: "",
      description: "",
      planType: "LISTENER",
      features: []
    });
    setFeatureInput("");
  };
  const handleCreateClick = () => {
    resetForm();
    setShowCreateModal(true);
  };
  const handleEditClick = plan => {
    setFormData({
      name: plan.name,
      stripePriceId: plan.stripePriceId,
      billingCycle: plan.billingCycle,
      price: plan.price.toString(),
      description: plan.description,
      planType: plan.planType,
      features: [...plan.features]
    });
    setEditPlan(plan);
  };
  const handleDeleteClick = plan => {
    setDeletePlan(plan);
  };
  const handleFormSubmit = async e => {
    e.preventDefault();
    if (!formData.name.trim() || !formData.price) {
      toastr.error("Please fill in all required fields");
      return;
    }
    try {
      const payload = {
        ...formData,
        price: parseFloat(formData.price),
        features: formData.features.filter(f => f.trim())
      };
      const url = editPlan ? `${baseUrl}/api/subscription-plan/${editPlan.subscriptionPlanId}` : `${baseUrl}/api/subscription-plan`;
      const method = editPlan ? "PUT" : "POST";
      const res = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error("Save failed");
      toastr.success(`Subscription plan ${editPlan ? 'updated' : 'created'} successfully`);

      // Close modals and refresh
      setShowCreateModal(false);
      setEditPlan(null);
      resetForm();
      await fetchPlans(currentPage);
    } catch (err) {
      console.error("Error saving plan:", err);
      toastr.error("Failed to save subscription plan");
    }
  };
  const confirmDelete = async () => {
    try {
      const res = await fetch(`${baseUrl}/api/subscription-plan/${deletePlan.subscriptionPlanId}`, {
        method: "DELETE"
      });
      if (!res.ok) throw new Error("Delete failed");
      toastr.success("Subscription plan deleted successfully");
      setDeletePlan(null);
      await fetchPlans(currentPage);
    } catch (err) {
      console.error("Error deleting plan:", err);
      toastr.error("Failed to delete subscription plan");
    }
  };
  const addFeature = () => {
    if (featureInput.trim() && !formData.features.includes(featureInput.trim())) {
      setFormData(prev => ({
        ...prev,
        features: [...prev.features, featureInput.trim()]
      }));
      setFeatureInput("");
    }
  };
  const removeFeature = index => {
    setFormData(prev => ({
      ...prev,
      features: prev.features.filter((_, i) => i !== index)
    }));
  };
  const formatCurrency = amount => {
    return new Intl.NumberFormat('ms-MY', {
      style: 'currency',
      currency: 'MYR'
    }).format(amount);
  };
  const formatDate = dateString => {
    return new Date(dateString).toLocaleDateString();
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
      "aria-label": "Subscription plan pagination"
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
  }, "Subscription Plan Management"), kpiData && /*#__PURE__*/React.createElement("div", {
    className: "row mb-4"
  }, /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Plans",
    value: kpiData.totalPlan
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Creator Plans",
    value: kpiData.creatorPlan
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Listener Plans",
    value: kpiData.listenerPlan
  })), /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-between align-items-center mb-3"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title mb-0"
  }, "Subscription Plans"), /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center gap-3"
  }, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, "Showing ", offset + 1, "-", Math.min(offset + plansPerPage, totalPlans), " of ", totalPlans, " plans"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-success",
    onClick: handleCreateClick
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-plus-circle"
  }), " Create Plan"))), /*#__PURE__*/React.createElement("div", {
    className: "table-responsive"
  }, /*#__PURE__*/React.createElement("table", {
    className: "table table-hover"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Plan Name"), /*#__PURE__*/React.createElement("th", null, "Price"), /*#__PURE__*/React.createElement("th", null, "Billing Cycle"), /*#__PURE__*/React.createElement("th", null, "Type"), /*#__PURE__*/React.createElement("th", null, "Stripe ID"), /*#__PURE__*/React.createElement("th", null, "Features"), /*#__PURE__*/React.createElement("th", null, "Created"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, plans.map(plan => /*#__PURE__*/React.createElement("tr", {
    key: plan.subscriptionPlanId
  }, /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("div", {
    className: "fw-medium"
  }, plan.name), /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, plan.description)), /*#__PURE__*/React.createElement("td", null, formatCurrency(plan.price)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: "badge bg-info"
  }, plan.billingCycle)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: `badge ${plan.planType === 'LISTENER' ? 'bg-warning' : 'bg-secondary'}`
  }, plan.planType)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("small", {
    className: "text-muted font-monospace"
  }, plan.stripePriceId)), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, plan.features.length, " features")), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, formatDate(plan.createdAt))), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-primary me-2",
    onClick: () => handleEditClick(plan)
  }, "Edit"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-danger",
    onClick: () => handleDeleteClick(plan)
  }, "Delete"))))))), /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-center mt-3"
  }, renderPagination()))), (showCreateModal || editPlan) && /*#__PURE__*/React.createElement("div", {
    className: "modal show d-block",
    tabIndex: "-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-dialog modal-lg"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-content"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-header"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "modal-title"
  }, editPlan ? 'Edit Subscription Plan' : 'Create Subscription Plan'), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    onClick: () => {
      setShowCreateModal(false);
      setEditPlan(null);
      resetForm();
    }
  })), /*#__PURE__*/React.createElement("form", {
    onSubmit: handleFormSubmit
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "planName",
    className: "form-label"
  }, "Plan Name *"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    id: "planName",
    className: "form-control",
    value: formData.name,
    onChange: e => setFormData(prev => ({
      ...prev,
      name: e.target.value
    })),
    required: true
  }))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "stripePriceId",
    className: "form-label"
  }, "Stripe Price ID *"), /*#__PURE__*/React.createElement("input", {
    type: "text",
    id: "stripePriceId",
    readOnly: true,
    className: "form-control",
    value: formData.stripePriceId,
    onChange: e => setFormData(prev => ({
      ...prev,
      stripePriceId: e.target.value
    }))
  })))), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "price",
    className: "form-label"
  }, "Price *"), /*#__PURE__*/React.createElement("input", {
    type: "number",
    id: "price",
    className: "form-control",
    step: "0.01",
    value: formData.price,
    onChange: e => setFormData(prev => ({
      ...prev,
      price: e.target.value
    })),
    required: true
  }))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "billingCycle",
    className: "form-label"
  }, "Billing Cycle"), /*#__PURE__*/React.createElement("select", {
    id: "billingCycle",
    className: "form-select",
    value: formData.billingCycle,
    onChange: e => setFormData(prev => ({
      ...prev,
      billingCycle: e.target.value
    }))
  }, /*#__PURE__*/React.createElement("option", {
    value: "monthly"
  }, "Monthly"), /*#__PURE__*/React.createElement("option", {
    value: "yearly"
  }, "Yearly")))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "planType",
    className: "form-label"
  }, "Plan Type"), /*#__PURE__*/React.createElement("select", {
    id: "planType",
    className: "form-select",
    value: formData.planType,
    onChange: e => setFormData(prev => ({
      ...prev,
      planType: e.target.value
    }))
  }, /*#__PURE__*/React.createElement("option", {
    value: "LISTENER"
  }, "Listener"), /*#__PURE__*/React.createElement("option", {
    value: "CREATOR"
  }, "Creator"))))), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    htmlFor: "description",
    className: "form-label"
  }, "Description"), /*#__PURE__*/React.createElement("textarea", {
    id: "description",
    className: "form-control",
    rows: "3",
    value: formData.description,
    onChange: e => setFormData(prev => ({
      ...prev,
      description: e.target.value
    }))
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-3"
  }, /*#__PURE__*/React.createElement("label", {
    className: "form-label"
  }, "Features"), /*#__PURE__*/React.createElement("div", {
    className: "d-flex mb-2"
  }, /*#__PURE__*/React.createElement("input", {
    type: "text",
    className: "form-control me-2",
    placeholder: "Add a feature...",
    value: featureInput,
    onChange: e => setFeatureInput(e.target.value),
    onKeyPress: e => e.key === 'Enter' && (e.preventDefault(), addFeature())
  }), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn btn-outline-primary",
    onClick: addFeature
  }, "Add")), /*#__PURE__*/React.createElement("div", {
    className: "d-flex flex-wrap gap-2"
  }, formData.features.map((feature, index) => /*#__PURE__*/React.createElement("span", {
    key: index,
    className: "badge bg-secondary d-flex align-items-center"
  }, feature, /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close btn-close-white ms-1",
    onClick: () => removeFeature(index),
    style: {
      fontSize: '0.75em'
    }
  })))))), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn btn-secondary",
    onClick: () => {
      setShowCreateModal(false);
      setEditPlan(null);
      resetForm();
    }
  }, "Cancel"), /*#__PURE__*/React.createElement("button", {
    type: "submit",
    className: "btn btn-primary"
  }, editPlan ? 'Update Plan' : 'Create Plan')))))), deletePlan && /*#__PURE__*/React.createElement("div", {
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
  }, "Delete Subscription Plan"), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    onClick: () => setDeletePlan(null)
  })), /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "alert alert-danger"
  }, /*#__PURE__*/React.createElement("strong", null, "Warning:"), " This action cannot be undone. Deleting this plan may affect existing subscriptions."), /*#__PURE__*/React.createElement("p", null, "Are you sure you want to delete the plan ", /*#__PURE__*/React.createElement("strong", null, "\"", deletePlan.name, "\""), "?"), /*#__PURE__*/React.createElement("div", {
    className: "p-3 rounded"
  }, /*#__PURE__*/React.createElement("small", null, /*#__PURE__*/React.createElement("strong", null, "Plan Details:"), /*#__PURE__*/React.createElement("br", null), "Price: ", formatCurrency(deletePlan.price), /*#__PURE__*/React.createElement("br", null), "Billing: ", deletePlan.billingCycle, /*#__PURE__*/React.createElement("br", null), "Stripe ID: ", deletePlan.stripePriceId, /*#__PURE__*/React.createElement("br", null), "Features: ", deletePlan.features.length, " features"))), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-secondary",
    onClick: () => setDeletePlan(null)
  }, "Cancel"), /*#__PURE__*/React.createElement("button", {
    className: "btn btn-danger",
    onClick: confirmDelete
  }, "Yes, Delete Plan"))))));
};