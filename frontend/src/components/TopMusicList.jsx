const TopMusicList = ({ items }) => {
	const formatCount = (count) => {
		return new Intl.NumberFormat('en', {
			notation: "compact",
			compactDisplay: "short",
			maximumFractionDigits: 1
		}).format(count);
	};

	return (
		<div className="col-md-6 mb-4">
			<div className="card shadow-sm h-100">
				<div className="card-body">
					<h5 className="card-title">Top 5 Music</h5>
					<ul className="list-group list-group-flush">
						{items.map((track, idx) => (
							<li key={idx} className="list-group-item d-flex justify-content-between align-items-center">
								<span>{track.title}</span>
								<span className="badge bg-primary rounded-pill">
									{formatCount(track.totalPlayCount)} plays
								</span>
							</li>
						))}
					</ul>
				</div>
			</div>
		</div>
	);
};

export default TopMusicList;
