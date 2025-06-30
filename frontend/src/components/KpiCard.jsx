const KpiCard = ({ title, value }) => (
	<div className="col-md-4 mb-3">
		<div className="card text-white bg-primary h-100 shadow-sm">
			<div className="card-body">
				<h5 className="card-title">{title}</h5>
				<p className="card-text fs-4 fw-bold">{value}</p>
			</div>
		</div>
	</div>
);

export default KpiCard;