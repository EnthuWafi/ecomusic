import KpiCard from "./KpiCard.js";
export const AdminMusicList = ({
  baseUrl
}) => {
  const [kpiData, setKpiData] = React.useState(null);
  const [music, setMusic] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  // Pagination state
  const [currentPage, setCurrentPage] = React.useState(1);
  const [totalMusic, setTotalMusic] = React.useState(0);
  const [musicPerPage] = React.useState(10); // You can make this configurable

  // Calculate pagination values
  const totalPages = Math.ceil(totalMusic / musicPerPage);
  const offset = (currentPage - 1) * musicPerPage;
  const fetchMusic = async (page = 1) => {
    try {
      setLoading(true);
      const pageOffset = (page - 1) * musicPerPage;
      const musicRes = await fetch(`${baseUrl}/api/music?limit=${musicPerPage}&offset=${pageOffset}`);
      const musicJson = await musicRes.json();
      setMusic(musicJson.data.results);
    } catch (err) {
      console.error("Error loading music:", err);
      setError("Failed to load music.");
    } finally {
      setLoading(false);
    }
  };
  React.useEffect(() => {
    const fetchData = async () => {
      try {
        const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=music`);
        const kpiJson = await kpiRes.json();
        setKpiData(kpiJson.data.results);
        setTotalMusic(kpiJson.data.results.totalMusicCount);
      } catch (err) {
        console.error("Error loading KPI data:", err);
        setError("Failed to load KPI data.");
      }
    };
    fetchData();
  }, [baseUrl]);
  React.useEffect(() => {
    fetchMusic(currentPage);
  }, [baseUrl, currentPage, musicPerPage]);
  const handlePageChange = page => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };
  const handleDelete = async musicId => {
    if (!confirm("Are you sure you want to delete this music?")) return;
    try {
      const res = await fetch(`${baseUrl}/api/music/${musicId}`, {
        method: "DELETE"
      });
      if (!res.ok) throw new Error("Delete failed");

      // Refresh the current page after deletion
      await fetchMusic(currentPage);
      toastr.success("Music deleted.");
    } catch (err) {
      console.error("Error deleting music:", err);
      toastr.error("Failed to delete music.");
    }
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
      "aria-label": "Music pagination"
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
  }, "Music Management"), kpiData && /*#__PURE__*/React.createElement("div", {
    className: "row mb-4"
  }, /*#__PURE__*/React.createElement(KpiCard, {
    title: "Total Music",
    value: kpiData.totalMusicCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Public Music",
    value: kpiData.publicMusicCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Non-Premium Music",
    value: kpiData.nonPremiumCount
  }), /*#__PURE__*/React.createElement(KpiCard, {
    title: "Premium Music",
    value: kpiData.premiumCount
  })), /*#__PURE__*/React.createElement("div", {
    className: "card shadow-sm"
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-between align-items-center mb-3"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title mb-0"
  }, "Music Tracks"), /*#__PURE__*/React.createElement("div", {
    className: "d-flex align-items-center gap-3"
  }, /*#__PURE__*/React.createElement("small", {
    className: "text-muted"
  }, "Showing ", offset + 1, "-", Math.min(offset + musicPerPage, totalMusic), " of ", totalMusic, " tracks"))), /*#__PURE__*/React.createElement("div", {
    className: "table-responsive"
  }, /*#__PURE__*/React.createElement("table", {
    className: "table table-hover"
  }, /*#__PURE__*/React.createElement("thead", null, /*#__PURE__*/React.createElement("tr", null, /*#__PURE__*/React.createElement("th", null, "Track"), /*#__PURE__*/React.createElement("th", null, "Play Count"), /*#__PURE__*/React.createElement("th", null, "Like Count"), /*#__PURE__*/React.createElement("th", null, "Status"), /*#__PURE__*/React.createElement("th", null, "Premium"), /*#__PURE__*/React.createElement("th", null, "Actions"))), /*#__PURE__*/React.createElement("tbody", null, music.map(track => /*#__PURE__*/React.createElement("tr", {
    key: track.musicId
  }, /*#__PURE__*/React.createElement("td", {
    className: "d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/music/${track.musicId}?size=thumb`,
    alt: "Album Art",
    style: {
      width: "40px",
      height: "40px",
      objectFit: "cover",
      borderRadius: "4px",
      marginRight: "10px"
    }
  }), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    className: "fw-medium"
  }, track.title))), /*#__PURE__*/React.createElement("td", null, track.totalPlayCount), /*#__PURE__*/React.createElement("td", null, track.likeCount), /*#__PURE__*/React.createElement("td", null, track.visibility === 'PUBLIC' ? 'Public' : 'Private'), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("span", {
    className: `badge ${track.premiumContent ? 'bg-warning' : 'bg-info'}`
  }, track.isPremium ? 'Premium' : 'Free')), /*#__PURE__*/React.createElement("td", null, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-danger",
    onClick: () => handleDelete(track.musicId)
  }, "Delete"))))))), /*#__PURE__*/React.createElement("div", {
    className: "d-flex justify-content-center mt-3"
  }, renderPagination()))));
};