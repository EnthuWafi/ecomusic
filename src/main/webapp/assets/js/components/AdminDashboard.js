const AdminDashboard = () => {
  const [kpiData, setKpiData] = React.useState({
    totalUsers: 15420,
    activeSubscriptions: 8934,
    totalTracks: 47892,
    monthlyRevenue: 89750,
    yearlyRevenue: 967340,
    todaySignups: 127,
    todayUploads: 89
  });
  const [recentActivity] = React.useState([{
    id: 1,
    artist: "Alex Chen",
    track: "Midnight Dreams",
    time: "2 minutes ago",
    avatar: "AC"
  }, {
    id: 2,
    artist: "Sarah Johnson",
    track: "Ocean Waves",
    time: "8 minutes ago",
    avatar: "SJ"
  }, {
    id: 3,
    artist: "Mike Rodriguez",
    track: "City Lights",
    time: "15 minutes ago",
    avatar: "MR"
  }, {
    id: 4,
    artist: "Emma Thompson",
    track: "Forest Path",
    time: "23 minutes ago",
    avatar: "ET"
  }, {
    id: 5,
    artist: "David Kim",
    track: "Neon Nights",
    time: "31 minutes ago",
    avatar: "DK"
  }, {
    id: 6,
    artist: "Lisa Wang",
    track: "Mountain Echo",
    time: "45 minutes ago",
    avatar: "LW"
  }, {
    id: 7,
    artist: "James Brown",
    track: "Summer Breeze",
    time: "1 hour ago",
    avatar: "JB"
  }, {
    id: 8,
    artist: "Anna Garcia",
    track: "Digital Soul",
    time: "1 hour ago",
    avatar: "AG"
  }]);
  const userGrowthRef = React.useRef(null);
  const uploadsRef = React.useRef(null);
  const userGrowthChart = React.useRef(null);
  const uploadsChart = React.useRef(null);
  React.useEffect(() => {
    // User Growth Chart
    if (userGrowthRef.current) {
      const ctx = userGrowthRef.current.getContext('2d');
      if (userGrowthChart.current) {
        userGrowthChart.current.destroy();
      }
      userGrowthChart.current = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Total Users',
            data: [8500, 9200, 10100, 11800, 13500, 15420],
            borderColor: '#667eea',
            backgroundColor: 'rgba(102, 126, 234, 0.1)',
            borderWidth: 3,
            fill: true,
            tension: 0.4,
            pointBackgroundColor: '#667eea',
            pointBorderColor: '#fff',
            pointBorderWidth: 2,
            pointRadius: 6
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: false,
              grid: {
                color: 'rgba(0,0,0,0.1)'
              }
            },
            x: {
              grid: {
                display: false
              }
            }
          }
        }
      });
    }

    // Music Uploads Chart
    if (uploadsRef.current) {
      const ctx = uploadsRef.current.getContext('2d');
      if (uploadsChart.current) {
        uploadsChart.current.destroy();
      }
      uploadsChart.current = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'This Week'],
          datasets: [{
            label: 'Music Uploads',
            data: [245, 312, 278, 389, 423],
            backgroundColor: 'rgba(118, 75, 162, 0.8)',
            borderColor: '#764ba2',
            borderWidth: 2,
            borderRadius: 8,
            borderSkipped: false
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: 'rgba(0,0,0,0.1)'
              }
            },
            x: {
              grid: {
                display: false
              }
            }
          }
        }
      });
    }
    return () => {
      if (userGrowthChart.current) {
        userGrowthChart.current.destroy();
      }
      if (uploadsChart.current) {
        uploadsChart.current.destroy();
      }
    };
  }, []);
  const formatCurrency = amount => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0
    }).format(amount);
  };
  const formatNumber = num => {
    return new Intl.NumberFormat('en-US').format(num);
  };
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "dashboard-header"
  }, /*#__PURE__*/React.createElement("div", {
    className: "container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-8"
  }, /*#__PURE__*/React.createElement("h1", {
    className: "mb-2"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-music me-3"
  }), "Music Platform Dashboard"), /*#__PURE__*/React.createElement("p", {
    className: "mb-0 ms-2 opacity-75"
  }, "Real-time analytics and insights")), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4 text-end"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center justify-content-end"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-calendar-alt me-2"
  }), /*#__PURE__*/React.createElement("span", null, new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  }))))))), /*#__PURE__*/React.createElement("div", {
    className: "container"
  }, /*#__PURE__*/React.createElement("div", {
    className: "row g-4 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-xxl-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: '#3498db'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-users"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value"
  }, formatNumber(kpiData.totalUsers)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label"
  }, "Total Users")))))), /*#__PURE__*/React.createElement("div", {
    className: "col-xxl-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: '#e74c3c'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-crown"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value"
  }, formatNumber(kpiData.activeSubscriptions)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label"
  }, "Active Subscriptions")))))), /*#__PURE__*/React.createElement("div", {
    className: "col-xxl-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: '#9b59b6'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-music"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value"
  }, formatNumber(kpiData.totalTracks)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label"
  }, "Total Music Tracks")))))), /*#__PURE__*/React.createElement("div", {
    className: "col-xxl-3 col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card revenue-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: 'rgba(255,255,255,0.2)'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-dollar-sign"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value text-white"
  }, formatCurrency(kpiData.monthlyRevenue)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label text-white opacity-75"
  }, "Monthly Revenue"), /*#__PURE__*/React.createElement("div", {
    className: "yearly-revenue mt-2"
  }, /*#__PURE__*/React.createElement("small", {
    className: "text-white opacity-75"
  }, "Yearly Revenue:"), /*#__PURE__*/React.createElement("div", {
    className: "text-white fw-bold"
  }, formatCurrency(kpiData.yearlyRevenue))))))))), /*#__PURE__*/React.createElement("div", {
    className: "row g-4 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: '#f39c12'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-user-plus"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value"
  }, formatNumber(kpiData.todaySignups)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label"
  }, "Today's New Signups")))))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card kpi-card"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body p-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-icon",
    style: {
      backgroundColor: '#1abc9c'
    }
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-upload"
  })), /*#__PURE__*/React.createElement("div", {
    className: "ms-3 flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "kpi-value"
  }, formatNumber(kpiData.todayUploads)), /*#__PURE__*/React.createElement("div", {
    className: "kpi-label"
  }, "Today's Music Uploads"))))))), /*#__PURE__*/React.createElement("div", {
    className: "row g-4 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-lg-8"
  }, /*#__PURE__*/React.createElement("div", {
    className: "chart-container"
  }, /*#__PURE__*/React.createElement("h4", {
    className: "section-title"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-chart-line me-2"
  }), "User Growth Over Time"), /*#__PURE__*/React.createElement("div", {
    style: {
      height: '300px'
    }
  }, /*#__PURE__*/React.createElement("canvas", {
    ref: userGrowthRef
  })))), /*#__PURE__*/React.createElement("div", {
    className: "col-lg-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "chart-container"
  }, /*#__PURE__*/React.createElement("h4", {
    className: "section-title"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-chart-bar me-2"
  }), "Weekly Music Uploads"), /*#__PURE__*/React.createElement("div", {
    style: {
      height: '300px'
    }
  }, /*#__PURE__*/React.createElement("canvas", {
    ref: uploadsRef
  }))))), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-12"
  }, /*#__PURE__*/React.createElement("div", {
    className: "chart-container"
  }, /*#__PURE__*/React.createElement("h4", {
    className: "section-title"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-clock me-2"
  }), "Recent Music Uploads"), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, recentActivity.map(activity => /*#__PURE__*/React.createElement("div", {
    key: activity.id,
    className: "col-lg-6"
  }, /*#__PURE__*/React.createElement("div", {
    className: "activity-item"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("div", {
    className: "activity-avatar me-3"
  }, activity.avatar), /*#__PURE__*/React.createElement("div", {
    className: "flex-grow-1"
  }, /*#__PURE__*/React.createElement("div", {
    className: "fw-bold text-dark mb-1"
  }, activity.track), /*#__PURE__*/React.createElement("div", {
    className: "text-muted small"
  }, "by ", activity.artist)), /*#__PURE__*/React.createElement("div", {
    className: "text-muted small"
  }, /*#__PURE__*/React.createElement("i", {
    className: "fas fa-clock me-1"
  }), activity.time)))))))))));
};