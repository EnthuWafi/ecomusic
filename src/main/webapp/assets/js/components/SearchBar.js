export const SearchBar = ({
  baseUrl
}) => {
  const [searchTerm, setSearchTerm] = React.useState('');
  const [suggestions, setSuggestions] = React.useState([]);
  const [showDropdown, setShowDropdown] = React.useState(false);
  React.useEffect(() => {
    const timeoutId = setTimeout(async () => {
      if (searchTerm.length > 2) {
        try {
          const response = await fetch(`${baseUrl}/api/music/search?q=${searchTerm}`);
          const data = await response.json();
          if (data.success && data.data.results) {
            setSuggestions(data.data.results);
            setShowDropdown(true);
          }
        } catch (error) {
          console.error('Search failed:', error);
          setSuggestions([]);
          setShowDropdown(false);
        }
      } else {
        setSuggestions([]);
        setShowDropdown(false);
      }
    }, 300);
    return () => clearTimeout(timeoutId);
  }, [searchTerm, baseUrl]);
  React.useEffect(() => {
    const evt = new CustomEvent('globalSearchTerm', {
      detail: searchTerm
    });
    window.dispatchEvent(evt);
  }, [searchTerm]);
  return /*#__PURE__*/React.createElement("div", {
    className: "position-relative"
  }, /*#__PURE__*/React.createElement("form", {
    action: `${baseUrl}/music/search`
  }, /*#__PURE__*/React.createElement("input", {
    name: "q",
    autoComplete: "off",
    type: "text",
    className: "form-control rounded-pill bg-secondary border-0 text-white",
    placeholder: "Search here...",
    value: searchTerm,
    onChange: e => setSearchTerm(e.target.value)
  }), showDropdown && suggestions.length > 0 && /*#__PURE__*/React.createElement("div", {
    className: "dropdown-menu  dropdown-menu-dark show position-absolute w-100"
  }, suggestions.map((song, index) => /*#__PURE__*/React.createElement("div", {
    key: index,
    className: "dropdown-item small",
    style: {
      cursor: 'pointer'
    },
    onClick: () => window.location.href = `${baseUrl}/music/search?q=${song.title}`
  }, song.title)))));
};
export default SearchBar;