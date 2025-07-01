<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/channel.css">
<div class="container mt-4">
	<!-- User Profile Section -->
	<div class="profile-header">
		<div class="profile-content">
			<div class="row align-items-center">
				<div class="col-md-2">
					<img
						src="${pageContext.request.contextPath}/stream/image/user/${artist.userId}"
						alt="${artist.username}" class="avatar-large">
				</div>
				<div class="col-md-10">
					<h2 class="mb-1">
						<c:out value="${artist.username}" />
					</h2>
					<h3 class="mb-2">
						<c:out value="${artist.firstName}" />
						<c:out value="${artist.lastName}" />
					</h3>

					<div class="profile-stats">
						<div class="stat-item">
							<span class="stat-number">${musicCount}</span> <span
								class="stat-label">Music(s)</span>
						</div>
					</div>
					<div class="mt-3">
						<a href="mailto:<c:out value='${artist.email}'/>"
							class="btn btn-action"><i class="bi bi-envelope"></i></a>

					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Profile Navigation Tabs -->
	<div class="section-tabs">
		<ul class="nav nav-tabs" id="profileTabs" role="tablist">
			<li class="nav-item" role="presentation"><a
				class="nav-link active" type="button" role="tab">All</a></li>
			<li class="nav-item" role="presentation"><a class="nav-link"
				id="tracks-tab" type="button" role="tab">Tracks</a></li>
			<li class="nav-item" role="presentation"><a class="nav-link"
				id="playlists-tab" type="button" role="tab">Playlists</a></li>
		</ul>
	</div>

	<!-- Tab Content -->
	<div class="tab-content" id="profileTabContent">
		<div class="tab-pane fade show active" id="all" role="tabpanel">
			<div class="row">
				<div class="col-md-8">
					<h5 class="mb-3">Recent</h5>

					<!-- More track items would go here -->
					<div class="track-item">
						<button class="btn btn-dark btn-sm rounded-circle me-3">
							<i class="bi bi-play-fill"></i>
						</button>
						<img
							src="https://via.placeholder.com/60x60/e74c3c/ffffff?text=Music"
							alt="Track" class="track-thumbnail">
						<div class="track-details">
							<div class="track-name">Another Track Name</div>
							<div class="track-meta">8 years ago</div>
						</div>
						<div class="text-muted">3:45</div>
					</div>
				</div>

				<div class="col-md-4">
					<!-- Fans Section -->
					<div class="fans-section">
						<div
							class="d-flex justify-content-between align-items-center mb-3">
							<h6 class="mb-0">FANS</h6>
							<a href="#" class="text-muted small">View all</a>
						</div>
						<div class="mb-3">
							<span class="badge bg-secondary">Top</span>
						</div>
						<p class="small text-muted mb-3">Fans who have played this
							track the most:</p>

						<div class="fan-item">
							<div class="fan-info">
								<span class="me-2">1</span> <img
									src="https://via.placeholder.com/30x30/333/fff?text=E"
									alt="Fan" class="avatar" style="width: 30px; height: 30px;">
								<span>Electric</span>
							</div>
							<span class="fan-plays">78 plays</span>
						</div>
					</div>

					<!-- Likes Section -->
					<div class="fans-section mt-4">
						<div
							class="d-flex justify-content-between align-items-center mb-3">
							<h6 class="mb-0">3 LIKES</h6>
							<a href="#" class="text-muted small">View all</a>
						</div>

						<div class="fan-item">
							<div class="fan-info">
								<img src="https://via.placeholder.com/30x30/4a90e2/fff?text=C"
									alt="Fan" class="avatar" style="width: 30px; height: 30px;">
								<div>
									<div class="small">Christine Vo 2</div>
									<div class="small text-muted">BRE@TH//LESS</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>