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
export default KpiCard;