import { MusicPlayer } from "./MusicPlayer.js";
export const PlaylistPlayer = ({
  baseUrl,
  playlistId,
  isAdmin = false,
  userId
}) => {
  const [playlistData, setPlaylistData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [currentTrack, setCurrentTrack] = React.useState(null);
  React.useEffect(() => {
    const fetchPlaylist = async () => {
      try {
        const response = await fetch(`${baseUrl}/api/playlist/${playlistId}?music=true`);
        if (!response.ok) throw new Error("Failed to fetch playlist.");
        const json = await response.json();
        const playlist = json.data?.data;
        setPlaylistData(playlist);
        if (playlist?.musicList?.length > 0) {
          setCurrentTrack(playlist.musicList[0].music);
        }
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
    fetchPlaylist();
  }, [baseUrl, playlistId]);
  if (loading) return /*#__PURE__*/React.createElement("div", {
    className: "text-center mb-4"
  }, /*#__PURE__*/React.createElement("div", {
    className: "spinner-border text-primary",
    role: "status"
  }, /*#__PURE__*/React.createElement("span", {
    className: "visually-hidden"
  }, "Loading...")), /*#__PURE__*/React.createElement("p", {
    className: "mt-2 text-muted"
  }, "Loading music..."));
  if (!playlistData) return /*#__PURE__*/React.createElement("div", {
    className: "text-danger text-center mt-5"
  }, "Failed to load playlist.");
  return /*#__PURE__*/React.createElement("div", {
    className: "container mt-4"
  }, /*#__PURE__*/React.createElement("h2", {
    className: "mb-4"
  }, playlistData.name), /*#__PURE__*/React.createElement("div", {
    className: "row"
  }, /*#__PURE__*/React.createElement("div", {
    className: "col-md-8"
  }, currentTrack ? /*#__PURE__*/React.createElement(MusicPlayer, {
    baseUrl: baseUrl,
    musicId: currentTrack.musicId,
    isAdmin: isAdmin,
    userId: userId
  }) : /*#__PURE__*/React.createElement("p", null, "No track selected.")), /*#__PURE__*/React.createElement("div", {
    className: "col-md-4"
  }, /*#__PURE__*/React.createElement("ul", {
    className: "list-group"
  }, playlistData.musicList.map((entry, index) => {
    const music = entry.music;
    return /*#__PURE__*/React.createElement("li", {
      key: music.musicId,
      className: `list-group-item d-flex justify-content-between align-items-center ${currentTrack?.musicId === music.musicId ? 'active' : ''}`,
      style: {
        cursor: "pointer"
      },
      onClick: () => setCurrentTrack(music)
    }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("strong", null, music.title), " ", /*#__PURE__*/React.createElement("br", null), /*#__PURE__*/React.createElement("small", {
      className: "text-muted"
    }, "by ", entry.artistUsername)), /*#__PURE__*/React.createElement("img", {
      src: `${baseUrl}/stream/image/music/${music.musicId}?size=thumb`,
      alt: music.title,
      width: "50",
      height: "50",
      className: "rounded"
    }));
  })))));
};
export default PlaylistPlayer;