import KpiCard from "./KpiCard.js";
import ChartCard from "./ChartCard.js";
import TopMusicList from "./TopMusicList.js";
export const AdminDashboard = ({
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
    baseUrl: baseUrl,
    dateType: dateType,
    start: start,
    end: end,
    type: "plays",
    title: "User Plays",
    canvasRef: playsRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    baseUrl: baseUrl,
    dateType: dateType,
    start: start,
    end: end,
    type: "music_uploads",
    title: "Music Uploads",
    canvasRef: uploadsRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    baseUrl: baseUrl,
    dateType: dateType,
    start: start,
    end: end,
    type: "revenue",
    title: "Revenue",
    canvasRef: revenueRef
  }), /*#__PURE__*/React.createElement(ChartCard, {
    baseUrl: baseUrl,
    dateType: dateType,
    start: start,
    end: end,
    type: "user_growth",
    title: "User Registrations",
    canvasRef: usersRef
  }))));
};