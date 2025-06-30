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
export default TopMusicList;