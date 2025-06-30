const ChartCard = ({ baseUrl, dateType, start, end, type, title, canvasRef }) => {

	const chartInstance = React.useRef(null);
	const [loading, setLoading] = React.useState(true);

	React.useEffect(() => {
		const fetchAndRenderChart = async () => {
			try {
				const res = await fetch(`${baseUrl}/api/report/chart?type=${type}&dateType=${dateType}&start=${start}&end=${end}`);
				const chartJson = await res.json();

				setTimeout(() => {
					if (!canvasRef.current) return;

					// Destroy old chart if exists
					if (chartInstance.current) {
						chartInstance.current.destroy();
					}

					const ctx = canvasRef.current.getContext('2d');
					if (!ctx) {
						console.warn(`Canvas context not found for ${type}`);
						return;
					}

					chartInstance.current = new window.Chart(ctx, {
						type: 'line',
						data: {
							labels: chartJson.data.results.labels,
							datasets: chartJson.data.results.datasets
						},
						options: { responsive: true }
					});
					setLoading(false);
				}, 50);
			} catch (err) {
				console.error(`Error fetching chart ${type}:`, err);
			}
		};
		fetchAndRenderChart();
	}, [baseUrl, start, end, dateType, type]);

	return (

		<div className="col-md-6 mb-4">
			<div className="card shadow-sm">
				<div className="card-body">
					{loading && (
						<div className="text-center my-5">
							<div className="spinner-border text-primary" role="status">
								<span className="visually-hidden">Loading...</span>
							</div>
						</div>
					)}

					<>
						<h5 className="card-title">{title}</h5>
						<canvas ref={canvasRef}></canvas>
					</>

				</div>
			</div>
		</div>
	);
};

export default ChartCard;