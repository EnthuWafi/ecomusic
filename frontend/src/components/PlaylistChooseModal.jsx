

export const PlaylistChooseModal = ({ baseUrl, musicId, userId, onClose, onPlaylistSelected }) => {
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

	const handleSelect = async (playlist) => {
		try {
			const res = await fetch(`${baseUrl}/api/playlist/${playlist.playlistId}/music`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ musicId }),
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


	return (
		<div
			className="modal fade show"
			tabIndex="-1"
			style={{ display: "block", backgroundColor: "rgba(0,0,0,0.5)" }}
			aria-modal="true"
			role="dialog"
		>
			<div className="modal-dialog modal-dialog-centered">
				<div className="modal-content">
					<div className="modal-header">
						<h5 className="modal-title">Choose a Playlist</h5>
						<button type="button" className="btn-close" aria-label="Close" onClick={onClose} />
					</div>
					<div className="modal-body">
						{loading && <p>Loading playlists...</p>}
						{!loading && playlistChoice.length === 0 && <p>No playlists found.</p>}
						{!loading && playlistChoice.length > 0 && (
							<ul className="list-group">
								{playlistChoice.map((playlist) => (
									<li
										key={playlist.playlistId}
										className="list-group-item list-group-item-action"
										style={{ cursor: "pointer" }}
										onClick={() => handleSelect(playlist)}
									>
										{playlist.name}
									</li>
								))}
							</ul>
						)}
					</div>
					<div className="modal-footer">
						<button type="button" className="btn btn-secondary" onClick={onClose}>
							Cancel
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};

export default PlaylistChooseModal;