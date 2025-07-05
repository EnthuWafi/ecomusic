<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-5">
	<!-- KPI Cards -->
	<div class="row">
		<!-- Total Revenue -->
		<div class="col-md-4 mb-3">
			<div class="card text-white bg-primary h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">Total Revenue</h5>
					<p class="card-text fs-4 fw-bold">
						<fmt:formatNumber value="${totalRevenue}" type="currency" />
					</p>
				</div>
			</div>
		</div>
		<!-- Total Plays -->
		<div class="col-md-4 mb-3">
			<div class="card text-white bg-success h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">Total Plays</h5>
					<p class="card-text fs-4 fw-bold">
						<fmt:formatNumber value="${totalPlayCount}" pattern="#,###" />
					</p>
				</div>
			</div>
		</div>
		<!-- Music Count -->
		<div class="col-md-4 mb-3">
			<div class="card text-white bg-info h-100 shadow-sm">
				<div class="card-body">
					<h5 class="card-title">Total Tracks</h5>
					<p class="card-text fs-4 fw-bold">
						<fmt:formatNumber value="${musicCount}" pattern="#" />
					</p>
				</div>
			</div>
		</div>
	</div>

	<!-- Monthly Plays Chart -->
	<div id="chart-root"></div>

	<!-- Top Performing Music -->
	<div class="row">
		<div class="col-lg-6 col-sm-12">

			<div class="card mb-5 shadow-sm">
				<div class="card-header">
					<h6 class="mb-0">Top Played Tracks</h6>
				</div>
				<div class="card-body p-0">
					<ul class="list-group list-group-flush">
						<c:forEach var="track" items="${topMusics}" varStatus="loop">
							<li onclick="location.href = '${pageContext.request.contextPath}/music/play/${track.musicId}'"
								class="list-group-item  list-group-item-action d-flex justify-content-between align-items-center">
								<img
								src="${pageContext.request.contextPath}/stream/image/music/${track.musicId}?size=thumb"
								class="rounded"
								style="width: 80px; height: 80px; object-fit: cover;"> <span>
									<strong>${loop.index + 1}. </strong> <c:out
										value="${track.title}" />
							</span> <span> <i class="bi bi-hand-thumbs-up me-1"></i>
									${track.likeCountString} <span class="mx-2">|</span> <i
									class="bi bi-eye me-1"></i> ${track.totalPlayCountString}
							</span>
							</li>
						</c:forEach>
						<c:if test="${empty topMusics}">
							<li class="list-group-item text-center text-muted">No tracks
								to display.</li>
						</c:if>
					</ul>
				</div>
			</div>

		</div>
		<div class="col-lg-6 col-sm-12">

			<div class="card mb-5 shadow-sm">
				<div class="card-header">
					<h6 class="mb-0">Top Liked Tracks</h6>
				</div>
				<div class="card-body p-0">
					<ul class="list-group list-group-flush">
						<c:forEach var="track" items="${likedMusics}" varStatus="loop">
							<li onclick="location.href = '${pageContext.request.contextPath}/music/play/${track.musicId}'"
								class="list-group-item  list-group-item-action d-flex justify-content-between align-items-center">
								<img
								src="${pageContext.request.contextPath}/stream/image/music/${track.musicId}?size=thumb"
								class="rounded"
								style="width: 80px; height: 80px; object-fit: cover;"> <span>
									<strong>${loop.index + 1}. </strong> <c:out
										value="${track.title}" />
							</span> <span> <i class="bi bi-hand-thumbs-up me-1"></i>
									${track.likeCountString} <span class="mx-2">|</span> <i
									class="bi bi-eye me-1"></i> ${track.totalPlayCountString}
							</span>
							</li>
						</c:forEach>
						<c:if test="${empty likedMusics}">
							<li class="list-group-item text-center text-muted">No tracks
								to display.</li>
						</c:if>
					</ul>
				</div>
			</div>

		</div>

	</div>

</div>

<script type="module">
  import { ArtistChartCard } from "${pageContext.request.contextPath}/assets/js/components/ArtistChartCard.js";

  const userId = ${sessionScope.user.userId};
  const container = document.getElementById('chart-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(ArtistChartCard, { baseUrl: window.baseUrl, userId: userId}));
</script>