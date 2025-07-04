import KpiCard from "./KpiCard.js";

export const AdminMusicList = ({ baseUrl }) => {
	const [kpiData, setKpiData] = React.useState(null);
	const [music, setMusic] = React.useState([]);
	const [loading, setLoading] = React.useState(true);
	const [error, setError] = React.useState(null);

	// Pagination state
	const [currentPage, setCurrentPage] = React.useState(1);
	const [totalMusic, setTotalMusic] = React.useState(0);
	const [musicPerPage] = React.useState(10); // You can make this configurable

	// Calculate pagination values
	const totalPages = Math.ceil(totalMusic / musicPerPage);
	const offset = (currentPage - 1) * musicPerPage;

	const fetchMusic = async (page = 1) => {
		try {
			setLoading(true);
			const pageOffset = (page - 1) * musicPerPage;
			const musicRes = await fetch(`${baseUrl}/api/music?limit=${musicPerPage}&offset=${pageOffset}`);
			const musicJson = await musicRes.json();
			
			setMusic(musicJson.data.results);
		} catch (err) {
			console.error("Error loading music:", err);
			setError("Failed to load music.");
		} finally {
			setLoading(false);
		}
	};

	React.useEffect(() => {
		const fetchData = async () => {
			try {
				const kpiRes = await fetch(`${baseUrl}/api/report/kpis?type=music`);
				const kpiJson = await kpiRes.json();
				setKpiData(kpiJson.data.results);
				setTotalMusic(kpiJson.data.results.totalMusicCount);
			} catch (err) {
				console.error("Error loading KPI data:", err);
				setError("Failed to load KPI data.");
			}
		};

		fetchData();
	}, [baseUrl]);

	React.useEffect(() => {
		fetchMusic(currentPage);
	}, [baseUrl, currentPage, musicPerPage]);

	const handlePageChange = (page) => {
		if (page >= 1 && page <= totalPages) {
			setCurrentPage(page);
		}
	};

	const handleDelete = async (musicId) => {
		if (!confirm("Are you sure you want to delete this music?")) return;
		try {
			const res = await fetch(`${baseUrl}/api/music/${musicId}`, {
				method: "DELETE"
			});
			if (!res.ok) throw new Error("Delete failed");
			
			// Refresh the current page after deletion
			await fetchMusic(currentPage);
			toastr.success("Music deleted.");
		} catch (err) {
			console.error("Error deleting music:", err);
			toastr.error("Failed to delete music.");
		}
	};


	const renderPagination = () => {
		if (totalPages <= 1) return null;

		const pages = [];
		const maxVisiblePages = 5;
		
		let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
		let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
		
		if (endPage - startPage + 1 < maxVisiblePages) {
			startPage = Math.max(1, endPage - maxVisiblePages + 1);
		}

		// Previous button
		pages.push(
			<li key="prev" className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
				<button 
					className="page-link" 
					onClick={() => handlePageChange(currentPage - 1)}
					disabled={currentPage === 1}
				>
					Previous
				</button>
			</li>
		);

		// First page + ellipsis
		if (startPage > 1) {
			pages.push(
				<li key={1} className="page-item">
					<button className="page-link" onClick={() => handlePageChange(1)}>1</button>
				</li>
			);
			if (startPage > 2) {
				pages.push(
					<li key="ellipsis1" className="page-item disabled">
						<span className="page-link">...</span>
					</li>
				);
			}
		}

		// Page numbers
		for (let i = startPage; i <= endPage; i++) {
			pages.push(
				<li key={i} className={`page-item ${currentPage === i ? 'active' : ''}`}>
					<button className="page-link" onClick={() => handlePageChange(i)}>
						{i}
					</button>
				</li>
			);
		}

		// Last page + ellipsis
		if (endPage < totalPages) {
			if (endPage < totalPages - 1) {
				pages.push(
					<li key="ellipsis2" className="page-item disabled">
						<span className="page-link">...</span>
					</li>
				);
			}
			pages.push(
				<li key={totalPages} className="page-item">
					<button className="page-link" onClick={() => handlePageChange(totalPages)}>
						{totalPages}
					</button>
				</li>
			);
		}

		// Next button
		pages.push(
			<li key="next" className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
				<button 
					className="page-link" 
					onClick={() => handlePageChange(currentPage + 1)}
					disabled={currentPage === totalPages}
				>
					Next
				</button>
			</li>
		);

		return (
			<nav aria-label="Music pagination">
				<ul className="pagination justify-content-center mb-0">
					{pages}
				</ul>
			</nav>
		);
	};
	

	if (loading) {
		return (
			<div className="text-center my-5">
				<div className="spinner-border text-primary" role="status">
					<span className="visually-hidden">Loading...</span>
				</div>
			</div>
		);
	}

	if (error) {
		return <div className="alert alert-danger">{error}</div>;
	}

	return (
		<div className="container-xl mt-5">
			<h2 className="mb-4">Music Management</h2>

			{kpiData && (
				<div className="row mb-4">
					<KpiCard title="Total Music" value={kpiData.totalMusicCount} />
					<KpiCard title="Public Music" value={kpiData.publicMusicCount} />
					<KpiCard title="Non-Premium Music" value={kpiData.nonPremiumCount} />
					<KpiCard title="Premium Music" value={kpiData.premiumCount} />
				</div>
			)}

			<div className="card shadow-sm">
				<div className="card-body">
					<div className="d-flex justify-content-between align-items-center mb-3">
						<h5 className="card-title mb-0">Music Tracks</h5>
						<div className="d-flex align-items-center gap-3">
							<small className="text-muted">
								Showing {offset + 1}-{Math.min(offset + musicPerPage, totalMusic)} of {totalMusic} tracks
							</small>
						</div>
					</div>
					
					<div className="table-responsive">
						<table className="table table-hover">
							<thead>
								<tr>
									<th>Track</th>
									<th>Play Count</th>
									<th>Like Count</th>
									<th>Status</th>
									<th>Premium</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{music.map((track) => (
									<tr key={track.musicId}>
										<td className="d-flex align-items-center">
											<img
												src={`${baseUrl}/stream/image/music/${track.musicId}?size=thumb`}
												alt="Album Art"
												style={{
													width: "40px",
													height: "40px",
													objectFit: "cover",
													borderRadius: "4px",
													marginRight: "10px"
												}}
											/>
											<div>
												<div className="fw-medium">{track.title}</div>
											</div>
										</td>
										<td>{track.totalPlayCount}</td>
										<td>{track.likeCount}</td>
										<td>
											{track.visibility === 'PUBLIC' ? 'Public' : 'Private'}		
										</td>
										<td>
											<span className={`badge ${track.premiumContent ? 'bg-warning' : 'bg-info'}`}>
												{track.isPremium ? 'Premium' : 'Free'}
											</span>
										</td>
										<td>
											<button
												className="btn btn-sm btn-danger"
												onClick={() => handleDelete(track.musicId)}
											>
												Delete
											</button>
										</td>
									</tr>
								))}
							</tbody>
						</table>
					</div>

					{/* Pagination */}
					<div className="d-flex justify-content-center mt-3">
						{renderPagination()}
					</div>
				</div>
			</div>
		</div>
	);
};