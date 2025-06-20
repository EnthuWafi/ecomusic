const PlaylistSidebar = ({
  userId,
  baseUrl
}) => {
  const [playlists, setPlaylists] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  React.useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const res = await fetch(`${baseUrl}/api/playlist?userId=${userId}`);
        const data = await res.json();
        if (data.success && data.data?.results) {
          setPlaylists(data.data.results);
        } else {
          setError('Failed to load playlists.');
        }
      } catch (err) {
        console.error(err);
        setError('An error occurred while fetching playlists.');
      } finally {
        setLoading(false);
      }
    };
    if (userId) {
      fetchPlaylists();
    }
  }, [userId, baseUrl]);
  if (loading) return /*#__PURE__*/React.createElement("p", {
    className: "text-muted"
  }, "Loading playlists...");
  if (error) return /*#__PURE__*/React.createElement("p", {
    className: "text-danger"
  }, error);
  if (playlists.length === 0) return /*#__PURE__*/React.createElement("p", {
    className: "text-muted"
  }, "No playlists found.");
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h6", {
    className: "text-uppercase"
  }, "Playlists"), /*#__PURE__*/React.createElement("ul", {
    className: "nav flex-column"
  }, playlists.map(playlist => /*#__PURE__*/React.createElement("li", {
    className: "nav-item",
    key: playlist.playlistId
  }, /*#__PURE__*/React.createElement("a", {
    className: "nav-link text-white px-0",
    href: `${baseUrl}/user/playlist/${playlist.playlistId}`
  }, playlist.name)))));
};