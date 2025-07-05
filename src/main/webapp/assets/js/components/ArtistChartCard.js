export const ArtistChartCard = ({
  baseUrl,
  userId
}) => {
  const [dateType, setDateType] = React.useState("weekly");
  const chartInstance = React.useRef(null);
  const [loading, setLoading] = React.useState(true);
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
  const canvasRef = React.useRef(null);
  React.useEffect(() => {
    const fetchAndRenderChart = async () => {
      try {
        const res = await fetch(`${baseUrl}/api/user/${userId}/chart?type=plays&dateType=${dateType}&start=${start}&end=${end}`);
        const chartJson = await res.json();
        setTimeout(() => {
          if (!canvasRef.current) return;

          // Destroy old chart if exists
          if (chartInstance.current) {
            chartInstance.current.destroy();
          }
          const ctx = canvasRef.current.getContext('2d');
          if (!ctx) {
            console.warn(`Canvas context not found for ${type}`);
            return;
          }
          chartInstance.current = new window.Chart(ctx, {
            type: 'line',
            data: {
              labels: chartJson.data.results.labels,
              datasets: chartJson.data.results.datasets
            },
            options: {
              responsive: true
            }
          });
          setLoading(false);
        }, 50);
      } catch (err) {
        console.error(`Error fetching chart:`, err);
      }
    };
    fetchAndRenderChart();
  }, [baseUrl, start, end, dateType, userId]);
  return /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("div", {
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
    className: "col-md-6 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("h5", {
    className: "card-title"
  }, "Music Plays Chart"), loading && /*#__PURE__*/React.createElement("div", {
    className: "text-center my-5"
  }, /*#__PURE__*/React.createElement("div", {
    className: "spinner-border text-primary",
    role: "status"
  }, /*#__PURE__*/React.createElement("span", {
    className: "visually-hidden"
  }, "Loading..."))), /*#__PURE__*/React.createElement("canvas", {
    ref: canvasRef
  }))))));
};