import { MusicPlayer } from "./MusicPlayer.js";



export const PlaylistPlayer = ({ baseUrl, playlistId, isAdmin = false, userId }) => {
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

	if (loading) return (<div className="text-center mb-4">
		<div className="spinner-border text-primary" role="status">
			<span className="visually-hidden">Loading...</span>
		</div>
		<p className="mt-2 text-muted">Loading music...</p>
	</div>);
	if (!playlistData) return <div className="text-danger text-center mt-5">Failed to load playlist.</div>;

	return (
		<div className="container mt-4">
			<h2 className="mb-4"><a class="text-decoration-none text-white" href={`${baseUrl}/user/playlist/${playlistId}`}>{playlistData.name}</a></h2>
			<div className="row">
				{/* Music Player Column */}
				<div className="col-md-8">
					{currentTrack ? (
						<MusicPlayer baseUrl={baseUrl} musicId={currentTrack.musicId} isAdmin={isAdmin} userId={userId} />
					) : (
						<p>No track selected.</p>
					)}
				</div>

				{/* Playlist Column */}
				<div className="col-md-4">
					<ul className="list-group">
						{playlistData.musicList.map((entry, index) => {
							const music = entry.music;
							return (
								<li
									key={music.musicId}
									className={`list-group-item d-flex justify-content-between align-items-center ${currentTrack?.musicId === music.musicId ? 'active' : ''}`}
									style={{ cursor: "pointer" }}
									onClick={() => setCurrentTrack(music)}
								>
									<div>
										<strong>{music.title}</strong> <br />
										<small className="text-muted">by {entry.artistUsername}</small>
									</div>
									<img src={`${baseUrl}/stream/image/music/${music.musicId}?size=thumb`} alt={music.title} width="50" height="50" className="rounded" />
								</li>
							);
						})}
					</ul>
				</div>
			</div>
		</div>
	);
};

export default PlaylistPlayer;