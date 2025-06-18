const SearchBar = ({ baseUrl }) => {
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

  return (
    <div className="mb-3 position-relative">
      <input
        type="text"
        className="form-control form-control-sm rounded-pill bg-secondary border-0 text-white"
        placeholder="Search here..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      
      {showDropdown && suggestions.length > 0 && (
        <div className="dropdown-menu show position-absolute w-100">
          {suggestions.map((song, index) => (
            <div key={index} className="dropdown-item">
              {song.title}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};