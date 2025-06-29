const KpiCard = ({
  title,
  value
}) => /*#__PURE__*/React.createElement("div", {
  className: "col-md-4 mb-3"
}, /*#__PURE__*/React.createElement("div", {
  className: "card text-white bg-primary h-100 shadow-sm"
}, /*#__PURE__*/React.createElement("div", {
  className: "card-body"
}, /*#__PURE__*/React.createElement("h5", {
  className: "card-title"
}, title), /*#__PURE__*/React.createElement("p", {
  className: "card-text fs-4 fw-bold"
}, value))));
const TopMusicList = ({
  items
}) => {
  const formatCount = count => {
    return new Intl.NumberFormat('en', {
      notation: "compact",
      compactDisplay: "short",
      maximumFractionDigits: 1
    }).format(count);
  };
  return /*#__PURE__*/React.createElement("div", {
    className: "col-md-6 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm h-100"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title"
  }, "Top 5 Music"), /*#__PURE__*/React.createElement("ul", {
    className: "list-group list-group-flush"
  }, items.map((track, idx) => /*#__PURE__*/React.createElement("li", {
    key: idx,
    className: "list-group-item d-flex justify-content-between align-items-center"
  }, /*#__PURE__*/React.createElement("span", null, track.title), /*#__PURE__*/React.createElement("span", {
    className: "badge bg-primary rounded-pill"
  }, formatCount(track.totalPlayCount), " plays")))))));
};
const ChartCard = ({
  title,
  canvasRef
}) => /*#__PURE__*/React.createElement("div", {
  className: "col-md-6 mb-4"
}, /*#__PURE__*/React.createElement("div", {
  className: "card shadow-sm"
}, /*#__PURE__*/React.createElement("div", {
  className: "card-body"
}, /*#__PURE__*/React.createElement("h5", {
  className: "card-title"
}, title), /*#__PURE__*/React.createElement("canvas", {
  ref: canvasRef
}))));
const AdminDashboard = ({
  baseUrl
}) => {
  const [kpiData, setKpiData] = React.useState(null);
  const [topMusic, setTopMusic] = React.useState([]);
  const [dateType, setDateType] = React.useState("daily");
  const [loading, setLoading] = React.useState(true);
  const playsRef = React.useRef(null);
  const uploadsRef = React.useRef(null);
  const revenueRef = React.useRef(null);
  const usersRef = React.useRef(null);
  const chartInstances = React.useRef({});
  const getDateRange = type => {
    const end = new Date();
    const start = new Date();
    switch (type) {
      case "daily":
        start.setDate(end.getDate() - 6);
        break;
      case "weekly":
        start.setDate(end.getDate() - 7 * 6);
        break;
      case "monthly":
        start.setMonth(end.getMonth() - 5);
        break;
      case "yearly":
        start.setFullYear(end.getFullYear() - 4);
        break;
      default:
        start.setDate(end.getDate() - 30);
    }
    return {
      start: start.toISOString().slice(0, 10),
      end: end.toISOString().slice(0, 10)
    };
  };
  const {
    start,
    end
  } = getDateRange(dateType);
  React.useEffect(() => {
    const fetchKpisAndTopMusic = async () => {
      try {
        const [kpiRes, musicRes] = await Promise.all([fetch(`${baseUrl}/api/report/kpis`), fetch(`${baseUrl}/api/music?sort=top&limit=5`)]);
        const kpiJson = await kpiRes.json();
        const musicJson = await musicRes.json();
        setKpiData(kpiJson.data.results);
        setTopMusic(musicJson.data.results);
      } catch (err) {
        console.error("Error fetching KPIs or Top Music:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchKpisAndTopMusic();
  }, [baseUrl]);
  React.useEffect(() => {
    const fetchAndRenderChart = async (type, ref) => {
      try {
        const res = await fetch(`${baseUrl}/api/report/chart?type=${type}&dateType=${dateType}&start=${start}&end=${end}`);
        const chartJson = await res.json();
        setTimeout(() => {
          if (!ref.current) return;

          // Destroy old chart if exists
          if (chartInstances.current[type]) {
            chartInstances.current[type].destroy();
          }
          const ctx = ref.current.getContext('2d');
          if (!ctx) {
            console.warn(`Canvas context not found for ${type}`);
            return;
          }
          chartInstances.current[type] = new window.Chart(ctx, {
            type: 'line',
            data: {
              labels: chartJson.data.results.labels,
              datasets: chartJson.data.results.datasets
            },
            options: {
              responsive: true
            }
          });
        }, 50);
      } catch (err) {
        console.error(`Error fetching chart ${type}:`, err);
      }
    };
    fetchAndRenderChart('plays', playsRef);
    fetchAndRenderChart('music_uploads', uploadsRef);
    fetchAndRenderChart('revenue', revenueRef);
    fetchAndRenderChart('user_growth', usersRef);
  }, [dateType, baseUrl]);
  const formatToRM = amount => {
    if (amount === null || amount === undefined || isNaN(amount)) {
      return 'RM 0.00';
    }
    const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
    return new Intl.NumberFormat('ms-MY', {
      style: 'currency',
      currency: 'MYR',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(numAmount);
  };
  return /*#__PURE__*/React.createElement("div", {
    className: "container mt-5"
  }, /*#__PURE__*/React.createElement("h2", {
    className: "mb-4"
  }, "Admin Dashboard"), loading ? /*#__PURE__*/React.createElement("div", {
    className: "text-center my-5"
  }, /*#__PURE__*/React.createElement("div", {
    className: "spinner-border text-primary",
    role: "status"
  }, /*#__PURE__*/React.createElement("span", {
    className: "visually-hidden"
  }, "Loading..."))) : /*#__PURE__*/React.createElement(React.Fragment, null, kpiData && /*#__PURE__*/React.createElement("div", {
    className: "row mb-4"
  }, /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Users",
    value: kpiData.userCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Public Music",
    value: kpiData.musicCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Active Subscriptions",
    value: kpiData.activeSubscriptionCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Revenue",
    value: formatToRM(kpiData.revenueAmount)
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Users Today",
    value: kpiData.registeredUsersToday
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Uploads Today",
    value: kpiData.musicUploadedToday
  })), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement(TopMusicList, {
    items: topMusic
  })), /*#__PURE__*/React.createElement("div", {
    className: "mb-4"
  }, /*#__PURE__*/React.createElement("select", {
    className: "form-select w-auto",
    value: dateType,
    onChange: e => setDateType(e.target.value)
  }, /*#__PURE__*/React.createElement("option", {
    value: "daily"
  }, "Daily"), /*#__PURE__*/React.createElement("option", {
    value: "weekly"
  }, "Weekly"), /*#__PURE__*/React.createElement("option", {
    value: "monthly"
  }, "Monthly"), /*#__PURE__*/React.createElement("option", {
    value: "yearly"
  }, "Yearly"))), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement(ChartCard, {
    title: "User Plays",
    canvasRef: playsRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    title: "Music Uploads",
    canvasRef: uploadsRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    title: "Revenue",
    canvasRef: revenueRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    title: "User Registrations",
    canvasRef: usersRef
  }))));
};