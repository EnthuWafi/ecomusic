const MusicGrid = ({
  title,
  apiEndpoint,
  baseUrl
}) => {
  const [musics, setMusics] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  React.useEffect(() => {
    const fetchMusics = async () => {
      try {
        const response = await fetch(`${baseUrl}${apiEndpoint}`);
        const data = await response.json();
        if (data.success && data.data.results) {
          setMusics(data.data.results);
        }
      } catch (error) {
        console.error('Failed to fetch musics:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchMusics();
  }, [baseUrl, apiEndpoint]);
  if (loading) {
    return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", {
      className: "text-white"
    }, title), /*#__PURE__*/React.createElement("div", {
      className: "text-white"
    }, "Loading..."));
  }
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("h4", {
    className: "text-white"
  }, title), /*#__PURE__*/React.createElement("div", {
    className: "d-flex overflow-auto mb-5",
    style: {
      gap: '1rem',
      paddingBottom: '0.5rem'
    }
  }, musics.map((music, index) => /*#__PURE__*/React.createElement("div", {
    key: index,
    className: "card bg-secondary text-white",
    style: {
      width: '180px',
      minWidth: '180px',
      flex: '0 0 auto'
    }
  }, /*#__PURE__*/React.createElement("div", {
    className: "card-img-top bg-secondary",
    style: {
      height: '150px'
    }
  }, music.imageUrl && /*#__PURE__*/React.createElement("img", {
    src: `${baseUrl}/stream/music/image/${music.musicId}?size=thumb`,
    alt: music.name,
    className: "w-100 h-100",
    style: {
      objectFit: 'cover'
    }
  })), /*#__PURE__*/React.createElement("div", {
    className: "card-body"
  }, /*#__PURE__*/React.createElement("h5", {
    className: "card-title text-truncate"
  }, music.title || 'Music Name'), /*#__PURE__*/React.createElement("p", {
    className: "card-text text-muted text-truncate"
  }, music.artistUsername || 'Artist'))))));
};