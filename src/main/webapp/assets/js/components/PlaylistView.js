export const PlaylistView = ({
  playlist,
  baseUrl = ''
}) => {
  const [songs, setSongs] = React.useState(playlist.musicList || []);
  const [draggedIdx, setDraggedIdx] = React.useState(null);
  const listRef = React.useRef(null);
  const fmtDate = iso => new Date(iso).toLocaleDateString(undefined, {
    month: 'short',
    day: '2-digit',
    year: 'numeric'
  });
  const refreshPositions = arr => arr.map((item, i) => ({
    ...item,
    position: i + 1
  }));

  // Send PUT on reorder
  const updateSongPosition = async (musicId, newPos) => {
    try {
      const res = await fetch(`${baseUrl}/api/playlist/${playlist.playlistId}/music/${musicId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          position: newPos
        })
      });
      if (!res.ok) throw new Error(await res.text());
    } catch (e) {
      console.error('Reorder failed:', e);
      alert('Failed to update song order. Reloading.');
      window.location.reload();
    }
  };

  // Send DELETE on removal
  const removeSong = async musicId => {
    if (!window.confirm('Remove this song from the playlist?')) return;
    try {
      const res = await fetch(`${baseUrl}/api/playlist/${playlist.playlistId}/music/${musicId}`, {
        method: 'DELETE'
      });
      if (!res.ok) throw new Error(await res.text());
      const filtered = songs.filter(s => s.musicId !== musicId);
      setSongs(refreshPositions(filtered));
    } catch (e) {
      console.error('Remove failed:', e);
      alert('Failed to remove song.');
    }
  };

  // Drag handlers
  const handleDragStart = (e, idx) => {
    setDraggedIdx(idx);
    e.dataTransfer.effectAllowed = 'move';
  };
  const handleDragOver = e => {
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';
  };
  const handleDrop = (e, idx) => {
    e.preventDefault();
    if (draggedIdx === null || draggedIdx === idx) return;
    const updated = [...songs];
    const [moved] = updated.splice(draggedIdx, 1);
    updated.splice(idx, 0, moved);
    const withPos = refreshPositions(updated);
    setSongs(withPos);
    updateSongPosition(moved.musicId, idx + 1);
    setDraggedIdx(null);
  };
  return /*#__PURE__*/React.createElement("div", {
    className: "playlist-container container my-5",
    "data-playlist-id": playlist.playlistId
  }, /*#__PURE__*/React.createElement("div", {
    className: "playlist-header d-flex flex-wrap align-items-end gap-4 mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "playlist-cover"
  }, songs[0]?.music?.imageUrl ? /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/music/${songs[0].musicId}?size=thumb`,
    alt: playlist.name,
    className: "rounded",
    style: {
      width: 200,
      height: 200,
      objectFit: 'cover'
    }
  }) : /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/music/0`,
    alt: playlist.name,
    className: "rounded",
    style: {
      width: 200,
      height: 200,
      objectFit: 'cover'
    }
  })), /*#__PURE__*/React.createElement("div", {
    className: "playlist-info"
  }, /*#__PURE__*/React.createElement("h1", {
    className: "fw-bold"
  }, /*#__PURE__*/React.createElement("a", {
    className: "text-decoration-none text-white",
    href: `${baseUrl}/playlist/play/${playlist.playlistId}`
  }, playlist.name)), /*#__PURE__*/React.createElement("div", {
    className: "playlist-meta text-secondary small d-flex flex-wrap gap-2"
  }, /*#__PURE__*/React.createElement("span", null, playlist.visibility === 'public' ? 'Public' : 'Private'), /*#__PURE__*/React.createElement("span", null, "\u2022"), /*#__PURE__*/React.createElement("span", null, songs.length, " songs"), playlist.createdAtDate && /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("span", null, "\u2022"), /*#__PURE__*/React.createElement("span", null, fmtDate(playlist.createdAtDate)))))), songs.length === 0 ? /*#__PURE__*/React.createElement("div", {
    className: "empty-playlist text-center py-5 text-muted"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-music-note-list display-3 mb-3"
  }), /*#__PURE__*/React.createElement("h3", null, "No songs in this playlist")) : /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("div", {
    className: "row align-items-center text-secondary small fw-bold border-bottom py-2 px-3 d-none d-md-flex"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-auto pe-0"
  }), /*#__PURE__*/React.createElement("div", {
    className: "col-auto"
  }, "#"), /*#__PURE__*/React.createElement("div", {
    className: "col"
  }, "Title"), /*#__PURE__*/React.createElement("div", {
    className: "col-md-2"
  }, "Artist"), /*#__PURE__*/React.createElement("div", {
    className: "col-md-2"
  }, "Likes / Plays"), /*#__PURE__*/React.createElement("div", {
    className: "col-auto"
  }, "Action")), /*#__PURE__*/React.createElement("div", {
    ref: listRef
  }, songs.map((item, idx) => /*#__PURE__*/React.createElement("div", {
    key: item.musicId,
    className: `row align-items-center border-bottom py-2 px-3 ${draggedIdx === idx ? 'bg-light opacity-75' : ''}`,
    draggable: true,
    onDragStart: e => handleDragStart(e, idx),
    onDragOver: handleDragOver,
    onDrop: e => handleDrop(e, idx)
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-auto pe-0"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-grip-vertical text-secondary"
  })), /*#__PURE__*/React.createElement("div", {
    className: "col-auto pe-3 text-secondary"
  }, idx + 1), /*#__PURE__*/React.createElement("div", {
    className: "col d-flex align-items-center"
  }, /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/image/music/${item.musicId}?size=thumb`,
    alt: "",
    className: "rounded flex-shrink-0 me-2",
    style: {
      width: 40,
      height: 40,
      objectFit: 'cover'
    }
  }), /*#__PURE__*/React.createElement("div", {
    className: "text-truncate"
  }, /*#__PURE__*/React.createElement("div", {
    className: "fw-medium text-truncate"
  }, item.music?.title || '[Unknown]'), item.music?.premiumContent && /*#__PURE__*/React.createElement("small", {
    className: "text-warning"
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-crown-fill me-1"
  }), " Premium"))), /*#__PURE__*/React.createElement("div", {
    className: "col-md-2 d-none d-md-block text-truncate text-secondary"
  }, item.artistUsername || '[Unknown]'), /*#__PURE__*/React.createElement("div", {
    className: "col-md-2 d-none d-md-flex flex-column small text-secondary"
  }, /*#__PURE__*/React.createElement("span", null, item.music?.likeCount ?? '--', " ", /*#__PURE__*/React.createElement("i", {
    className: "bi bi-heart-fill text-danger"
  })), /*#__PURE__*/React.createElement("span", null, item.music?.totalPlayCount ?? '--', " ", /*#__PURE__*/React.createElement("i", {
    className: "bi bi-play-fill text-primary"
  }))), /*#__PURE__*/React.createElement("div", {
    className: "col-auto text-end"
  }, /*#__PURE__*/React.createElement("button", {
    className: "btn btn-sm btn-outline-danger",
    onClick: () => removeSong(item.musicId)
  }, /*#__PURE__*/React.createElement("i", {
    className: "bi bi-trash-fill"
  }))))))));
};