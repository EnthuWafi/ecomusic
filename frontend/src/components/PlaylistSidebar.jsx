const PlaylistSidebar = ({ userId, baseUrl }) => {
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

	if (loading) return <p className="text-muted">Loading playlists...</p>;
	if (error) return <p className="text-danger">{error}</p>;
	if (playlists.length === 0) return <p className="text-muted">No playlists found.</p>;

	return (
		<div>
			<h6 className="text-uppercase">Playlists</h6>
			<ul className="nav flex-column">
				{playlists.map((playlist) => (
					<li className="nav-item" key={playlist.playlistId}>
						<a className="nav-link text-white px-0" href={`${baseUrl}/user/playlist/${playlist.playlistId}`}>
							{playlist.name}
						</a>
					</li>
				))}
			</ul>
		</div>
	);
};
