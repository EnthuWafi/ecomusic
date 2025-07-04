
export const PlaylistView = ({ playlist, baseUrl = '' }) => {
	const [songs, setSongs] = React.useState(playlist.musicList || []);
	const [draggedIdx, setDraggedIdx] = React.useState(null);
	const listRef = React.useRef(null);

	const fmtDate = iso =>
		new Date(iso).toLocaleDateString(undefined, {
			month: 'short',
			day: '2-digit',
			year: 'numeric',
		});

	const refreshPositions = arr =>
		arr.map((item, i) => ({ ...item, position: i + 1 }));

	// Send PUT on reorder
	const updateSongPosition = async (musicId, newPos) => {
		try {
			const res = await fetch(
				`${baseUrl}/api/playlist/${playlist.playlistId}/music/${musicId}`,
				{
					method: 'PUT',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify({ position: newPos }),
				}
			);
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
			const res = await fetch(
				`${baseUrl}/api/playlist/${playlist.playlistId}/music/${musicId}`,
				{ method: 'DELETE' }
			);
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

	return (
		<div className="playlist-container container my-5" data-playlist-id={playlist.playlistId}>
			{/* Header */}
			<div className="playlist-header d-flex flex-wrap align-items-end gap-4 mb-4">
				<div className="playlist-cover">
					{songs[0]?.music?.imageUrl ? (
						<img
							src={`${baseUrl}/stream/image/music/${songs[0].musicId}?size=thumb`}
							alt={playlist.name}
							className="rounded"
							style={{ width: 200, height: 200, objectFit: 'cover' }}
						/>
					) : (
						<img
							src={`${baseUrl}/stream/image/music/0`}
							alt={playlist.name}
							className="rounded"
							style={{ width: 200, height: 200, objectFit: 'cover' }}
						/>
					)}
				</div>
				<div className="playlist-info">
					<h1 className="fw-bold"><a className="text-decoration-none text-white" href={`${baseUrl}/playlist/play/${playlist.playlistId}`}>{playlist.name}</a></h1>
					<div className="playlist-meta text-secondary small d-flex flex-wrap gap-2">
						<span>{playlist.visibility === 'public' ? 'Public' : 'Private'}</span>
						<span>•</span>
						<span>{songs.length} songs</span>
						{playlist.createdAtDate && (
							<>
								<span>•</span>
								<span>{fmtDate(playlist.createdAtDate)}</span>
							</>
						)}
					</div>
				</div>
			</div>


			{/* Songs List */}
			{songs.length === 0 ? (
				<div className="empty-playlist text-center py-5 text-muted">
					<i className="bi bi-music-note-list display-3 mb-3" />
					<h3>No songs in this playlist</h3>
				</div>
			) : (
				<>
					{/* Header Row (hidden on xs/sm) */}
					<div className="row align-items-center text-secondary small fw-bold border-bottom py-2 px-3 d-none d-md-flex">
						<div className="col-auto pe-0"></div>
						<div className="col-auto">#</div>
						<div className="col">Title</div>
						<div className="col-md-2">Artist</div>
						<div className="col-md-2">Likes / Plays</div>
						<div className="col-auto">Action</div>
					</div>

					{/* Item Rows */}
					<div ref={listRef}>
						{songs.map((item, idx) => (
							<div
								key={item.musicId}
								className={`row align-items-center border-bottom py-2 px-3 ${draggedIdx === idx ? 'bg-light opacity-75' : ''
									}`}
								draggable
								onDragStart={e => handleDragStart(e, idx)}
								onDragOver={handleDragOver}
								onDrop={e => handleDrop(e, idx)}
							>
								{/* Drag Handle */}
								<div className="col-auto pe-0">
									<i className="bi bi-grip-vertical text-secondary"></i>
								</div>

								{/* Index */}
								<div className="col-auto pe-3 text-secondary">
									{idx + 1}
								</div>

								<div className="col d-flex align-items-center">
									<img
										src={`${baseUrl}/stream/image/music/${item.musicId}?size=thumb`}
										alt=""
										className="rounded flex-shrink-0 me-2"
										style={{ width: 40, height: 40, objectFit: 'cover' }}
									/>
									<div className="text-truncate">
										<div className="fw-medium text-truncate">{item.music?.title || '[Unknown]'}</div>
										{item.music?.premiumContent && (
											<small className="text-warning">
												<i className="bi bi-crown-fill me-1" /> Premium
											</small>
										)}
									</div>
								</div>

								<div className="col-md-2 d-none d-md-block text-truncate text-secondary">
									{item.artistUsername || '[Unknown]'}
								</div>

								<div className="col-md-2 d-none d-md-flex flex-column small text-secondary">
									<span>
										{item.music?.likeCount ?? '--'} <i className="bi bi-heart-fill text-danger"></i>
									</span>
									<span>
										{item.music?.totalPlayCount ?? '--'} <i className="bi bi-play-fill text-primary"></i>
									</span>
								</div>

								{/* Actions */}
								<div className="col-auto text-end">
									<button
										className="btn btn-sm btn-outline-danger"
										onClick={() => removeSong(item.musicId)}
									>
										<i className="bi bi-trash-fill"></i>
									</button>
								</div>
							</div>
						))}
					</div>
				</>
			)}

		</div>
	);
}
