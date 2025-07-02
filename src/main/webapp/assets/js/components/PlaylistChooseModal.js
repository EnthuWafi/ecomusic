export const PlaylistChooseModal = ({
  baseUrl,
  musicId,
  userId,
  onClose,
  onPlaylistSelected
}) => {
  const [playlistChoice, setPlaylistChoice] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  React.useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const res = await fetch(`${baseUrl}/api/playlist?userId=${userId}`);
        const data = await res.json();
        if (data.success) {
          setPlaylistChoice(data.data.results);
        } else {
          toastr.error("Failed to fetch playlists.");
        }
      } catch (err) {
        toastr.error("Failed to fetch playlists.");
      } finally {
        setLoading(false);
      }
    };
    fetchPlaylists();
  }, [baseUrl, userId]);
  const handleSelect = async playlist => {
    try {
      const res = await fetch(`${baseUrl}/api/playlist/${playlist.playlistId}/music`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          musicId
        })
      });
      const result = await res.json();
      if (result.success) {
        toastr.success(`Added to playlist "${playlist.name}"`);
        if (onPlaylistSelected) onPlaylistSelected(playlist);
        if (onClose) onClose();
      } else {
        toastr.error("Failed to add music to playlist.");
      }
    } catch (err) {
      toastr.error("Failed to add music to playlist.");
    }
  };
  return /*#__PURE__*/React.createElement("div", {
    className: "modal fade show",
    tabIndex: "-1",
    style: {
      display: "block",
      backgroundColor: "rgba(0,0,0,0.5)"
    },
    "aria-modal": "true",
    role: "dialog"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-dialog modal-dialog-centered"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-content"
  }, /*#__PURE__*/React.createElement("div", {
    className: "modal-header"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "modal-title"
  }, "Choose a Playlist"), /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn-close",
    "aria-label": "Close",
    onClick: onClose
  })), /*#__PURE__*/React.createElement("div", {
    className: "modal-body"
  }, loading && /*#__PURE__*/React.createElement("p", null, "Loading playlists..."), !loading && playlistChoice.length === 0 && /*#__PURE__*/React.createElement("p", null, "No playlists found."), !loading && playlistChoice.length > 0 && /*#__PURE__*/React.createElement("ul", {
    className: "list-group"
  }, playlistChoice.map(playlist => /*#__PURE__*/React.createElement("li", {
    key: playlist.playlistId,
    className: "list-group-item list-group-item-action",
    style: {
      cursor: "pointer"
    },
    onClick: () => handleSelect(playlist)
  }, playlist.name)))), /*#__PURE__*/React.createElement("div", {
    className: "modal-footer"
  }, /*#__PURE__*/React.createElement("button", {
    type: "button",
    className: "btn btn-secondary",
    onClick: onClose
  }, "Cancel")))));
};
export default PlaylistChooseModal;